import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var text = scanner.nextLine();
            var result = text.replaceAll("\\/\\*.*?\\*\\/|\\/\\/.+", "");
            System.out.println(result);
        }
    }
}