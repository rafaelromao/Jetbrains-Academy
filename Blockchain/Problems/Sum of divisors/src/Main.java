import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var a = scanner.nextInt();
            var b = scanner.nextInt();
            var m = scanner.nextInt();
            var n = scanner.nextInt();
            var c = IntStream.rangeClosed(a, b)
                    .filter(x -> x % m == 0 || x % n == 0)
                    .reduce(0, (x, y) -> x + y);
            System.out.println(c);
        }
    }
}