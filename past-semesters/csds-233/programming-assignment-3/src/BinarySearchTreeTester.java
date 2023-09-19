import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tester class for BinarySearchTree
 * @author Joshua Shew
 */
public class BinarySearchTreeTester {
    /**
     * Unit tests for insert method
     */
    @Test
    public void testInsert() {
        String insertError = "The value was not inserted correctly";

        // empty tree
        BinarySearchTree<Integer, Integer> tree1 = newTree();
        insert(tree1, 1);
        Assert.assertEquals(insertError, "1", preOrderString(tree1));

        // tree with only the root (left and right side)
        tree1 = newTree();
        insert(tree1, 1);
        insert(tree1, 0);
        Assert.assertEquals(insertError, "1 (0, )", preOrderString(tree1));
        tree1 = newTree();
        insert(tree1, 1);
        insert(tree1, 2);
        Assert.assertEquals(insertError, "1 (, 2)", preOrderString(tree1));

        // new value goes on far left
        tree1 = newTree();
        Assert.assertEquals(insertError, "", preOrderString(tree1));
        insert(tree1, 5, 2, 3, 7, 6, 8);
        Assert.assertEquals(insertError, "5 (2 (, 3), 7 (6, 8))", preOrderString(tree1));
        insert(tree1, 0);
        Assert.assertEquals(insertError, "5 (2 (0, 3), 7 (6, 8))", preOrderString(tree1));

        // new value goes on far right
        insert(tree1, 9);
        Assert.assertEquals(insertError, "5 (2 (0, 3), 7 (6, 8 (, 9)))", preOrderString(tree1));

        // new value goes in the middle
        insert(tree1, 4);
        Assert.assertEquals(insertError, "5 (2 (0, 3 (, 4)), 7 (6, 8 (, 9)))", preOrderString(tree1));

        // new value is a repeated value
        tree1 = newTree();
        insert(tree1, 2, 1, 3);
        Assert.assertEquals(insertError, "2 (1, 3)", preOrderString(tree1));
        insert(tree1, 3);
        Assert.assertEquals(insertError, "2 (1, 3 (, 3))", preOrderString(tree1));
        insert(tree1, 1);
        Assert.assertEquals(insertError, "2 (1 (, 1), 3 (, 3))", preOrderString(tree1));
        insert(tree1, 1);
        Assert.assertEquals(insertError, "2 (1 (, 1 (, 1)), 3 (, 3))", preOrderString(tree1));
    }

