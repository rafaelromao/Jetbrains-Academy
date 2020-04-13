import java.util.*;

public class Main {
    public static void main(String[] args) {
        //try (var scanner = new Scanner(System.in)) {
        //var n = scanner.nextInt();
        var n = Integer.parseInt(args[0]);
        var b = (int) Math.floor(Math.sqrt(n));
        var j = 0;
        var v = 0;
        var m = 0;
        for (var i = 0; i < n; i++) {
            if (i % b == 0) {
                System.out.printf("%d ", ++j);
                m = (j + 1) * b;
                v = m - (j * b) + j;
            } else {
                if (m - 1 > n && i >= (j - 1) * b) {
                    var l = n - ((n / b) * b);
                    l = l == 0 ? b - 1 : l + 1;
                    v -= b - l;
                }
                System.out.printf("%d ", v--);
            }
        }
        //}
    }
}