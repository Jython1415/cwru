import org.junit.Test;
import org.junit.Assert;
import java.util.Random;
import java.util.Arrays;

public class SortTester {
    
    /**
     * Creating an array of ascending integers of the specified length that begins at the specified value
     * @param start the starting value
     * @param length the length of the array
     * @return an array of ascending integers
     */
    public static int[] ascendingArray(int start, int length) {
        int[] result = new int[length];
        
        for (int i = 0; i < length; i++) {
            result[i] = start + i;
        }

        return result;
    }

    /**
     * Creating an array of descending integers of the specified length that begins at the specified value
     * @param start the starting value
     * @param length the length of the array
     * @return an array of descending integers
     */
    public static int[] descendingArray(int start, int length) {
        int[] result = new int[length];
        
        for (int i = 0; i < length; i++) {
            result[i] = start - i;
        }

        return result;
    }

    /**
     * Creating an array of repeated integers of the specified length
     * @param value the value to repeat
     * @param length the length of the array
     * @return an array of the input value 
     */
    public static int[] repeatingArray(int value, int length) {
        int[] result = new int[length];
        
        for (int i = 0; i < length; i++) {
            result[i] = value;
        }

        return result;
    }

    /**
     * Swaps 2 random values in the input array
     * @param arr the array to swap values in
     */
    public static int[] randomSwap(int[] arr) {
        int a = (int)((new Random()).nextDouble() * arr.length);
        int b;

        do {
            b = (int)((new Random()).nextDouble() * arr.length);
        } while (b == a);

        int save = arr[a];
        arr[a] = arr[b];
        arr[b] = save;

        return arr;
    }

    /**
     * Performs n random swaps on the input array to randomize it
     * @param arr the array to randomize
     * @param n the number of random swaps
     */
    public static int[] randomizeArray(int[] arr, int n) {
        for (int i = 0; i < n; i++) {
            randomSwap(arr);
        }

        return arr;
    }

    /**
     * Unit tests for randomArray
     */
    @Test
    public void testRandomArray() {
        // length 0
        int[] a1 = Sort.randomArray(0, 1, 10);
        Assert.assertArrayEquals(new int[0], a1);

        // length 1 - invalid range
        a1 = Sort.randomArray(1, 1, -1);
        Assert.assertArrayEquals(new int[0], a1);

        // length 1 - range of 1
        a1 = Sort.randomArray(1, 0, 0);
        Assert.assertArrayEquals(new int[]{0}, a1);
        a1 = Sort.randomArray(1, 5, 5);
        Assert.assertArrayEquals(new int[]{5}, a1);
        a1 = Sort.randomArray(1, -5, -5);
        Assert.assertArrayEquals(new int[]{-5}, a1);

        // length 1 - large range
        a1 = Sort.randomArray(1, -1000, 1000);
        Assert.assertTrue(a1[0] >= -1000 && a1[0] <= 1000);

        // large array - range of 1
        a1 = Sort.randomArray(100, 0, 0);
        for (int n : a1) {
            Assert.assertTrue(n == 0);
        }

        // large array - large range
        a1 = Sort.randomArray(1000, -1000, 1000);
        for (int n : a1) {
            Assert.assertTrue(n >= -1000 && n <= 1000);
        }
    }

    /**
     * Unit tests for insertionSort
     */
    @Test
    public void testInsertionSort() {
        int[] a1;
        
        // null
        a1 = null;
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(null, a1);
        
        // length 0
        a1 = new int[0];
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(new int[0], a1);

        // length 1
        a1 = new int[]{0};
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(new int[]{0}, a1);

        // length 2
        a1 = new int[]{0, 1};
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{1, 0};
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{0, 0};
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(new int[]{0, 0}, a1);

        // large even length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1000);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = descendingArray(999, 1000);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = repeatingArray(0, 1000);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1000), a1);
        a1 = randomizeArray(ascendingArray(0, 1000), 1000);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);

