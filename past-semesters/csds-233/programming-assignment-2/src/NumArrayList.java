import java.util.NoSuchElementException;

/**
 * For storing doubles in a list
 * O(1) time complexity for access
 * O(N) time complexity for adding, inserting, and deleting
 * @author Joshua Shew
 */
public class NumArrayList implements NumList {
    /* internalArray is for storing the values in the NumArrayList */
    private double[] internalArray = null;

    /* size keeps track of how many elements are stored in the list */
    private int size = 0;

    /* Keeps track of whether the list is sorted or not */
    private boolean sorted = true;

    /**
     * Creates a new NumArrayList with a capacity of 0
     */
    public NumArrayList() {
        this.internalArray = new double[0];
        this.size = 0;
    }

    /**
     * Creates a new NumArrayList with the specified capacity
     * @param capacity
     */
    public NumArrayList(int capacity) {
        this.internalArray = new double[capacity];
        this.size = 0;
    }
    
    /**
     * Getter method for the internalArray
     * @return the internalArray
     */
    private double[] getInternalArray() {
        return this.internalArray;
    }

    /**
     * Setter method for the internalArray
     * @param internalArray the new array to set as the internal array
     */
    private void setInternalArray(double[] internalArray) {
        this.internalArray = internalArray;
    }

    /**
     * Getter method for the size of the array
     * The size is the number of numbers currently in the array
     * @return the size of the array
     */
    public int size() {
        return this.size;
    }

    /**
     * Increments the size
     */
    public void incrementSize() {
        this.size++;
    }

    /**
     * Decrements the size
     */
    public void decrementSize() {
        this.size--;
    }

    /**
     * Getter method for the capacity of the array
     * The capacity is the number of numbers the array can hold without resizing
     * @return the capacity of the array
     */
    public int capacity() {
        return getInternalArray().length;
    }

    private void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    /**
     * Changes the value stored at the specified index in the list
     * @param i the index to store the value at
     * @param value the new value to store
     */
    private void set(int i, double value) {
        getInternalArray()[i] = value;
    }

    /**
     * Adds a number of the end of the array
     * The method expands the capacity if needed
     * O(1) if capacity is greater than size
     * O(N) if capacity needs to be expanded
     * @param value the number to be added to the end of the array
     */
    public void add(double value) {
        if (size() == capacity()) {
            increaseCapacity();
        }
        
        set(size(), value);
        
        this.size++;

        /* Updates the sorted field if the list becomes unsorted with the addition of the new value */
        if (size() > 1 && (isSorted() && value < lookup(size() - 2))) {
            setSorted(false);
        }
    }

    /**
     * Adds a number at a specified position of the array
     * All numbers after (and the number at) the specified position are shifted down the array
     * If the array has i or fewer numbers, then the value is added in the same fashion as the add method
     * O(N)
     * @param i the index to insert the new value at
     * @param value the value to add to the array
     */
    public void insert(int i, double value) {
        if (size () == capacity()) {
            increaseCapacity();
        }
        
        /* If the insertion is beyond the current size, just append the value */
        if (i >= size()) {
            add(value);
        }
        else if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        else {
            /* Shifts elements over to provide space for the insertion */
            for (int j = size(); j > i; j--) {
                set(j, lookup(j - 1));
            }

            set(i, value);
            incrementSize();

            /* Check to see if the list remains sorted after insertion */
            if (isSorted()) {
                if (i == 0) {
                    if (value > lookup(1)) {
                        setSorted(false);
                    }
                }
                else if (i == size() - 1) {
                    if (value < lookup(size() - 2)) {
                        setSorted(false);
                    }
                }
                /* Insertion into the middle of the list must check both sides of the value */
                else {
                    if (lookup(i - 1) > value || value > lookup(i + 1)) {
                        setSorted(false);
                    }
                }
            }
        }
    }

