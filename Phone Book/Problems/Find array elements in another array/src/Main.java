import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var a = new int[n];
            for (var i = 0; i < n; i++) {
                a[i] = scanner.nextInt();
            }
            var k = scanner.nextInt();
            for (var j = 0; j < k; j++) {
                var i = Arrays.binarySearch(a, scanner.nextInt());
                System.out.print(i > -1 ? i + 1 : -1);
                System.out.print(" ");
            }
        }
    }
}