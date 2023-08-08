/**
 * An interface for a list that contains doubles
 * @author Joshua Shew
 */
public interface NumList extends DoubleIterable {
    /**
     * Getter method for the size of the list
     * The size is the number of elements currently in the list
     * @return the size of the list
     */
    public abstract int size();

    /**
     * Getter method for the capacity of the list
     * The capacity is the number of elements the list can hold without resizing
     * @return the capacity of the list
     */
    public abstract int capacity();

    /**
     * Adds a number of the end of the list
     * The method expands the capacity if needed
     * @param value the number to be added to the end of the list
     */
    public abstract void add(double value);

    /**
     * Adds a number at a specified position of the list
     * All numbers after (and the number at) the specified position are shifted down the list
     * If the list has i or fewer numbers, then the value is added in the same fashion as the add method
     * @param i the index to insert the new value at
     * @param value the value to add to the list
     */
    public abstract void insert(int i, double value);

    /**
     * Removes the element at the specified position of the list
     * All elements after the specified position are shifted up the list
     * The size of the list is shortened by this method if there is a number at the specified position
     * @param i the index where the value should be removed
     */
    public abstract void remove(int i);

    /**
     * Checks whether the list contains the input value
     * @param value the value is what the method is confirming is in the list
     * @return true if the value is in the list, false otherwise
     */
    public abstract boolean contains(double value);

    /**
     * Returns the i-th element of the list
     * @param i the index of the list to get the value from
     * @return the value stored at the specified index
     * @throws IndexOutOfBoundsException when the input index is greater than or equal to the size of the list
     */
    public abstract double lookup(int i) throws IndexOutOfBoundsException;

    /**
     * Checks whether this list is equal to the input list
     * Two lists are equal if they have all the same numbers in the same order
     * O(N)
     * @param otherList the other list to compare this list to
     * @return true if the two are equal, false otherwise
     */
    public default boolean equals(NumList otherList) {
        /* If the sizes are different, they cannot be equal */
        if (this.size() != otherList.size()) {
            return false;
        }
        /* Linear search for any differences */
        else {
            DoubleIterator i1 = this.iterator();
            DoubleIterator i2 = otherList.iterator();

            while (i1.hasNext()) {
                if (i1.next() != i2.next()) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * Removes duplicates in this list while preserving the current order of the numbers
     */
    public abstract void removeDuplicates();

    /**
     * Returns whether the list is currently sorted in increasing order or not
     * @return true if the list is sorted, false otherwise
     */
    public abstract boolean isSorted();

    /**
     * Reverses the order of the elements in the list
     */
    public abstract void reverse();

    /**
     * Creates a new list which has all elements in the input lists without any duplicate elements
     * If both lists are sorted, then the resulting list is also sorted
     * O(N) for sorted lists
     * O(N^2) for unsorted lists
     * @param list1 the first list
     * @param list2 the second list
     * @return a NumList (of true type NumLinkedList) with all the elements of the input lists without any duplicate elements
     */
    public static NumList union(NumList list1, NumList list2) {
        NumLinkedList newList = new NumLinkedList();

        DoubleIterator i1 = list1.iterator();
        DoubleIterator i2 = list2.iterator();

        if (list1.isSorted() && list2.isSorted()) {
            /* lastAdded keeps track of the last value added to the new list to prevent duplicate values */
            /* This logic sets the value to something less than the first values in the two input lists */
            double lastAdded = i1.hasNext() && i2.hasNext() ? 
                               Math.min(i1.peek(), i2.peek()) - 1 :
                               0;

            /* Adds the values from each list in ascending order while skipping duplicates */
            while (i1.hasNext() && i2.hasNext()) {
                if (i1.peek() < i2.peek()) {
                    /* Checks to make sure a duplicate value is not added */
                    if (i1.peek() != lastAdded) {
                        newList.add(i1.peek());
                        lastAdded = i1.peek();
                    }

                    i1.next();
                }
                else {
                    if (i2.peek() != lastAdded) {
                        newList.add(i2.peek());
                        lastAdded = i2.peek();
                    }

                    i2.next();
                }
            }
            /* Add remaining values from list1 */
            while (i1.hasNext()) {
                if (i1.peek() != lastAdded) {
                    newList.add(i1.peek());
                    lastAdded = i1.peek();
                }

                i1.next();
            }
            /* Add remaining values from list2 */
            while (i2.hasNext()) {
                if (i2.peek() != lastAdded) {
                    newList.add(i2.peek());
                    lastAdded = i2.peek();
                }

                i2.next();
            }
        }
        /* When one or both of the input lists is not sorted */
        else {
            /* Add all unique values from list1 */
            while (i1.hasNext()) {
                if (!newList.contains(i1.peek())) {
                    newList.add(i1.peek());
                }

                i1.next();
            }
            /* Add all unique values from list2 */
            while (i2.hasNext()) {
                if (!newList.contains(i2.peek())) {
                    newList.add(i2.peek());
                }

                i2.next();
            }
        }

        return newList;
    }

    /**
     * Provides a String representation of this list
     * For an empty list, an empty String is returned
     * Numbers are separated by a space and no other characters are included
     * @return a String representation of the list
     */
    public abstract String toString();
}