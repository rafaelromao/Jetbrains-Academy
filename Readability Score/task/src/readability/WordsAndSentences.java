package readability;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class WordsAndSentences {
    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            var singleLine = scanner.nextLine();
            var result = Arrays
                    .stream(singleLine.split("\\.|\\?|!"))
                    .map(s -> s.strip())
                    .mapToInt(s -> s.split(" ").length)
                    .average()
                    .getAsDouble();
            var output = result > 10 ? "HARD" : "EASY";
            System.out.println(output);
        }
    }
}