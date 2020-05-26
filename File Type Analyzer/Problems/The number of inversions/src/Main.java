import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var a = new int[n];
            for (var i = 0; i < n; i++) {
                a[i] = scanner.nextInt();
            }
            var r = mergeSort(a, 0, a.length);
            System.out.println(r);
        }
    }

    public static long mergeSort(int[] array, int leftIncl, int rightExcl) {
        // the base case: if subarray contains <= 1 items, stop dividing because it's sorted
        if (rightExcl <= leftIncl + 1) {
            return 0;
        }

        /* divide: calculate the index of the middle element */
        int middle = leftIncl + (rightExcl - leftIncl) / 2;

        long result = 0;

        result += mergeSort(array, leftIncl, middle);  // conquer: sort the left subarray
        result += mergeSort(array, middle, rightExcl); // conquer: sort the right subarray

        /* combine: merge both sorted subarrays into sorted one */
        result += merge(array, leftIncl, middle, rightExcl);

        return result;
    }

    private static long merge(int[] array, int left, int middle, int right) {
        long inversions = 0;

        int i = left;   // index for the left subarray
        int j = middle; // index for the right subarray
        int k = 0;      // index for the temp subarray

        int[] temp = new int[right - left]; // temporary array for merging

    /* get the next lesser element from one of two subarrays
       and then insert it in the array until one of the subarrays is empty */
        while (i < middle && j < right) {
            if (array[i] <= array[j]) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
                inversions += middle - i;
            }
            k++;
        }

        /* insert all the remaining elements of the left subarray in the array */
        for (; i < middle; i++, k++) {
            temp[k] = array[i];
        }

        /* insert all the remaining elements of the right subarray in the array */
        for (; j < right; j++, k++) {
            temp[k] = array[j];
        }

        /* effective copying elements from temp to array */
        System.arraycopy(temp, 0, array, left, temp.length);

        return inversions;
    }
}