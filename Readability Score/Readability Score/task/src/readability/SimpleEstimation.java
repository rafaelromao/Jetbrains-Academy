package readability;

import java.io.IOException;
import java.util.Scanner;

public class SimpleEstimation {
    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            var singleLine = scanner.nextLine();
            var count = singleLine
                    .chars()
                    .count();
            var result = count > 100 ? "HARD" : "EASY";
            System.out.println(result);
        }
    }
}