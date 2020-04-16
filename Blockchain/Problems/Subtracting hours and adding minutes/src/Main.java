import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var o = LocalDateTime.parse(scanner.nextLine());
            var h = scanner.nextInt();
            var m = scanner.nextInt();
            var n = o.minusHours(h).plusMinutes(m);
            System.out.println(n);
        }
    }
}