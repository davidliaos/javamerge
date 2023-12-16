import java.util.Arrays;
import java.util.Scanner;

public class Main {

    //initial implementation of merge sort as a function
    public static void mergeSort(int[] array) {
        if (array.length <= 1) {
            return;
        }

        // split the array into two halves
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);

        // recursive sort
        mergeSort(left);
        mergeSort(right);

        // merge sort with finalMerge function
        int[] result = finalMerge(left, right);

        // override original arr
        System.arraycopy(result, 0, array, 0, result.length);
    }

    // final merge when the individual arrays are sorted
    public static int[] finalMerge(int[] left, int[] right) {
        int[] sorted = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        // merge left & right
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                sorted[k++] = left[i++];
            } else {
                sorted[k++] = right[j++];
            }
        }

        // copy rest of left arr
        while (i < left.length) {
            sorted[k++] = left[i++];
        }

        // copy rest of right arr
        while (j < right.length) {
            sorted[k++] = right[j++];
        }

        return sorted;
    }

    public static void parallelMergeSort(int[] array, int threadCount) {
        if (threadCount <= 1) {
            mergeSort(array);
        } else if (array.length >= 2) {
            // split in half
            int mid = array.length / 2;
            int[] left = Arrays.copyOfRange(array, 0, mid);
            int[] right = Arrays.copyOfRange(array, mid, array.length);

            // sort each half in its own thread
            Thread leftSorter = new Thread(() -> parallelMergeSort(left, threadCount / 2));
            Thread rightSorter = new Thread(() -> parallelMergeSort(right, threadCount / 2));
            leftSorter.start();
            rightSorter.start();

            try {
                leftSorter.join();
                rightSorter.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // merge results
            int[] result = finalMerge(left, right);

            // copy merged result back into original array
            System.arraycopy(result, 0, array, 0, result.length);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] input = scanner.nextLine().split(" ");
        
        // converting input to an integer array
        int[] array = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            array[i] = Integer.parseInt(input[i]);
        }

        // determine the number of threads based on the size of the array
        int threadCount = Math.max(1, array.length / 100 * 2);

        // sorting the array
        parallelMergeSort(array, threadCount);

        // output in  format
        for (int num : array) {
        System.out.print(num + " ");
        }

        scanner.close();
    }
}
