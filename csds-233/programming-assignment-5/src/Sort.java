import java.util.Random;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.File;

public class Sort {

    /**
     * Swaps two values in an array
     * @param arr the array
     * @param a the first index
     * @param b the second index
     * @return the array
     */
    private static int[] swap(int[] arr, int a, int b) {
        int save = arr[a];
        arr[a] = arr[b];
        arr[b] = save;

        return arr;
    }

    /**
     * Creates a new array of length n with random integers in the range of [a, b]
     * @param n the length of the array
     * @param a the lower bound (inclusive)
     * @param b the upper bound (inclusive)
     * @return an int[] of random integers
     */
    public static int[] randomArray(int n, int a, int b) {
        if (b < a) { // check for invalid input
            return new int[0];
        }

        int[] result = new int[n];
        
        /* generate values for each value of the array*/
        for (int i = 0; i < n; i++) {
            result[i] = a + (int)((new Random()).nextDouble() * (b - a + 1));
        }

        return result;
    }

    /**
     * Sorts the input array using insertionSort
     * This checks each value (left to right) and finds a place in the sorted portion of the array (on the left) to insert the element into
     * Shifts elements down to make space for the insertion
     * @param arr the array to sort
     */
    public static void insertionSort(int[] arr) {
        if (arr == null) {
            return;
        }
        
        /* i goes through each value that is to be inserted
         * everything to the left of i is sorted */
        for (int i = 1; i < arr.length; i++) {
            int j = i - 1;
            /* j goes back to find a space for the new element */
            while (j >= 0 && arr[j] > arr[j + 1]) {
                swap(arr, j, j + 1);
                j--;
            }
        }
    }


    
    /**
     * Sorts the input array using quickSort
     * quickSort divides the array into halves (left <= right) until the sub-array size is 1, and as a result of the dividing conditions, the array is sorted
     * @param arr the array to sort
     */
    public static void quickSort(int[] arr) {
        if (arr == null) {
            return;
        }

        qSort(arr, 0, arr.length - 1);
    }

    /**
     * Helper method for quickSort that allows the array to be divided (and then conquered)
     * @param arr the array to sort
     * @param start beginning (inclusive) of section to sort
     * @param end ending (inclusive) of section to sort
     */
    private static void qSort(int[] arr, int start, int end) {
        if (end - start < 1) { // base case
            return;
        }

        // selects a random pivot
        int pivot = arr[start + (int)((new Random()).nextDouble() * (end - start + 1))];

        int partition = partition(arr, pivot, start, end);

        qSort(arr, start, partition);
        qSort(arr, partition + 1, end);
    }

    /**
     * Partitions an array (or part of 1) into two halves and finds the division
     * Left is less than or equal to the pivot, right is greater than or equal to the pivot
     * @param arr the array the values are from
     * @param pivot the pivot
     * @param start the left hand boundary (inclusive)
     * @param end the right hand boundary (inclusive)
     * @return the upper boundary (inclusive) of the left sub-array after partitioning
     */
    private static int partition(int[] arr, int pivot, int start, int end) {
        int i = start - 1; // i finds values that need to be moved to the right sub-array
        int j = end + 1; // j finds values that need to be moved to the left sub-array

        while (true) {
            do {
                i++;
            } while (i < arr.length && arr[i] < pivot);
            do {
                j--;
            } while (j > -1 && arr[j] > pivot);

            if (i > j) { // if they have crossed over, then the partition has been found
                return j;
            }
            else { // swap if both i and j have values they can swap over
                swap(arr, i, j);
            }
        }
    }

    /**
     * Sorts the input array using mergeSort
     * Divides the array into sub-arrays until size is 1 (an array of 1 is sorted)
     * Recombines them into larger sorted sub-arrays until the whole array is sorted
     * @param arr the array to sort
     */
    public static void mergeSort(int[] arr) {
        if (arr == null) {
            return;
        }

        /* this temporary array is necessary for efficiently combining two sorted sub-arrays
         * enough space is created to allow for all the recursive calls to reference their part without overlapping */
        int[] temp = new int[arr.length];

        mSort(arr, temp, 0, arr.length - 1);
    }