        // large odd length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1001);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = descendingArray(1000, 1001);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = repeatingArray(0, 1001);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1001), a1);
        a1 = randomizeArray(ascendingArray(0, 1001), 1001);
        Sort.insertionSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);

        // large random
        a1 = Sort.randomArray(1000, -1000, 1000);
        int[] a2 = a1.clone();
        Sort.insertionSort(a2);
        int[] a3 = a1.clone();
        Arrays.sort(a3);
        Assert.assertArrayEquals(a3, a2);
    }

    /**
     * Unit tests for quickSort
     */
    @Test
    public void testQuickSort() {
        int[] a1;
        
        // null
        a1 = null;
        Sort.quickSort(a1);
        Assert.assertArrayEquals(null, a1);
        
        // length 0
        a1 = new int[0];
        Sort.quickSort(a1);
        Assert.assertArrayEquals(new int[0], a1);

        // length 1
        a1 = new int[]{0};
        Sort.quickSort(a1);
        Assert.assertArrayEquals(new int[]{0}, a1);

        // length 2
        a1 = new int[]{0, 1};
        Sort.quickSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{1, 0};
        Sort.quickSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{0, 0};
        Sort.quickSort(a1);
        Assert.assertArrayEquals(new int[]{0, 0}, a1);

        // large even length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1000);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = descendingArray(999, 1000);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = repeatingArray(0, 1000);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1000), a1);
        a1 = randomizeArray(ascendingArray(0, 1000), 1000);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);

        // large odd length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1001);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = descendingArray(1000, 1001);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = repeatingArray(0, 1001);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1001), a1);
        a1 = randomizeArray(ascendingArray(0, 1001), 1001);
        Sort.quickSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);

        // large random
        a1 = Sort.randomArray(1000, -1000, 1000);
        int[] a2 = a1.clone();
        Sort.quickSort(a2);
        int[] a3 = a1.clone();
        Arrays.sort(a3);
        Assert.assertArrayEquals(a3, a2);
    }

    /**
     * Unit tests for mergeSort
     */
    @Test
    public void testMergeSort() {
        int[] a1;
        
        // null
        a1 = null;
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(null, a1);
        
        // length 0
        a1 = new int[0];
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(new int[0], a1);

        // length 1
        a1 = new int[]{0};
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(new int[]{0}, a1);

        // length 2
        a1 = new int[]{0, 1};
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{1, 0};
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{0, 0};
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(new int[]{0, 0}, a1);

        // large even length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1000);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = descendingArray(999, 1000);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = repeatingArray(0, 1000);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1000), a1);
        a1 = randomizeArray(ascendingArray(0, 1000), 1000);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);

        // large odd length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1001);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = descendingArray(1000, 1001);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = repeatingArray(0, 1001);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1001), a1);
        a1 = randomizeArray(ascendingArray(0, 1001), 1001);
        Sort.mergeSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);

        // large random
        a1 = Sort.randomArray(1000, -1000, 1000);
        int[] a2 = a1.clone();
        Sort.mergeSort(a2);
        int[] a3 = a1.clone();
        Arrays.sort(a3);
        Assert.assertArrayEquals(a3, a2);
    }

    /**
     * Unit tests for bubbleSort
     */
    @Test
    public void testBubbleSort() {
        int[] a1;
        
        // null
        a1 = null;
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(null, a1);
        
        // length 0
        a1 = new int[0];
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(new int[0], a1);

        // length 1
        a1 = new int[]{0};
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(new int[]{0}, a1);

        // length 2
        a1 = new int[]{0, 1};
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{1, 0};
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(new int[]{0, 1}, a1);
        a1 = new int[]{0, 0};
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(new int[]{0, 0}, a1);

        // large even length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1000);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = descendingArray(999, 1000);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);
        a1 = repeatingArray(0, 1000);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1000), a1);
        a1 = randomizeArray(ascendingArray(0, 1000), 1000);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1000), a1);

        // large odd length - ascending, descending, repeated, and randomized
        a1 = ascendingArray(0, 1001);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = descendingArray(1000, 1001);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);
        a1 = repeatingArray(0, 1001);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(repeatingArray(0, 1001), a1);
        a1 = randomizeArray(ascendingArray(0, 1001), 1001);
        Sort.bubbleSort(a1);
        Assert.assertArrayEquals(ascendingArray(0, 1001), a1);

        // large random
        a1 = Sort.randomArray(1000, -1000, 1000);
        int[] a2 = a1.clone();
        Sort.bubbleSort(a2);
        int[] a3 = a1.clone();
        Arrays.sort(a3);
        Assert.assertArrayEquals(a3, a2);
    }
}
