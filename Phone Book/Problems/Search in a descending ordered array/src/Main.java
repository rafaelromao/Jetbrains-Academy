import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /* Modify this method */
    public static int binarySearch(int elem, int[] array) {

        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (elem == array[mid] && (mid == 0 || array[mid-1] > elem)) {
                return mid;
            } else if (elem < array[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int elem = scanner.nextInt();
        ArrayList<Integer> list = new ArrayList<>();
        while (scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        System.out.println(binarySearch(elem, array));
    }
}