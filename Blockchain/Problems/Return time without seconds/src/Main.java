import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var o = LocalTime.parse(scanner.nextLine());
            o = o.withSecond(0);
            System.out.println(o);
        }
    }
}