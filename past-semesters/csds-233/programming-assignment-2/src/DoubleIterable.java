/**
 * An interface to indicate that a list can be iterated over using an iterator
 * @author Joshua Shew
 */
public interface DoubleIterable {
    /**
     * Returns an iterator for the list that begins at the front of the list
     * @return a DoubleIterator for the list the method is called on
     */
    public abstract DoubleIterator iterator();
}
