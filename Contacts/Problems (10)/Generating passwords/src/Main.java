import java.util.*;

public class Main {
    public static void main(String[] args) {
        replaceElements(new int[] {17,18,5,4,6,1});
    }
    public static int[] replaceElements(int[] arr) {
        int[] r = new int[arr.length];
        for (int i=0; i<arr.length-1; i++) {
            r[i] = arr[i];
            for (int j=i+1; j<arr.length-1; j++) {
                if (arr[j] > r[i]) {
                    r[i] = arr[j];
                }
            }
        }
        r[r.length-1] = -1;
        return r;
    }
}
