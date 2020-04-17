// Posted from EduTools plugin
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var f = LocalDateTime.parse(scanner.nextLine());
            var l = LocalDateTime.parse(scanner.nextLine());
            var d = Duration.between(l, f);
            System.out.println(Math.abs(d.toHours()));
        }
    }
}