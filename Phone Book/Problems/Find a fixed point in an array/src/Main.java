import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var a = new int[n];
            for (var i = 0; i < n; i++) {
                a[i] = scanner.nextInt();
            }
            var hasFixedPoint = hasFixedPoint(a);
            System.out.println(hasF);
        }
    }

    private static boolean hasFixedPoint(int[] a) {
    }
}