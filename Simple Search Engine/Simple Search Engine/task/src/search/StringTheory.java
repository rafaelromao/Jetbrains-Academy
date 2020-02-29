package search;

import java.util.Arrays;
import java.util.Scanner;

public class StringTheory {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var phrase = scanner.nextLine();
            var word = scanner.nextLine();
            var words = phrase.split(" ");
            var index = Arrays.asList(words).indexOf(word)+1;
            var result = index == 0 ? "Not Found" : index;
            System.out.println(result);
        }
    }
}
