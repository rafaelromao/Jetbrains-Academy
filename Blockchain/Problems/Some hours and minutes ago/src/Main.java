// Posted from EduTools plugin
import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var o = LocalTime.parse(scanner.nextLine());
            var h = scanner.nextInt();
            var m = scanner.nextInt();
            var n = o.minusHours(h).minusMinutes(m);
            System.out.println(n);
        }
    }
}