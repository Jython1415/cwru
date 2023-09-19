import java.lang.IndexOutOfBoundsException;

/**
 * 
 * @author Joshua Shew
 */
public interface NumList {
    /**
     * Getter method for the size of the list
     * The size is the number of numbers currently in the list
     * @return the size of the list
     */
    public abstract int size();

    /**
     * Getter method for the capacity of the list
     * The capacity is the number of numbers the list can hold without resizing
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
     * Removes the number at the specified position of the list
     * All numbers after the specified position are shifted up the list
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
     * @param otherList the other list to compare this list to
     * @return true if the two are equal, false otherwise
     */
    public abstract boolean equals(NumList otherList);

    /**
     * Removes duplicates in this list while preserving the current order of the numbers
     */
    public abstract void removeDuplicates();

    /**
     * Provides a String representation of this list
     * For an empty list, an empty String is returned
     * Numbers are separated by a space and no other characters are included
     * @return a String representation of the list
     */
    public abstract String toString();
}