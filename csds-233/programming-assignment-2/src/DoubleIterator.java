/**
 * An iterator for a list that contains doubles
 * @author Joshua Shew
 */
public interface DoubleIterator {
    /**
     * Checks whether there is another value ahead of the iterator in the list
     * @return true if there is a value, false otherwise
     */
    public abstract boolean hasNext();

    /**
     * Moves the iterator forward one value and returns that value
     * @return the value at iterator's current index
     */
    public abstract double next();

    /**
     * Retrieves the value the iterator is current at without moving it forward
     * @return the value at the iterator's current index
     */
    public abstract double peek();
}
