import java.util.NoSuchElementException;

/**
 * For storing doubles in a list
 * O(N) time complexity for random access
 * O(1) time complexity for insertion or removal from either end
 * O(N) time complexity for random insertion and deletion
 * @author Joshua Shew
 */
public class NumLinkedList implements NumList {
    /* Stores a reference to the first node in the list */
    private LLNode front = null;

    /* Stores a reference to the last node in the list */
    private LLNode back = null;

    /* Keeps track of how many elements are in the list */
    private int size = 0;

    /* The maximum capacity of the list */
    private final int capacity = Integer.MAX_VALUE;

    /* Keeps track of whether or not the list is sorted */
    private boolean sorted = true;

    /**
     * Constructor for an empty linked list
     */
    public NumLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
        this.sorted = true;
    }

    /**
     * Getter for the front of the list
     * @return a reference to the first node in the list
     */
    private LLNode getFront() {
        return this.front;
    }

    /**
     * Setter for the front of the list
     * @param front a reference to the first node in the list
     */
    private void setFront(LLNode front) {
        this.front = front;
    }

    /**
     * Getter for the back of the list
     * @return a reference to the last node in the list
     */
    private LLNode getBack() {
        return this.back;
    }

    /**
     * Setter for the back of the list
     * @param back
     */
    private void setBack(LLNode back) {
        this.back = back;
    }

    /**
     * Getter method for the size of the list
     * The size is the number of numbers currently in the list
     * @return the size of the list
     */
    public int size() {
        return this.size;
    }

    /**
     * Increments the size field
     */
    private void incrementSize() {
        this.size++;
    }

    /**
     * Decrements the size field
     */
    private void decrementSize() {
        this.size--;
    }

    /**
     * Getter method for the capacity of the list
     * The capacity is the number of numbers the list can hold without resizing
     * @return the capacity of the list
     */
    public int capacity() {
        return this.capacity;
    }

    /**
     * Setter for the sorted field
     * @param sorted whether or not the list is sorted
     */
    private void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    /**
     * Adds a number of the end of the list
     * O(1)
     * @param value the number to be added to the end of the list
     */
    public void add(double value) {
        /* case for an empty list */
        if (getFront() == null) {
            setFront(new LLNode(value, null, null));
            setBack(getFront());
        }
        else {
            /* updates sorted based on the new value if the list is sorted, no update otherwise */
            setSorted(isSorted() ? value >= getBack().getValue() : isSorted());

            /* adds the new node to the back & updates the next for the previous "back" node */
            setBack(new LLNode(value, getBack(), null));
            getBack().getPrev().setNext(getBack());
        }

        incrementSize();
    }

    /**
     * Adds a number at a specified position of the list
     * All numbers after (and the number at) the specified position are shifted down the list
     * If the list has i or fewer numbers, then the value is added in the same fashion as the add method
     * O(1) for front or back
     * O(N) for random index
     * @param i the index to insert the new value at
     * @param value the value to add to the list
     */
    public void insert(int i, double value) {
        /* cover cases when insert should behave like add */
        if (i >= size()) {
            add(value);
        }
        else if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        else if (i == 0) {
            /* updates sorted based on the new value if the list is sorted, no update otherwise */
            setSorted(isSorted() ? value <= getFront().getValue() : isSorted());

            setFront(new LLNode(value, null, getFront()));
            getFront().getNext().setPrev(getFront());
        }
        else if (i == size() - 1) {
            /* updates sorted based on the new value if the list is sorted, no update otherwise */
            setSorted(isSorted() ?
                      getBack().getPrev().getValue() <= value && value <= getBack().getValue() :
                      isSorted());

            getBack().setPrev((new LLNode(value, getBack().getPrev(), getBack())));
            getBack().getPrev().getPrev().setNext(getBack().getPrev());
        }
        else {
            /* finds the node directly before the insertion point */
            LLNode nodePtr = nodeLookup(i - 1);

            /* updates sorted based on the new value if the list is sorted, no update otherwise */
            setSorted(isSorted() ?
                      nodePtr.getValue() <= value && value <= nodePtr.getNext().getValue() :
                      isSorted());

            nodePtr.setNext(new LLNode(value, nodePtr, nodePtr.getNext()));
            nodePtr.getNext().getNext().setPrev(nodePtr.getNext());
        }

        incrementSize();
    }

    /**
     * Removes the input node from the list
     * O(1)
     * @param node the node to remove from the list
     * @return the node before the node that was removed
     */
    public LLNode removeNode(LLNode node) {
        decrementSize();

        /* When the node is the only node in the list */
        if (node.getPrev() == null && node.getNext() == null) {
            setFront(null);
            setBack(null);

            return null;
        }
        /* When the node is at the front of the list */
        else if (node.getPrev() == null) {
            setFront(node.getNext());
            node.getNext().setPrev(null);

            return null;
        }
        /* When the node is at the back of the list*/
        else if (node.getNext() == null) {
            setBack(node.getPrev());
            node.getPrev().setNext(null);

            return getBack();
        }
        /* When the node is in the middle of the list */
        else {
            LLNode save = node.getPrev();

            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());

            return save;
        }
    }

    /**
     * Removes the number at the specified position of the list
     * All numbers after the specified position are shifted up the list
     * The size of the list is shortened by this method if there is a number at the specified position
     * O(1) for front or back
     * O(N) for random index
     * @param i the index where the value should be removed
     */
    public void remove(int i) {
        if (0 <= i && i < size()) {
            if (size == 1) {
                setFront(null);
                setBack(null);
            }
            /* Removing the first element in the list */
            else if (i == 0) {
                setFront(getFront().getNext());
                getFront().setPrev(null);
            }
            /* Removing the last element in the list */
            else if (i == size() - 1) {
                setBack(getBack().getPrev());
                getBack().setNext(null);
            }
            else {
                LLNode nodePtr = nodeLookup(i - 1);
                nodePtr.setNext(nodePtr.getNext().getNext());
            }

            decrementSize();
            setSorted(checkIfSorted());
        }
    }

    /**
     * Checks whether the list contains the input value
     * O(N)
     * @param value the value is what the method is confirming is in the list
     * @return true if the value is in the list, false otherwise
     */
    public boolean contains(double value) {
        DoubleIterator i = this.iterator();

        /* Linear search through the list */
        while (i.hasNext()) {
            if (i.next() == value) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the i-th node of the list
     * O(1) for front or back
     * O(N) for random access
     * @param index the index of the list to get the node from
     * @return a reference to the specified node
     * @throws IndexOutOfBoundsException when the input index is greater than or equal to the size of the list
     */
    private LLNode nodeLookup(int index) throws IndexOutOfBoundsException {
        // this check prevents a null pointer exception
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        else {
            LLNode nodePtr = null;
            /* advance from the back if the node is in the back half of the list */
            if (index > (int)(size() / 2)) {
                nodePtr = getBack();

                // go backward in the list
                for (int i = 0; i < size() - index - 1; i++) {
                    nodePtr = nodePtr.getPrev();
                }
            }
            /* advance from the front if the node is in the front half of hte list */
            else {
                nodePtr = getFront();

                // advance in the list index times
                for (int i = 0; i < index; i++) {
                    nodePtr = nodePtr.getNext();
                }
            }

            return nodePtr;
        }
    }

    /**
     * Returns the i-th element of the list
     * O(1) for front or back
     * O(N) for random index
     * @param i the index of the list to get the value from
     * @return the value stored at the specified index
     * @throws IndexOutOfBoundsException when the input index is greater than or equal to the size of the list
     */
    public double lookup(int i) throws IndexOutOfBoundsException {
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        
        return nodeLookup(i).getValue();
    }

    /**
     * Removes duplicates in this list while preserving the current order of the numbers
     * O(N^2)
     */
    public void removeDuplicates() {
        LLNode nodePtr1 = getFront();

        /* nodePtr1 goes through the entire list */
        while (nodePtr1 != null && nodePtr1.getNext() != null) {
            LLNode nodePtr2 = nodePtr1.getNext();
            /* nodePtr2 removes all elements right of nodePtr1 that are duplicates of nodePtr1 */
            while (nodePtr2 != null) {
                if (nodePtr2.getValue() == nodePtr1.getValue()) {
                    nodePtr2 = removeNode(nodePtr2);
                }

                nodePtr2 = nodePtr2.getNext();
            }

            nodePtr1 = nodePtr1.getNext();
        }

        setSorted(checkIfSorted());
    }

    /**
     * Checks the entire list to see if it is sorted
     * O(N)
     * @return true if the list is sorted, false otherwise
     */
    public boolean checkIfSorted() {
        /* An empty list or a list with only 1 element is sorted already */
        if (size() < 2) {
            return true;
        }
        /* Linear search for any elements out of order */
        else {
            DoubleIterator i = this.iterator();
            double prev = i.next();

            while (i.hasNext()) {
                if (i.peek() < prev) {
                    return false;
                }

                prev = i.next();
            }

            return true;
        }
    }

    /**
     * Returns whether the list is currently sorted in increasing order or not
     * O(1)
     * @return true if the list is sorted, false otherwise
     */
    public boolean isSorted() {
        return this.sorted;
    }

    /**
     * Reverses the order of the elements in the list
     * O(N)
     */
    public void reverse() {
        LLNode frontSave = getFront();

        setFront(getBack());
        setBack(frontSave);

        LLNode nodePtr = frontSave;
        /* Goes through and swaps next and previous for every node */
        while (nodePtr != null) {
            LLNode nextSave = nodePtr.getNext();

            nodePtr.setNext(nodePtr.getPrev());
            nodePtr.setPrev(nextSave);

            nodePtr = nextSave;
        }

        setSorted(checkIfSorted());
    }

    /**
     * Provides a String representation of this list
     * For an empty list, an empty String is returned
     * Numbers are separated by a space and no other characters are included
     * O(N)
     * @return a String representation of the list
     */
    public String toString() {
        StringBuilder result = new StringBuilder();

        DoubleIterator i = this.iterator();

        while (i.hasNext()) {
            result.append(i.next()).append(" ");
        }

        // Remove the last space
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString(); 
    }

    /**
     * Returns an iterator for the list that begins at the front of the list
     * O(1)
     * @return a DoubleIterator for the list the method is called on
     */
    public DoubleIterator iterator() {
        return new NumLinkedListIterator(this);
    }

    /**
     * LLNode represents a single node in a list of nodes connected to each other
     * All methods are O(1)
     * @author Joshua Shew
     */
    private static class LLNode {
        /* Stores the value in the node */
        private double value = 0.0;

        /* Stores a reference to the next node in the list */
        private LLNode next = null;

        /* Stores a reference to the previous node in the list */
        private LLNode prev = null;

        /**
         * Constructor for a node
         * @param value the value to store in the node
         * @param prev a reference to the previous node in the list
         * @param next a reference to the next node in the list
         */
        public LLNode(double value, LLNode prev, LLNode next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        /**
         * Getter for the value
         * @return the value stored in the node
         */
        public double getValue() {
            return this.value;
        }

        /**
         * Setter for the value
         * @param value the value to store in the node
         */
        public void setValue(double value) {
            this.value = value;
        }

        /**
         * Getter for the previous node in the list
         * @return a reference to the previous node in the list
         */
        public LLNode getPrev() {
            return this.prev;
        }

        /**
         * Setter for the previous node in the list
         * @param prev a reference to the previous node in the list
         */
        public void setPrev(LLNode prev) {
            this.prev = prev;
        }

        /**
         * Getter for the next node in the list
         * @return a reference to the next node in the list
         */
        public LLNode getNext() {
            return this.next;
        }

        /**
         * Setter for the next node in the list
         * @param next a reference to the next node in the list
         */
        public void setNext(LLNode next) {
            this.next = next;
        }
    }

    /**
     * Iterator for NumLinkedList
     * All methods are O(1)
     * @author Joshua Shew
     */
    private static class NumLinkedListIterator implements DoubleIterator {
        /* Stores a reference to the current position of the iterator */
        private LLNode nodePtr = null;
        
        /**
         * Constructor for an iterator for NumLinkedList
         * @param list the list to iterate over
         */
        public NumLinkedListIterator(NumLinkedList list) {
            this.nodePtr = list.getFront();
        }
        
        /**
         * Checks whether there is another value ahead of the iterator in the list
         * @return true if there is a value, false otherwise
         */
        public boolean hasNext() {
            return nodePtr != null;
        }
        
        /**
         * Moves the iterator forward one value and returns that value
         * @return the value at iterator's current index
         */
        public double next() {
            if (hasNext()) {
                double save = nodePtr.getValue();
                nodePtr = nodePtr.getNext();

                return save;
            }
            else {
                throw new NoSuchElementException();
            }
        }

        /**
         * Retrieves the value the iterator is current at without moving it forward
         * @return the value at the iterator's current index
         */
        public double peek() {
            if (hasNext()) {
                return nodePtr.getValue();
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Demonstration for the functionality of NumLinkedList
     * @param args unused
     */
    public static void main(String[] args) {
        String indent = "    ";
        String newLine = "\n";

        System.out.println(newLine + "*** Demonstration for the NumLinkedList class ***" + newLine);
    
        System.out.println("Methods:" + newLine +
                           indent + "NumLinkedList(); // constructor for an empty list" + newLine +
                           indent + "list.size(); // returns the size, the number of elements in the list" + newLine + 
                           indent + "list.capacity(); // returns the capacity, the number of elements the list can store without expanding" + newLine +
                           indent + "list.add(value); // adds the value to the end of the list" + newLine +
                           indent + "list.insert(index, value); // adds the value to the list at the specified index" + newLine +
                           indent + "list.remove(index); // removes the value stored at the specified index" + newLine +
                           indent + "list.contains(value); // returns whether the list contains the value or not" + newLine +
                           indent + "list.lookup(index); // returns the value stored at the index" + newLine +
                           indent + "list.equals(anotherList); // returns whether the two lists store the exact same values in the same order or not" + newLine +
                           indent + "list.removeDuplicates(); // removes duplicate values in the list" + newLine +
                           indent + "list.isSorted(); // returns whether the list is sorted in ascending order or not" + newLine +
                           indent + "list.reverse(); // reverses the order of the elements in the list" + newLine +
                           indent + "NumList.union(list1, list2); // returns a combination of the two lists with no duplicates" + newLine +
                           indent + "list.toString(); // returns the values in the list separated by spaces" + newLine);
        
        System.out.println("Demonstration for NumLinkedList(), size(), add(value), and toString()");
        System.out.println("--- Code ---");
        System.out.println("NumLinkedList list = new NumLinkedList();" + newLine +
                           "System.out.println(list.size());" + newLine +
                           newLine +
                           "list.add(0.0);" + newLine +
                           "list.add(1.0);" + newLine +
                           "System.out.println(list.size());" + newLine +
                           "System.out.println(list.toString());" + newLine);
        System.out.println("-- Output --");

        NumLinkedList list = new NumLinkedList();
        System.out.println(list.size());
        
        list.add(0.0);
        list.add(1.0);
        System.out.println(list.size());
        System.out.println(list.toString());

        System.out.println(""); // new line

        System.out.println("Demonstration for insert(index, value), remove(index), contains(value), lookup(index)");
        System.out.println("--- Code ---");
        System.out.println("NumLinkedList list = new NumLinkedList();" + newLine +
                           "System.out.println(list.size());" + newLine +
                           newLine +
                           "list.insert(0, 3.0);" + newLine +
                           "list.insert(0, 1.0);" + newLine +
                           "list.insert(0, 0.0);" + newLine +
                           "list.insert(2, 2.0);" + newLine +
                           "list.insert(2, 2.0);" + newLine +
                           newLine +
                           "System.out.println(list.toString());" + newLine +
                           newLine +
                           "System.out.println(list.lookup(3));" + newLine +
                           "list.remove(3);" + newLine +
                           "System.out.println(list.lookup(3));" + newLine +
                           "System.out.println(list.toString());" + newLine +
                           newLine +
                           "System.out.println(list.contains(0.0));" + newLine +
                           "System.out.println(list.contains(0.5));" + newLine);
        System.out.println("-- Output --");

        list = new NumLinkedList();
        System.out.println(list.size());

        list.insert(0, 3.0);
        list.insert(0, 1.0);
        list.insert(0, 0.0);
        list.insert(2, 2.0);
        list.insert(2, 2.0);

        System.out.println(list.toString());

        System.out.println(list.lookup(3));
        list.remove(3);
        System.out.println(list.lookup(3));
        System.out.println(list.toString());

        System.out.println(list.contains(0.0));
        System.out.println(list.contains(0.5));

        System.out.println(""); // new line

        System.out.println("Demonstration for equals(anotherList) and removeDuplicates()");
        System.out.println("--- Code ---");
        System.out.println("NumLinkedList list = new NumLinkedList();" + newLine +
                           "list.add(0.0);" + newLine +
                           "list.add(1.0);" + newLine +
                           "list.add(2.0);" + newLine +
                           "list.add(1.0);" + newLine +
                           "list.add(2.0);" + newLine +
                           "list.add(3.0);" + newLine +
                           newLine +
                           "System.out.println(list.toString());" + newLine +
                           newLine +
                           "// Copying the list" + newLine +
                           "NumLinkedList listCopy = new NumLinkedList();" + newLine +
                           "listCopy.add(0.0);" + newLine +
                           "listCopy.add(1.0);" + newLine +
                           "listCopy.add(2.0);" + newLine +
                           "listCopy.add(1.0);" + newLine +
                           "listCopy.add(2.0);" + newLine +
                           "listCopy.add(3.0);" + newLine +
                           newLine +
                           "System.out.println(list.equals(listCopy));" + newLine +
                           newLine +
                           "list.removeDuplicates();" + newLine +
                           newLine +
                           "System.out.println(list.toString());" + newLine +
                           "System.out.println(listCopy.toString());" + newLine +
                           "System.out.println(list.equals(listCopy));" + newLine);
        System.out.println("-- Output --");

        list = new NumLinkedList();
        list.add(0.0);
        list.add(1.0);
        list.add(2.0);
        list.add(1.0);
        list.add(2.0);
        list.add(3.0);

        System.out.println(list.toString());

        // Copying the list
        NumLinkedList listCopy = new NumLinkedList();
        listCopy.add(0.0);
        listCopy.add(1.0);
        listCopy.add(2.0);
        listCopy.add(1.0);
        listCopy.add(2.0);
        listCopy.add(3.0);

        System.out.println(list.equals(listCopy));

        list.removeDuplicates();

        System.out.println(list.toString());
        System.out.println(listCopy.toString());
        System.out.println(list.equals(listCopy));

        System.out.println(""); // new line

        System.out.println("Demonstration for isSorted() and reverse()");
        System.out.println("--- Code ---");
        System.out.println("NumLinkedList list = new NumLinkedList();" + newLine +
                           "list.add(0.0);" + newLine +
                           "list.add(1.0);" + newLine +
                           "list.add(2.0);" + newLine +
                           newLine +
                           "System.out.println(list.isSorted());" + newLine +
                           newLine + 
                           "list.reverse();" + newLine +
                           "System.out.println(list.toString());" + newLine +
                           "System.out.println(list.isSorted());" + newLine +
                           "list.reverse();" + newLine +
                           "System.out.println(list.isSorted());" + newLine +
                           newLine +
                           "list.insert(0, 1.0);" + newLine +
                           "System.out.println(list.isSorted());" + newLine);
        System.out.println("-- Output --");

        list = new NumLinkedList();

        list.add(0.0);
        list.add(1.0);
        list.add(2.0);

        System.out.println(list.isSorted());

        list.reverse();
        System.out.println(list.toString());
        System.out.println(list.isSorted());
        list.reverse();
        System.out.println(list.isSorted());

        list.insert(0, 1.0);
        System.out.println(list.isSorted());

        System.out.println(""); // new line

        System.out.println("Demonstration for union(list1, list2)");
        System.out.println("--- Code ---");
        System.out.println("NumLinkedList list1 = new NumLinkedList();" + newLine +
                           "NumLinkedList list2 = new NumLinkedList();" + newLine +
                           newLine + 
                           "list1.add(1.0);" + newLine +
                           "list1.add(3.0);" + newLine +
                           "list1.add(5.0);" + newLine +
                           "list1.add(5.0);" + newLine +
                           "list1.add(7.0);" + newLine +
                           "list1.add(9.0);" + newLine +
                           newLine +
                           "list2.add(0.0);" + newLine +
                           "list2.add(2.0);" + newLine +
                           "list2.add(4.0);" + newLine +
                           "list2.add(5.0);" + newLine +
                           "list2.add(5.0);" + newLine +
                           "list2.add(6.0);" + newLine +
                           "list2.add(8.0);" + newLine +
                           "list2.add(9.0);" + newLine +
                           "list2.add(10.0);" + newLine +
                           newLine + 
                           "System.out.println(list1.toString());" + newLine +
                           "System.out.println(list2.toString());" + newLine +
                           "System.out.println(NumList.union(list1, list2).toString());" + newLine);
        System.out.println("-- Output --");

        NumLinkedList list1 = new NumLinkedList();
        NumLinkedList list2 = new NumLinkedList();

        list1.add(1.0);
        list1.add(3.0);
        list1.add(5.0);
        list1.add(5.0);
        list1.add(7.0);
        list1.add(9.0);

        list2.add(0.0);
        list2.add(2.0);
        list2.add(4.0);
        list2.add(5.0);
        list2.add(5.0);
        list2.add(6.0);
        list2.add(8.0);
        list2.add(9.0);
        list2.add(10.0);

        System.out.println(list1.toString());
        System.out.println(list2.toString());
        System.out.println(NumList.union(list1, list2).toString());

        System.out.println(""); // new line

        System.out.println("*** Demonstration Complete ***" + newLine);
    }
}
