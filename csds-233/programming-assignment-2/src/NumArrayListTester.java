import org.junit.Assert;
import org.junit.Test;

public class NumArrayListTester {
    /**
     * Creates a NumArrayList with the specified double values
     * Uses the empty constructor and the add method
     */
    private static NumArrayList createArrayList(double... values) {
        NumArrayList list = new NumArrayList();
        for (double value : values)
            list.add(value);

        return list;
    }

    /**
     * Unit tests for the NumArrayList constructors
     */
    @Test
    public void testNumArrayList() {
        // constructor with no parameters
        NumArrayList list1 = new NumArrayList();
        Assert.assertTrue("list1 size should have been 0 but it was not", list1.size() == 0);
        Assert.assertTrue("list1 capacity should have been 0 but it was not", list1.capacity() == 0);

        // constructor with capacity of 0
        NumArrayList list2 = new NumArrayList(0);
        Assert.assertTrue("list2 size should have been 0 but it was not", list2.size() == 0);
        Assert.assertTrue("list2 capacity should have been 0 but it was not", list2.capacity() == 0);

        // constructor with capacity of 3
        NumArrayList list3 = new NumArrayList(3);
        Assert.assertTrue("list3 size should have been 0 but it was not", list3.size() == 0);
        Assert.assertTrue("list3 capacity should have been 3 but it was not", list3.capacity() == 3);
    }
    
    /**
     * Unit tests for the size method
     */
    @Test
    public void testSize() {
        // size 0
        NumArrayList list1 = new NumArrayList();
        Assert.assertTrue("list1 size should have been 0 but it was not", list1.size() == 0);

        // size 1
        list1.add(1);
        Assert.assertTrue("list1 size should have been 1 but it was not", list1.size() == 1);

        // size 3
        list1.add(1);
        list1.add(1);
        Assert.assertTrue("list1 size should have been 3 but it was not", list1.size() == 3);
    }

    /**
     * Unit tests for the capacity method
     */
    @Test
    public void testCapacity() {
        // capacity 0
        NumArrayList list1 = new NumArrayList();
        Assert.assertTrue("list1 capacity should have been 0 but it was not", list1.capacity() == 0);

        // capacity 1
        list1.add(1);
        Assert.assertTrue("list1 capacity should have been 1 but it was not", list1.capacity() == 1);

        // capacity 4
        list1.add(1);
        list1.add(1);
        Assert.assertTrue("list1 capacity should have been 4 but it was not", list1.capacity() == 4);
    }

    /**
     * Unit tests for the add method
     */
    @Test
    public void testAdd() {
        // 0 elements
        NumArrayList list1 = new NumArrayList();
        Assert.assertEquals("list1 was not empty when it should have been", "", list1.toString());
        Assert.assertTrue("list1 size should have been 0 but it was not", list1.size() == 0);
        Assert.assertTrue("list1 capacity should have been 0 but it was not", list1.capacity() == 0);

        // 1 element
        list1.add(1.0);
        Assert.assertEquals("list1 did not have the correct element", "1.0", list1.toString());
        Assert.assertTrue("list1 size should have been 1 but it was not", list1.size() == 1);
        Assert.assertTrue("list1 capacity should have been 1 but it was not", list1.capacity() == 1);

        // 3 elements
        list1.add(2.0);
        list1.add(3.0);
        Assert.assertEquals("list1 did not have the correct elements", "1.0 2.0 3.0", list1.toString());
        Assert.assertTrue("list1 size should have been 3 but it was not", list1.size() == 3);
        Assert.assertTrue("list1 capacity should have been 4 but it was not", list1.capacity() == 4);
    }

