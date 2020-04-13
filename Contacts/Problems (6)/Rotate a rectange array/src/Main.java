import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)){
            var dim = scanner.nextLine().split(" ");
            var n = Integer.parseInt(dim[0]);
            var m = Integer.parseInt(dim[1]);
            var a = new int[n][m];
            var r = new int[m][n];
            for (var i = 0; i < n; i++){
                var l = scanner.nextLine().split(" ");
                for (var j = 0; j < m; j++){
                    a[i][j] = Integer.parseInt(l[j]);
                }
            }
            for (var i = 0; i < n; i++){
                for (var j = 0; j < m; j++) {
                    r[j][n-i-1] = a[i][j];
                }
            }
            for (var l : r) {
                for (var i : l) {
                    System.out.print(i);
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
    }
}