    /**
     * Helper method for mergeSort
     * @param arr the array to sort
     * @param temp 
     * @param start
     * @param end
     */
    private static void mSort(int[] arr, int[] temp, int start, int end) {
        if (end <= start) { // base case
            return;
        }
        
        int split = (start + end) / 2;

        mSort(arr, temp, start, split);
        mSort(arr, temp, split + 1, end);

        // merge the two halves
        merge(arr, temp, start, split, split + 1, end);
    }

    /**
     * Merges two (sorted) sub-arrays into one sorted array by using a temporary array as space to process the values
     * @param arr the array to sort
     * @param temp the temporary array
     * @param leftStart the lower boundary (inclusive) of the left sub-array
     * @param leftEnd the upper boundary (inclusive) of the right sub-array
     * @param rightStart the lower boundary (inclusive) of the right sub-array
     * @param rightEnd the upper boundary (inclusive) of the right sub-array
     */
    private static void merge(int[] arr, int[] temp, int leftStart, int leftEnd, int rightStart, int rightEnd) {
        // merge
        int i = leftStart;
        int j = rightStart;
        int k = leftStart;
        while (i <= leftEnd && j <= rightEnd) { // compare values to copy over the lower one
            if (arr[i] < arr[j]) {
                temp[k++] = arr[i++];
            }
            else {
                temp[k++] = arr[j++];
            }
        }
        while (i <= leftEnd) { // copy over remaining
            temp[k++] = arr[i++];
        }
        while (j <= rightEnd) { // copy over remaining
            temp[k++] = arr[j++];
        }

        // copy back
        for (int l = 0; l < rightEnd + 1; l++) {
            arr[l] = temp[l];
        }
    }