    /**
     * Unit tests for the insert method
     */
    @Test
    public void testInsert() {
        // test when the index is negative
        NumList list = new NumArrayList();
        try {
            list.insert(-1, 0.0);
            Assert.fail("The method should have thrown an exception");
        }
        catch (Exception e) {
            // the method succeeded
        }
        
        // test when the list is empty
        NumList list1 = new NumArrayList();
        list1.insert(0, 0.0);
        Assert.assertEquals("The method did not add the value to the list as intended", 0.0, list1.lookup(0), 0.0);

        // test when the new value displaces the one element in the list
        list1 = new NumArrayList();
        list1.insert(0, 1.0);
        list1.insert(0, 0.0);        
        Assert.assertEquals("The 1st element in the list should be 0.0", 0.0, list1.lookup(0), 0.0);
        Assert.assertEquals("The 2nd element in the list should be 1.0", 1.0, list1.lookup(1), 0.0);

        // test when the new value goes after the one element in a list (and expands the capacity)
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.insert(2, 1.0);
        Assert.assertEquals("The 2nd element in the list should be 1.0", 1.0, list1.lookup(1), 0.0);

        // test when the new value displaces multiple elements in a list
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(2.0);
        list1.add(3.0);
        list1.add(4.0);
        list1.insert(1, 1.0);
        Assert.assertEquals("The value was not inserted correctly", "0.0 1.0 2.0 3.0 4.0", list1.toString());

        // insertion in the middle
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        Assert.assertEquals("0.0 1.0", list1.toString());
        list1.insert(1, 2.0);
        Assert.assertEquals("0.0 2.0 1.0", list1.toString());
    }

    /**
     * Unit tests for the remove method
     */
    @Test
    public void testRemove() {
        // test on an empty list
        NumList list1 = new NumArrayList();
        list1.remove(0);
        Assert.assertTrue("The size of the list should be 0", list1.size() == 0);

        // test on a list with one element with an index of 0
        list1.add(0.0);
        list1.remove(0);
        Assert.assertTrue("The size of the list should be 0", list1.size() == 0);

        // test on a list with one element with an index of 1
        list1.add(0.0);
        list1.remove(1);
        Assert.assertTrue("The size of the list should be 1", list1.size() == 1);
        Assert.assertTrue("The 1st element in the list should be 0.0", list1.lookup(0) == 0.0);

        // test on a list with multiple elements with an index of 0
        list1 = new NumArrayList();
        list1.add(-1.0);
        list1.add(0.0);
        list1.add(1.0);
        list1.add(2.0);
        list1.remove(0);
        Assert.assertEquals("The value was not removed correctly", "0.0 1.0 2.0", list1.toString());

        // test on a list with multiple element with an index greater than the size
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.add(2.0);
        list1.add(3.0);
        list1.add(4.0);
        list1.remove(5);
        Assert.assertEquals("The value was not removed correctly", "0.0 1.0 2.0 3.0 4.0", list1.toString());
    }

    /**
     * Unit tests for the contains method
     */
    @Test
    public void testContains() {
        // test on an empty list
        NumArrayList list1 = new NumArrayList();
        Assert.assertFalse("The method should return false on an empty list", list1.contains(0.0));

        // test on a list with one value that is the correct value
        list1.add(0.0);
        Assert.assertTrue("The method should return true when the list contains the value", list1.contains(0.0));

        // test on a list with one value that is not the value in a parameter
        Assert.assertFalse("The method should return false when the list does not contain the value", list1.contains(1.0));

        // test on a list with multiple values with one match
        list1.add(1.0);
        list1.add(2.0);
        Assert.assertTrue("The method should return true when the list contains the value", list1.contains(1.0));

        // test on a list with multiple values with multiple matches
        list1 = NumArrayListTester.createArrayList(0.0, 0.0, 0.0);
        Assert.assertTrue("The method should return true when the list contains the value", list1.contains(0.0));

        // test on a list with multiple values with no matches
        Assert.assertFalse("The method should return false when the list does not contain the value", list1.contains(1.0));
    }

    /**
     * Unit tests for the lookup method
     */
    @Test
    public void testLookup() {
        // test with a negative index
        NumList list = new NumArrayList();
        try {
            list.lookup(-1);
            Assert.fail("The method should have thrown an exception");
        }
        catch (Exception e) {

        }

        // test on a list with no capacity
        NumArrayList list1 = new NumArrayList();
        try {
            list1.lookup(0);
            Assert.fail("The method should have thrown an exception but it did not");
        }
        catch (IndexOutOfBoundsException e) {
        }
        catch (Exception e) {
            Assert.fail("The method threw the wrong exception: " + e.toString());
        }

        // test on a list with capacity but size of 0 (empty)
        list1 = new NumArrayList(1);
        try {
            list1.lookup(0);
            Assert.fail("The method should have thrown an exception but it did not");
        }
        catch (IndexOutOfBoundsException e) {
        }
        catch (Exception e) {
            Assert.fail("The method threw the wrong exception: " + e.toString());
        }

        // tests on a list with values
        list1 = NumArrayListTester.createArrayList(0.0, 1.0, 2.0, 3.0, 4.0);
        try {
            String message = "The method returned a value from the wrong index of the list";
            Assert.assertTrue(message, list1.lookup(0) == 0.0);
            Assert.assertTrue(message, list1.lookup(1) == 1.0);
            Assert.assertTrue(message, list1.lookup(2) == 2.0);
            Assert.assertTrue(message, list1.lookup(3) == 3.0);
            Assert.assertTrue(message, list1.lookup(4) == 4.0);
        }
        catch (Exception e) {
            Assert.fail("The method threw an exception when it should not have: " + e.toString());
        }

        // test where the index is beyond the size of the list
        try {
            list1.lookup(5);
            Assert.fail("The method should have thrown an exception but it did not");
        }
        catch (IndexOutOfBoundsException e) {
        }
        catch (Exception e) {
            Assert.fail("The method threw the wrong exception: " + e.toString());
        }
    }

