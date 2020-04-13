import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var a = new String[n][n];
            for (var i = 0; i < n; i++) {
                for (var j = 0; j < n; j++) {
                    if (i == n / 2 || j == n / 2) {
                        a[i][j] = "*";
                    } else if (i == j || n - i - 1 == j) {
                        a[i][j] = "*";
                    } else {
                        a[i][j] = ".";
                    }
                }
            }
            for (var l : a) {
                for (var i : l) {
                    System.out.print(i);
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
    }
}