    /**
     * Unit tests for search method
     */
    @Test
    public void testSearch() {
        String exceptionExpected = "The method should have thrown an exception but it did not";
        String wrongException = "The method threw the wrong exception: ";
        String badException = "The method should not have thrown an exception: ";
        String wrongValue = "The method returned the wrong value";

        // empty tree
        BinarySearchTree<Integer, Integer> tree1 = newTree();
        try {
            tree1.search(1);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // root node (match and not a match)
        insert(tree1, 2);
        try {
            Assert.assertEquals(wrongValue, (Integer)2, tree1.search(2));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }
        try {
            tree1.search(1);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // search for the smallest element (match and not a match)
        tree1 = newTree();
        insert(tree1, 5, 2, 7, 1, 3, 6, 8);
        try {
            Assert.assertEquals(wrongValue, (Integer)1, tree1.search(1));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }
        try {
            tree1.search(0);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // search for the largest element (match and not a match)
        try {
            Assert.assertEquals(wrongValue, (Integer)8, tree1.search(8));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }
        try {
            tree1.search(9);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // search for a middle element (match and not a match)
        try {
            Assert.assertEquals(wrongValue, (Integer)6, tree1.search(6));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }
        try {
            tree1.search(4);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // search for a duplicate element
        insert(tree1, 4, 4);
        try {
            Assert.assertEquals(wrongValue, (Integer)4, tree1.search(4));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }
    }

    /**
     * Unit tests for delete method
     */
    @Test
    public void testDelete() {
        String badDeletion = "The value should not have been removed";
        String wrongDeletion = "The value was not removed correctly";

        // empty tree
        BinarySearchTree<Integer, Integer> tree1 = newTree();
        tree1.delete(1); // does nothing and that is correct

        // root only (match and no match)
        insert(tree1, 5);
        tree1.delete(1);
        Assert.assertEquals(badDeletion, "5", preOrderString(tree1));
        tree1.delete(5);
        Assert.assertEquals(wrongDeletion, "", preOrderString(tree1));

        // large tree but no match
        tree1 = newTree();
        insert(tree1, 5, 2, 1, 3, 7, 6, 8);
        tree1.delete(4);
        Assert.assertEquals(badDeletion, "5 (2 (1, 3), 7 (6, 8))", preOrderString(tree1));

        // smallest value (yes children, no children)
        tree1 = newTree();
        insert(tree1, 5, 3, 4, 1, 2, 7, 6, 8);
        Assert.assertEquals("Tree was not constructed properly", "5 (3 (1 (, 2), 4), 7 (6, 8))", preOrderString(tree1));
        tree1.delete(1);
        Assert.assertEquals(wrongDeletion, "5 (3 (2, 4), 7 (6, 8))", preOrderString(tree1));
        tree1.delete(2);
        Assert.assertEquals(wrongDeletion, "5 (3 (, 4), 7 (6, 8))", preOrderString(tree1));

        // largest value (yes children, no children)
        tree1 = newTree();
        insert(tree1, 5, 3, 1, 4, 7, 6, 9, 8);
        Assert.assertEquals("Tree was not constructed properly", "5 (3 (1, 4), 7 (6, 9 (8, )))", preOrderString(tree1));
        tree1.delete(9);
        Assert.assertEquals(wrongDeletion, "5 (3 (1, 4), 7 (6, 8))", preOrderString(tree1));
        tree1.delete(8);
        Assert.assertEquals(wrongDeletion, "5 (3 (1, 4), 7 (6, ))", preOrderString(tree1));

        // middle value (left child, right child, both children, both children have children too)
        tree1 = newTree();
        insert(tree1, 8, 5, 12, 2, 6, 10, 15, 1, 4, 7, 9, 11, 13, 16, 3, 14);
        Assert.assertEquals("Tree was not constructed properly",
                            "8 (5 (2 (1, 4 (3, )), 6 (, 7)), 12 (10 (9, 11), 15 (13 (, 14), 16)))",
                            preOrderString(tree1));
        tree1.delete(4);
        Assert.assertEquals(wrongDeletion,
                            "8 (5 (2 (1, 3), 6 (, 7)), 12 (10 (9, 11), 15 (13 (, 14), 16)))",
                            preOrderString(tree1));
        tree1.delete(6);
        Assert.assertEquals(wrongDeletion,
                            "8 (5 (2 (1, 3), 7), 12 (10 (9, 11), 15 (13 (, 14), 16)))",
                            preOrderString(tree1));
        tree1.delete(2);
        Assert.assertEquals(wrongDeletion,
                            "8 (5 (3 (1, ), 7), 12 (10 (9, 11), 15 (13 (, 14), 16)))",
                            preOrderString(tree1));
        tree1.delete(12);
        Assert.assertEquals(wrongDeletion,
                            "8 (5 (3 (1, ), 7), 13 (10 (9, 11), 15 (14, 16)))",
                            preOrderString(tree1));

        // remove root in large tree
        tree1.delete(8);
        Assert.assertEquals(wrongDeletion,
                            "9 (5 (3 (1, ), 7), 13 (10 (, 11), 15 (14, 16)))",
                            preOrderString(tree1));

        // remove a duplicate value
        tree1 = newTree();
        insert(tree1, 5, 1, 1, 2, 6);
        Assert.assertEquals("Tree was not constructed properly", "5 (1 (, 1 (, 2)), 6)", preOrderString(tree1));
        tree1.delete(1);
        Assert.assertEquals("Tree was not constructed properly", "5 (1 (, 2), 6)", preOrderString(tree1));
        tree1.delete(1);
        Assert.assertEquals("Tree was not constructed properly", "5 (2, 6)", preOrderString(tree1));
    }

    /**
     * Unit tests for inorderRec method
     */
    @Test
    public void testInorderRec() {
        String badList = "The method did not return the expected list";

        // empty tree
        BinarySearchTree<Integer, Integer> tree1 = newTree();
        Assert.assertEquals(badList, "[]", tree1.inorderRec().toString());

        // root only
        insert(tree1, 5);
        Assert.assertEquals(badList, "[5]", tree1.inorderRec().toString());

        // large tree
        insert(tree1, 2, 1, 3, 7, 6, 8);
        Assert.assertEquals(badList, "[1, 2, 3, 5, 6, 7, 8]", tree1.inorderRec().toString());
    }

    /**
     * Unit tests for kthSmallest method
     */
    @Test
    public void kthSmallest() throws NoSuchElementException {
        String exceptionExpected = "The method should have thrown an exception but it did not";
        String wrongException = "The method threw the wrong exception: ";
        String badException = "The method should not have thrown an exception: ";
        String wrongValue = "The method returned the wrong value";
        
        // empty tree
        BinarySearchTree<Integer, Integer> tree1 = newTree();
        try {
            tree1.kthSmallest(1);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // negative or zero input
        try {
            tree1.kthSmallest(0);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }
        try {
            tree1.kthSmallest(-1);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // root only (no exception and exception)
        insert(tree1, 5);
        try {
            Assert.assertEquals(wrongValue, (Integer)5, tree1.kthSmallest(1));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }
        try {
            tree1.kthSmallest(2);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }

        // smallest
        insert(tree1, 3, 2, 4, 7, 6, 8);
        try {
            Assert.assertEquals(wrongValue, (Integer)2, tree1.kthSmallest(1));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }

        // middle
        try {
            Assert.assertEquals(wrongValue, (Integer)4, tree1.kthSmallest(3));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }

        // largest
        try {
            Assert.assertEquals(wrongValue, (Integer)8, tree1.kthSmallest(7));
        }
        catch (Exception e) {
            Assert.fail(badException + e.toString());
        }

        // large tree (exception)
        try {
            tree1.kthSmallest(9);
            Assert.fail(exceptionExpected);
        }
        catch (NoSuchElementException e) {

        }
        catch (Exception e) {
            Assert.fail(wrongException + e.toString());
        }
    }

    /**
     * Helper method to use a private method in BinarySearchTree
     * @param tree the tree to call the method on
     * @return the string outputted by the method
     */
    public static String preOrderString(BinarySearchTree<?, ?> tree) {
        Class<?> treeClass = tree.getClass();
        try {
            Method method = treeClass.getDeclaredMethod("preOrderToString");
            method.setAccessible(true);
            return (String)method.invoke(tree, (Object[])null);
        }
        catch (Exception e) {
            throw new NullPointerException("Could not call the method via reflection");
        }
    }

    /**
     * Helper method to repeatedly call the insert method on a tree
     * @param tree the tree to call insert on
     * @param values the value to use as both key and value for the insert invocations
     * @return the tree
     */
    public static BinarySearchTree<Integer, Integer> insert(BinarySearchTree<Integer, Integer> tree, int... values) {
        for (int i : values)
            tree.insert(i, i);

        return tree;
    }

    /**
     * Helper method to create a new tree that stores Integers for both keys and values
     * @return the tree
     */
    public static BinarySearchTree<Integer, Integer> newTree() {
        return new BinarySearchTree<Integer, Integer>();
    }
}