    /**
     * Removes the number at the specified position of the array
     * All numbers after the specified position are shifted up the array
     * The size of the array is shortened by this method if there is a number at the specified position
     * O(N)
     * @param i the index where the value should be removed
     */
    public void remove(int i) {
        if (i < size()) {
            /* Shifts elements over until the element to be removed is covered up */
            for (int j = i; j < size() - 1; j++) {
                set(j, lookup(j + 1));
            }

            decrementSize();
        }

        setSorted(checkIfSorted());
    }

    /**
     * Removes all elements of a certain value within the specified range of the list
     * The size of the lists is shortened by this method if there are elements to remove
     * O(N)
     * @param start the index to start searching at (inclusive)
     * @param end the index to stop searching at (excluded)
     * @param value the value to remove
     */
    private void removeAll(int start, int end, double value) {
        int shift = 0; // the number of values to shift by, also the number of values being removed

        int i = start;
        /* Go through the whole list and shift to remove values*/
        while (i + shift < size()) {
            /* While statement only helps to avoid errors with consecutive removals */
            while ((i + shift < end && i + shift < size()) && lookup(i + shift) == value) {
                shift++;
            }
            
            /* Shift over according to how many are being deleted */
            if (i + shift < size()) {
                set(i, lookup(i + shift));
            }

            i++;
        }

        /* Decrease the size for however many elements were removed */
        for (int j = 0; j < shift; j++) {
            decrementSize();
        }
    }

