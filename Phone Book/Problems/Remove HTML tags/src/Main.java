import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var stringWithHTMLTags = scanner.nextLine();
            var result = stringWithHTMLTags.replaceAll("<\\/?.+?>", "");
            System.out.println(result);
        }
    }
}