    /**
     * Sorts the input array using bubbleSort
     * Repeatedly goes through the array from left to right, swapping adjacent values if they are out of order
     * As a result, by the k-th iteration, the k right-most elements are sorted
     * @param arr the array to sort
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null) {
            return;
        }

        /* repeated arr.length times to make sure the smallest value on the rightmost side can make it all the way over */
        for (int i = 0; i < arr.length; i++) {
            /* goes through left to right, swapping values */
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    /**
     * Java's built in sort method
     * @param arr the array to sort
     */
    public static void javaSort(int[] arr) {
        Arrays.sort(arr);
    }

    private static void benchmark() throws Exception {
        String[] methodNames = new String[]{"insertionSort", "quickSort", "mergeSort", "bubbleSort", "javaSort"};
        int[] sizes = new int[]{10, 20, 50, 100, 200, 500, 1000, 2000, 5000};
        int repetitions = 100;

        Hashtable<String, ArrayList<ArrayList<Long>>> results = new Hashtable<String, ArrayList<ArrayList<Long>>>(sizes.length);

        // time all methods
        for (String methodName : methodNames) {
            results.put(methodName, new ArrayList<ArrayList<Long>>(sizes.length));

            Method method = Sort.class.getDeclaredMethod(methodName, int[].class);
            method.setAccessible(true);

            for (int i = 0; i < sizes.length; i++) {
                int size = sizes[i];

                results.get(methodName).add(new ArrayList<Long>());
                for (int j = 0; j < 4; j++) {
                    results.get(methodName).get(i).add(0L);  
                }
                
                for (int j = 0; j < repetitions; j++) {
                    int[] a1 = Sort.randomArray(size, 0, 100);
                    int[] a2 = SortTester.ascendingArray(0, size);
                    int[] a3 = SortTester.descendingArray(size - 1, size);
                    int[] a4 = SortTester.randomizeArray(SortTester.ascendingArray(0, size), (int)Math.ceil(size / (double)10)); // 10% mixed
                    int[][] arrays = new int[][]{a1, a2, a3, a4};
                    long time;
                    
                    for (int k = 0; k < arrays.length; k++) {
                        long start;
                        start = System.nanoTime();
                        method.invoke(null, a1);
                        time = System.nanoTime() - start;
                        results.get(methodName).get(i).set(k, results.get(methodName).get(i).get(k) + time);
                    }
                }

                for (int k = 0; k < results.get(methodName).get(i).size(); k++) {
                    results.get(methodName).get(i).set(k, results.get(methodName).get(i).get(k) / repetitions);
                }
            }
        }

        // print out
        for (String methodName : methodNames) {
            System.out.println(methodName);

            for (int i = 0; i < sizes.length; i++) {
                System.out.print(sizes[i] + ": ");
                System.out.print(results.get(methodName).get(i) + "\n");
            }

            System.out.println("");
        }

        // create csv for exporting
        File resultsFile = new File("benchmarking.csv");
        resultsFile.createNewFile();
        CSVWriter csvWriter = new CSVWriter(new FileWriter(resultsFile));
        
        String[] nextLine;

        for (int i = 0; i < 4; i++) {

            nextLine = new String[sizes.length + 1];
            nextLine[0] = Integer.toString(i);
            for (int j = 1; j < sizes.length; j++) {
                nextLine[j] = "";
            }
            csvWriter.writeNext(nextLine);

            nextLine = new String[sizes.length + 1];
            for (int j = 0; j < sizes.length; j++) {
                nextLine[j + 1] = Integer.toString(sizes[j]);
            }
            csvWriter.writeNext(nextLine);

            for (String methodName : methodNames) {
                nextLine = new String[sizes.length + 1];

                nextLine[0] = methodName;

                for (int k = 0; k < sizes.length; k++) {
                    nextLine[k + 1] = Long.toString(results.get(methodName).get(k).get(i));
                }

                csvWriter.writeNext(nextLine);
            }
            
            nextLine = new String[sizes.length + 1];
            for (int j = 0; j < sizes.length; j++) {
                nextLine[j] = "";
            }
            csvWriter.writeNext(nextLine);
        }
        csvWriter.close();
    }

    /**
     * Main method to demonstrate the functionality of all the methods
     */
    public static void main(String[] args) throws Exception {
        if (args.length >= 1 && args[0].equals("benchmark")) {
            benchmark();
        }
        else {
            System.out.println("DEMONSTRATION\n");

            System.out.println("Create a new random array using Sort.randomArray(20, 1, 10)");

            int[] a1 = Sort.randomArray(20, 1, 10);

            System.out.print("[");
            for (int i = 0; i < a1.length - 1; i++) {
                System.out.print(a1[i] + ", ");
            }
            System.out.print(a1[a1.length - 1] + "]\n\n");

            String[] methodNames = new String[]{"insertionSort", "quickSort", "mergeSort", "bubbleSort", "javaSort"};

            for (String methodName : methodNames) {    
                System.out.println("---------- Sort with " + methodName + " ----------");

                Method method = Sort.class.getDeclaredMethod(methodName, int[].class);
                method.setAccessible(true);
    
                System.out.print("Unsorted: [");
                for (int i = 0; i < a1.length - 1; i++) {
                    System.out.print(a1[i] + ", ");
                }
                System.out.print(a1[a1.length - 1] + "]\n");

                int[] copy = a1.clone();

                long start = System.nanoTime();
                method.invoke(null, copy);
                long time = System.nanoTime() - start;
                System.out.println("sorting with " + methodName + "...");

                System.out.print("Sorted: [");
                for (int i = 0; i < copy.length - 1; i++) {
                    System.out.print(copy[i] + ", ");
                }
                System.out.print(copy[copy.length - 1] + "]\n");
                System.out.println("Took " + Long.toString(time) + " nano seconds\n");
            }

            System.out.println("DEMONSTRATION OVER");
            System.out.println("For more detailed timings, run Sort with \"benchmark\" as the first argument and see the generated csv file");
        }
    }
}
