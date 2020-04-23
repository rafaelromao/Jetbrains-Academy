import java.util.*;
import java.time.*;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            scanner.nextLine();
            LocalDateTime latest = LocalDateTime.MIN;
            for (var i = 0; i < n; i++) {
                var current = LocalDateTime.parse(scanner.nextLine());
                if (current.isAfter(latest)) {
                    latest = current;
                }
            }
            System.out.println(latest);
        }
    }
}