    /**
     * Unit tests for the remove duplicates method
     */
    @Test
    public void testRemoveDuplicates() {
        // empty list
        NumArrayList list1 = new NumArrayList();
        list1.removeDuplicates();
        Assert.assertTrue("The capacity of the list should be 0", list1.capacity() == 0);
        Assert.assertTrue("The size of the list should be 0", list1.size() == 0);

        // list with one element
        list1.add(0.0);
        list1.removeDuplicates();
        Assert.assertTrue("The capacity of the list should be 1", list1.capacity() == 1);
        Assert.assertTrue("The size of the list should be 1", list1.size() == 1);
        Assert.assertTrue("The 1st element in the list should be 0.0", list1.lookup(0) == 0.0);

        // list with multiple elements but no duplicates
        list1 = NumArrayListTester.createArrayList(0.0, 1.0, 2.0, 3.0, 4.0);
        list1.removeDuplicates();
        Assert.assertEquals("The contents of the array were altered when they shouldn't have been",
                            "0.0 1.0 2.0 3.0 4.0", list1.toString());

        /* a list with multiple duplicates at various locations */
        String message = "The method did not remove the duplicates correctly";
        // a list with adjacent duplicates
        list1 = NumArrayListTester.createArrayList(0.0, 0.0, 1.0, 1.0, 2.0);
        list1.removeDuplicates();
        Assert.assertEquals(message,
                            "0.0 1.0 2.0", list1.toString());

        // a list with duplicates at the ends
        list1 = NumArrayListTester.createArrayList(0.0, 1.0, 2.0, 1.0, 0.0);
        list1.removeDuplicates();
        Assert.assertEquals(message,
                            "0.0 1.0 2.0", list1.toString());

        // a list with elements that are all the same value
        list1 = NumArrayListTester.createArrayList(0.0, 0.0, 0.0, 0.0, 0.0);
        list1.removeDuplicates();
        Assert.assertEquals(message,
                            "0.0", list1.toString());
    }

