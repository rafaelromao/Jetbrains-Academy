import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var x = LocalDate.parse(scanner.next());
            var m = LocalDate.parse(scanner.next());
            var n = LocalDate.parse(scanner.next());
            if (n.isBefore(m)) {
                var t = m;
                m = n;
                n = t;
            }
            System.out.print(x.isAfter(m) && x.isBefore(n));
        }
    }
}