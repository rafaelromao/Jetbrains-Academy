// Posted from EduTools plugin
import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var o = LocalDateTime.parse(scanner.nextLine());
            var n = o.plusHours(11).toLocalDate();
            System.out.println(n);
        }
    }
}