    /**
     * Unit tests for the isSorted method
     */
    @Test
    public void testIsSorted() {
        // empty list
        NumList list1 = new NumArrayList();
        Assert.assertTrue("An empty list should be sorted", list1.isSorted());

        // list with one element
        list1.add(0.0);
        Assert.assertTrue("A list with one element should be sorted", list1.isSorted());

        // list with two identical elements
        list1.add(0.0);
        Assert.assertTrue("A list with two identical elements should be sorted", list1.isSorted());

        // list with two elements in ascending order
        list1.remove(1);
        list1.add(1.0);
        Assert.assertTrue("A list with two values in ascending order should be considered sorted",
                          list1.isSorted());

        // list with two elements in descending order (this is also a check for reverse)
        list1.reverse();
        Assert.assertFalse("A list with two values in descending order should not be considered sorted",
                           list1.isSorted());

        // list with multiple elements in ascending order
        list1.remove(1);
        list1.remove(0);
        list1.add(0.0);
        list1.add(1.0);
        list1.add(1.0);
        list1.add(2.0);
        Assert.assertTrue("A list with multiple values in ascending order should be considered sorted",
                          list1.isSorted());

        // list with multiple elements that are not sorted
        list1.reverse();
        Assert.assertFalse("A list with multiple values that are not in strictly ascending order should not be considered sorted",
                           list1.isSorted());

        // check a list that becomes sorted (& one that remains unsorted) after removing an element
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(2.0);
        list1.add(1.0);
        list1.remove(1);
        Assert.assertTrue("A list that becomes sorted after removing an item should be considered sorted",
                          list1.isSorted());
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(2.0);
        list1.add(2.0);
        list1.add(1.0);
        Assert.assertFalse("A list the remains unsorted after removing an item should still be considered unsorted",
                           list1.isSorted());

        // check a list that becomes unsorted (& one that remains sorted) after adding a value
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.add(0.0);
        Assert.assertFalse("A list that becomes unsorted after adding a value should be considered unsorted",
                           list1.isSorted());
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.add(2.0);
        Assert.assertTrue("A list that remains sorted after adding a value should still be considered sorted",
                          list1.isSorted());

        // check a list that becomes unsorted (& one that remains sorted) after inserting a value
        // front
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.insert(0, 2.0);
        Assert.assertFalse(list1.isSorted());
        list1 = new NumArrayList();
        list1.add(1.0);
        list1.add(2.0);
        list1.insert(0, 0.0);
        Assert.assertTrue(list1.isSorted());
        // middle
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.insert(1, 2.0);
        Assert.assertFalse("A list that becomes unsorted after inserting a value should be considered unsorted",
                           list1.isSorted());
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(2.0);
        list1.insert(1, 1.0);
        Assert.assertTrue("A list that remains sorted after inserting a value should still be considered sorted",
                          list1.isSorted());
        // back
        list1 = new NumArrayList();
        list1.add(1.0);
        list1.add(2.0);
        list1.insert(2, 0.0);
        Assert.assertFalse(list1.isSorted());
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.insert(2, 2.0);
        Assert.assertTrue(list1.isSorted());

        // check a list that remains sorted after reversing the order
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(0.0);
        list1.add(0.0);
        list1.reverse();
        Assert.assertTrue("A list that remains sorted after reversing the order should still be considered sorted",
                          list1.isSorted());

        // check a list the becomes sorted (& and one that remains unsorted) after removing duplicates
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.add(0.0);
        list1.removeDuplicates();
        Assert.assertTrue("A list that becomes sorted after removing duplicates should be considered sorted",
                          list1.isSorted());
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(2.0);
        list1.add(0.0);
        list1.add(1.0);
        list1.removeDuplicates();
        Assert.assertFalse("A list that remains unsorted after removing duplicates should still be considered unsorted",
                           list1.isSorted());
    }

    /**
     * Unit tests for the reverse method
     */
    @Test
    public void testReverse() {
        // empty list
        NumList list1 = new NumArrayList();
        NumList list2 = new NumArrayList();
        Assert.assertTrue("Two empty lists should be equal", list1.equals(list2));
        list1.reverse();
        Assert.assertTrue("Two empty lists should be equal", list1.equals(list2));

        // list with one element
        list1.add(1.0);
        list2.add(1.0);
        list1.reverse();
        Assert.assertTrue("Two lists with the same element should remain equal even after one is reversed", list1.equals(list2));

        // list with two elements that are the same
        list1.add(1.0);
        list2.add(1.0);
        Assert.assertTrue("Two lists of repeated elements should remain equal even after one is reversed", list1.equals(list2));

        // list with two elements that are different
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(1.0);
        list1.reverse();
        Assert.assertEquals("The list should have changed the order of the two elements", "1.0 0.0", list1.toString());

        // list with multiple elements
        list1.add(2.0);
        list1.reverse();
        Assert.assertEquals("The list should have reversed the whole list", "2.0 0.0 1.0", list1.toString());

        // repeated calls to reverse
        list1.reverse();
        list1.reverse();
        Assert.assertEquals("Two consecutive calls to reverse should revert to the original list", "2.0 0.0 1.0", list1.toString());
    }

    /**
     * Unit tests for the toString method
     */
    @Test
    public void testToString() {
        // test on an empty array
        NumArrayList list1 = new NumArrayList();
        Assert.assertEquals("The method did not return an empty string", "", list1.toString());

        // test on an array with one element
        list1.add(0.0);
        Assert.assertEquals("The method did not return the correct string", "0.0", list1.toString());

        // test on an array with multiple elements
        list1 = NumArrayListTester.createArrayList(0.0, 1.0, 2.0, 3.0, 4.0);
        Assert.assertEquals("The method did not return the correct string", "0.0 1.0 2.0 3.0 4.0", list1.toString());
    }
}