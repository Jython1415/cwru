import org.junit.Assert;
import org.junit.Test;

public class NumListTester {
    /**
     * Unit tests for the equals method
     */
    @Test
    public void testEquals() {
        // two empty lists should be equal (NumLinkedList (LL) & LL, NumArrayList (AL) & LL, and AL & AL)
        // the first test also checks communicativeness
        NumList list1 = new NumLinkedList();
        NumList list2 = new NumLinkedList();
        Assert.assertTrue("The method should have returned true for the two lists, but it did not", list1.equals(list2));
        Assert.assertTrue("The method should have returned true for the two lists, but it did not", list2.equals(list1));
        list1 = new NumArrayList();
        list2 = new NumLinkedList();
        Assert.assertTrue(list1.equals(list2));
        Assert.assertTrue(list2.equals(list1));
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        Assert.assertTrue(list1.equals(list2));
        Assert.assertTrue(list2.equals(list1));
        // a list should also equal itself
        list1 = new NumLinkedList();
        Assert.assertTrue("The method should have returned true for the two lists, but it did not", list1.equals(list1));
        list1 = new NumArrayList();
        Assert.assertTrue(list1.equals(list1));

        // two lists with the same value should be equal (LL & LL, AL & LL, AL & AL -- same thing for all tests)
        list1 = new NumLinkedList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list2.add(1.0);
        Assert.assertTrue("The method should have returned true for the two lists, but it did not", list1.equals(list2));
        list1 = new NumArrayList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list2.add(1.0);
        Assert.assertTrue(list1.equals(list2));
        Assert.assertTrue(list2.equals(list1));
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        list1.add(1.0);
        list2.add(1.0);
        Assert.assertTrue(list1.equals(list2));

        // two lists with the same values should be equal
        list1 = new NumLinkedList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list1.add(2.0);
        list2.add(1.0);
        list2.add(2.0);
        Assert.assertTrue("The method should have returned true for the two lists, but it did not", list1.equals(list2));
        list1 = new NumArrayList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list1.add(2.0);
        list2.add(1.0);
        list2.add(2.0);
        Assert.assertTrue(list1.equals(list2));
        Assert.assertTrue(list2.equals(list1));
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        list1.add(1.0);
        list1.add(2.0);
        list2.add(1.0);
        list2.add(2.0);
        Assert.assertTrue(list1.equals(list2));

        // two lists with values that are not in the exact same order should not be equal
        list1 = new NumLinkedList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list1.add(2.0);
        list1.add(2.0);
        list2.add(1.0);
        list2.add(2.0);
        list2.add(3.0);
        Assert.assertFalse("The method should have returned false for the two lists, but it did not", list1.equals(list2));
        list1 = new NumLinkedList();
        list2 = new NumArrayList();
        list1.add(1.0);
        list1.add(2.0);
        list1.add(2.0);
        list2.add(1.0);
        list2.add(2.0);
        list2.add(3.0);
        Assert.assertFalse(list1.equals(list2));
        Assert.assertFalse(list2.equals(list1));
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        list1.add(1.0);
        list1.add(2.0);
        list1.add(2.0);
        list2.add(1.0);
        list2.add(2.0);
        list2.add(3.0);
        Assert.assertFalse(list1.equals(list2));
    }

