// Posted from EduTools plugin
import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var f = LocalTime.parse(scanner.nextLine());
            var l = LocalTime.parse(scanner.nextLine());
            var d = Duration.between(l, f);
            System.out.println(Math.abs(d.toSeconds()));
        }
    }
}