    /**
     * Checks whether the array contains the input value
     * O(N)
     * @param value the value is what the method is confirming is in the array
     * @return true if the value is in the array, false otherwise
     */
    public boolean contains(double value) {
        /* Linear search for the element */
        for (double element : getInternalArray()) {
            if (element == value) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the i-th element of the array
     * O(1)
     * @param i the index of the array to get the value from
     * @return the value stored at the specified index
     * @throws IndexOutOfBoundsException when the input index is greater than or equal to the size of the array
     */
    public double lookup(int i) throws IndexOutOfBoundsException {
        if (i >= size() || i < 0) {
            throw new IndexOutOfBoundsException();
        }
        
        return getInternalArray()[i];
    }

    /**
     * Removes duplicates in this array while preserving the current order of the numbers
     * O(N^2)
     */
    public void removeDuplicates() {
        /* Index goes from start to finish */
        for (int i = 0; i < size(); i++) {
            /* Remove all duplicates of the value at i from the remainder of the list */
            removeAll(i + 1, size(), lookup(i));
        }

        setSorted(checkIfSorted());
    }

    /**
     * Checks whether the list is sorted linearly
     * O(N)
     * @return true if the list is sorted, false otherwise
     */
    private boolean checkIfSorted() {
        /* Linear search for any elements in descending order */
        for (int i = 0; i < size() - 1; i++) {
            if (lookup(i) > lookup(i + 1)) {
                return false;
            }
        }

        return true;
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
        /* Iterates through half of the list & swaps values with the other half */
        for (int i = 0; i < (int)(size() / 2); i++) {
            double save = lookup(i);
            set(i, lookup(size - i - 1));
            set(size() - i - 1, save);
        }

        setSorted(checkIfSorted());
    }

    /**
     * Provides a String representation of this array
     * For an empty array, an empty String is returned
     * Numbers are separated by a space and no other characters are included
     * O(N)
     * @return a String representation of the array
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
        return new NumArrayListIterator(this);
    }

    /**
     * Iterator for NumArrayList
     * All methods are O(1)
     * @author Joshua Shew
     */
    private static class NumArrayListIterator implements DoubleIterator {
        /* Stores a reference to the list the iterator is iterating over */
        private NumArrayList list = null;
        
        /* Stores the current index of the iterator */
        private int index = 0;
        
        /**
         * Constructor for an iterator for NumArrayList
         * @param list
         */
        public NumArrayListIterator(NumArrayList list) {
            this.list = list;
            this.index = 0;
        }
        
        /**
         * Getter for the list
         * @return a reference to the list
         */
        private NumArrayList getList() {
            return this.list;
        }

        /**
         * Getter for the index
         * @return the current index
         */
        private int getIndex() {
            return this.index;
        }

        /**
         * Increments the index
         */
        private void incrementIndex() {
            this.index++;
        }

        /**
         * Checks whether there is another value ahead of the iterator in the list
         * @return true if there is a value, false otherwise
         */
        public boolean hasNext() {
            return getIndex() < getList().size();
        }

        /**
         * Moves the iterator forward one value and returns that value
         * @return the value at iterator's current index
         */
        public double next() {
            if (hasNext()) {
                double save = getList().lookup(getIndex());
                incrementIndex();
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
                return getList().lookup(getIndex());
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Copies the contents of one array onto another array and returns the new array
     * O(N)
     * @param emptyArray the array the values are copied onto. This is the array that is returned by the method
     * @param originalArray the array the values are copied from
     * @return the array the values were copied onto
     */
    private static double[] copyArray(double[] emptyArray, double[] originalArray) {
        for (int i = 0; i < originalArray.length; i++)
            emptyArray[i] = originalArray[i];
        
        return emptyArray;
    }

    /**
     * Increases the capacity of the NumArrayList
     * Doubles the capacity
     * O(N)
     */
    private void increaseCapacity() {
        /* Creates an array with double capacity or 1 if the current capacity is 0 */
        double[] tempArray = new double[Math.max(1, capacity() * 2)];
        NumArrayList.copyArray(tempArray, getInternalArray());
        setInternalArray(tempArray);
    }

    /**
     * Demonstration for the functionality of NumArrayList
     * @param args unused
     */
    public static void main(String[] args) {
        String indent = "    ";
        String newLine = "\n";

        System.out.println(newLine + "*** Demonstration for the NumArrayList class ***" + newLine);
    
        System.out.println("Methods:" + newLine +
                           indent + "NumArrayList(); // constructor for an empty list with 0 capacity" + newLine +
                           indent + "NumArrayList(capacity); // constructor for an empty list with set capacity" + newLine +
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
        
        System.out.println("Demonstration for NumArrayList(), size(), capacity(), add(value), and toString()");
        System.out.println("--- Code ---");
        System.out.println("NumArrayList list = new NumArrayList();" + newLine +
                           "System.out.println(list.size());" + newLine +
                           "System.out.println(list.capacity());" + newLine +
                           newLine +
                           "list.add(0.0);" + newLine +
                           "list.add(1.0);" + newLine +
                           "System.out.println(list.size());" + newLine +
                           "System.out.println(list.capacity());" + newLine +
                           "System.out.println(list.toString());" + newLine);
        System.out.println("-- Output --");

        NumArrayList list = new NumArrayList();
        System.out.println(list.size());
        System.out.println(list.capacity());
        
        list.add(0.0);
        list.add(1.0);
        System.out.println(list.size());
        System.out.println(list.capacity());
        System.out.println(list.toString());

        System.out.println(""); // new line

        System.out.println("Demonstration for NumArrayList(capacity), insert(index, value), remove(index), contains(value), lookup(index)");
        System.out.println("--- Code ---");
        System.out.println("NumArrayList list = new NumArrayList(4);" + newLine +
                           "System.out.println(list.size());" + newLine +
                           "System.out.println(list.capacity());" + newLine +
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

        list = new NumArrayList(4);
        System.out.println(list.size());
        System.out.println(list.capacity());

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
        System.out.println("NumArrayList list = new NumArrayList();" + newLine +
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
                           "NumArrayList listCopy = new NumArrayList();" + newLine +
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

        list = new NumArrayList();
        list.add(0.0);
        list.add(1.0);
        list.add(2.0);
        list.add(1.0);
        list.add(2.0);
        list.add(3.0);

        System.out.println(list.toString());

        // Copying the list
        NumArrayList listCopy = new NumArrayList();
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
        System.out.println("NumArrayList list = new NumArrayList();" + newLine +
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

        list = new NumArrayList();

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
        System.out.println("NumArrayList list1 = new NumArrayList();" + newLine +
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

        NumArrayList list1 = new NumArrayList();
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