    /**
     * Unit tests for the union method
     */
    @Test
    public void testUnion() {
        // two empty lists (NumLinkedList (LL) & LL, NumArrayList (AL) & LL, and AL & AL)
        NumList list1 = new NumLinkedList();
        NumList list2 = new NumLinkedList();
        Assert.assertEquals("The union of two empty lists should be an empty list", "", NumList.union(list1, list2).toString());
        list1 = new NumLinkedList();
        list2 = new NumArrayList();
        Assert.assertEquals("The union of two empty lists should be an empty list", "", NumList.union(list1, list2).toString());
        Assert.assertEquals("The union of two empty lists should be an empty list", "", NumList.union(list2, list1).toString());
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        Assert.assertEquals("The union of two empty lists should be an empty list", "", NumList.union(list1, list2).toString());

        // one empty list and one list with one or multiple (unsorted) elements (LL & LL, AL & LL, AL & AL -- same thing for all tests)
        list1 = new NumLinkedList();
        list2 = new NumLinkedList();
        list1.add(0.0);
        list1.add(2.0);
        list1.add(1.0);
        Assert.assertEquals("The union of an empty list with an non empty list should return the non empty list with duplicates removed",
                            "0.0 2.0 1.0", NumList.union(list1, list2).toString());
        Assert.assertEquals("The order of the parameters should not affect the result in this case",
                            "0.0 2.0 1.0", NumList.union(list1, list2).toString());
        list1 = new NumArrayList();
        list1 = new NumLinkedList();
        list1.add(0.0);
        list1.add(2.0);
        list1.add(1.0);
        Assert.assertEquals("0.0 2.0 1.0", NumList.union(list1, list2).toString());
        Assert.assertEquals("0.0 2.0 1.0", NumList.union(list1, list2).toString());
        list1 = new NumArrayList();
        list1 = new NumArrayList();
        list1.add(0.0);
        list1.add(2.0);
        list1.add(1.0);
        Assert.assertEquals("0.0 2.0 1.0", NumList.union(list1, list2).toString());
        Assert.assertEquals("0.0 2.0 1.0", NumList.union(list1, list2).toString());

        // two unsorted lists with multiple elements (no duplicates)
        list1 = new NumLinkedList();
        list2 = new NumLinkedList();
        list1.add(6.0);
        list1.add(4.0);
        list1.add(2.0);
        list2.add(5.0);
        list2.add(3.0);
        list2.add(1.0);
        NumList list3 = NumList.union(list1, list2);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));
        Assert.assertTrue(list3.size() == 6);
        list1 = new NumArrayList();
        list2 = new NumLinkedList();
        list1.add(6.0);
        list1.add(4.0);
        list1.add(2.0);
        list2.add(5.0);
        list2.add(3.0);
        list2.add(1.0);
        list3 = NumList.union(list1, list2);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));
        Assert.assertTrue(list3.size() == 6);
        list3 = NumList.union(list2, list1);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));
        Assert.assertTrue(list3.size() == 6);
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        list1.add(6.0);
        list1.add(4.0);
        list1.add(2.0);
        list2.add(5.0);
        list2.add(3.0);
        list2.add(1.0);
        list3 = NumList.union(list1, list2);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));
        Assert.assertTrue(list3.size() == 6);

        // two unsorted lists with multiple elements (with duplicates)
        list1 = new NumLinkedList();
        list1 = new NumLinkedList();
        list1.add(6.0);
        list1.add(4.0);
        list1.add(2.0);
        list1.add(5.0);
        list1.add(5.0);
        list2.add(5.0);
        list2.add(3.0);
        list2.add(1.0);
        list2.add(5.0);
        list2.add(5.0);
        list3 = NumList.union(list1, list2);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));
        Assert.assertTrue(list3.size() == 6);
        list1 = new NumArrayList();
        list1 = new NumLinkedList();
        list1.add(6.0);
        list1.add(4.0);
        list1.add(2.0);
        list1.add(5.0);
        list1.add(5.0);
        list2.add(5.0);
        list2.add(3.0);
        list2.add(1.0);
        list2.add(5.0);
        list2.add(5.0);
        list3 = NumList.union(list1, list2);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));
        Assert.assertTrue(list3.size() == 6);
        list3 = NumList.union(list2, list1);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));
        Assert.assertTrue(list3.size() == 6);
        list1 = new NumArrayList();
        list1 = new NumArrayList();
        list1.add(6.0);
        list1.add(4.0);
        list1.add(2.0);
        list1.add(5.0);
        list1.add(5.0);
        list2.add(5.0);
        list2.add(3.0);
        list2.add(1.0);
        list2.add(5.0);
        list2.add(5.0);
        list3 = NumList.union(list1, list2);
        Assert.assertTrue(list3.contains(1.0));
        Assert.assertTrue(list3.contains(2.0));
        Assert.assertTrue(list3.contains(3.0));
        Assert.assertTrue(list3.contains(4.0));
        Assert.assertTrue(list3.contains(5.0));
        Assert.assertTrue(list3.contains(6.0));

        // two sorted lists (no duplicates)
        list1 = new NumLinkedList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list1.add(3.0);
        list1.add(5.0);
        list2.add(0.0);
        list2.add(2.0);
        list2.add(4.0);
        Assert.assertEquals("The result should be a sorted union of the two lists",
                            "0.0 1.0 2.0 3.0 4.0 5.0", NumList.union(list1, list2).toString());
        list1 = new NumArrayList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list1.add(3.0);
        list1.add(5.0);
        list2.add(0.0);
        list2.add(2.0);
        list2.add(4.0);
        Assert.assertEquals("0.0 1.0 2.0 3.0 4.0 5.0", NumList.union(list1, list2).toString());
        Assert.assertEquals("0.0 1.0 2.0 3.0 4.0 5.0", NumList.union(list2, list1).toString());
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        list1.add(1.0);
        list1.add(3.0);
        list1.add(5.0);
        list2.add(0.0);
        list2.add(2.0);
        list2.add(4.0);
        Assert.assertEquals("0.0 1.0 2.0 3.0 4.0 5.0", NumList.union(list1, list2).toString());

        // two sorted lists (with duplicates)
        list1 = new NumLinkedList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list1.add(3.0);
        list1.add(5.0);
        list1.add(6.0);
        list1.add(7.0);
        list1.add(8.0);
        list2.add(0.0);
        list2.add(2.0);
        list2.add(4.0);
        list2.add(6.0);
        list2.add(6.0);
        list2.add(9.0);
        Assert.assertEquals("The result should be a sorted union of the two lists with no duplicates",
                            "0.0 1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0", NumList.union(list1, list2).toString());
        list1 = new NumArrayList();
        list2 = new NumLinkedList();
        list1.add(1.0);
        list1.add(3.0);
        list1.add(5.0);
        list1.add(6.0);
        list1.add(7.0);
        list1.add(8.0);
        list2.add(0.0);
        list2.add(2.0);
        list2.add(4.0);
        list2.add(6.0);
        list2.add(6.0);
        list2.add(9.0);
        Assert.assertEquals("0.0 1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0", NumList.union(list1, list2).toString());
        Assert.assertEquals("0.0 1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0", NumList.union(list2, list1).toString());
        list1 = new NumArrayList();
        list2 = new NumArrayList();
        list1.add(1.0);
        list1.add(3.0);
        list1.add(5.0);
        list1.add(6.0);
        list1.add(7.0);
        list1.add(8.0);
        list2.add(0.0);
        list2.add(2.0);
        list2.add(4.0);
        list2.add(6.0);
        list2.add(6.0);
        list2.add(9.0);
        Assert.assertEquals("0.0 1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0", NumList.union(list1, list2).toString());
    }
}
