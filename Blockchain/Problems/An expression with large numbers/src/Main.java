import java.math.BigInteger;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var a = new BigInteger(scanner.next());
            var b = new BigInteger(scanner.next());
            var c = new BigInteger(scanner.next());
            var d = new BigInteger(scanner.next());
            var r = a.negate().multiply(b).add(c).subtract(d);
            System.out.println(r);
        }
    }
}