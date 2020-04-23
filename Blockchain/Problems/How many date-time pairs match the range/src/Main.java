import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var start = LocalDateTime.parse(scanner.nextLine());
            var end = LocalDateTime.parse(scanner.nextLine());
            if (start.isAfter(end)) {
                var t = start;
                start = end;
                end = t;
            }
            var n = scanner.nextInt();
            scanner.nextLine();
            var r = 0;
            for (var i = 0; i < n; i++) {
                var current = LocalDateTime.parse(scanner.nextLine());
                if ((current.isEqual(start) || current.isAfter(start)) && current.isBefore(end)) {
                    r++;
                }
            }
            System.out.println(r);
        }
    }
}