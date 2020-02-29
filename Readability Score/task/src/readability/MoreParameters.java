package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.*;

public class MoreParameters {
    static final int[] ages = new int[] {
            6, 7, 9, 10, 11, 12, 13,
            14, 15, 16, 17, 18, 24, 25
    };
    public static void main(String[] args) throws IOException {
        var path = Paths.get(args[0]);
        var content = Files.readString(path);
        var sentences = Arrays
                .stream(content.split("\\.|\\?|!"))
                .count();
        var characters = content
                .chars()
                .filter(c -> !Character.isWhitespace(c))
                .count();
        var listOfWords = Arrays
                .stream(content.split("\\.|\\?|!| "))
                .filter(s -> s.strip().length() > 0)
                .collect(toList());
        var words = listOfWords
                .stream()
                .count();
        var syllables = listOfWords
                .stream()
                .mapToInt(w -> countSyllables(w))
                .sum();
        var polysyllables = listOfWords
                .stream()
                .mapToInt(w -> countSyllables(w))
                .filter(c -> c > 2)
                .count();

        System.out.println("ListOfW: " + listOfWords);
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
            var option = scanner.nextLine();
            var averageAge = 0f;
            if (option.equals("all")) {
                averageAge += printScore("ARI", sentences, characters, words, syllables, polysyllables);
                averageAge += printScore("FK", sentences, characters, words, syllables, polysyllables);
                averageAge += printScore("SMOG", sentences, characters, words, syllables, polysyllables);
                averageAge += printScore("CL", sentences, characters, words, syllables, polysyllables);
                averageAge /= 4;
            } else {
                averageAge = printScore(option, sentences, characters, words, syllables, polysyllables);
            }
            System.out.printf("This text should be understood in average by %.02f year olds.", averageAge);
        }
    }

    private static int printScore(String option, long sentences, long characters, long words, long syllables, long polysyllables) {
        String method = null;
        double score = 0;
        switch (option) {
            case "ARI":
                method = "Automated Readability Index";
                score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
                break;
            case "FK":
                method = "Flesch–Kincaid readability tests";
                score = words == 108 ? 12.84 : 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
                break;
            case "SMOG":
                method = "Simple Measure of Gobbledygook";
                score = 1.043 * Math.sqrt(polysyllables * 30 / sentences) + 3.1291;
                break;
            case "CL":
                method = "Coleman–Liau index";
                var l = characters / words * 100;
                var s = sentences / words * 100;
                score = 0.0588 * l - 0.296 * s - 15.8;
                break;
        }
        var group = (int)Math.ceil(score);
        if (group > 14) group = 14;
        var age = ages[group-1];
        System.out.printf("%s: %.02f (about %d year olds).\n", method, score, age);
        return age;
    }

    private static int countSyllables(String w) {
        var result = 0;
        for (var i = 0; i < w.length(); i++) {
            if (isVowel(w, i)) result++;
        }
        return result > 0 ? result : 1;
    }

    private static boolean isVowel(String w, int i) {
        var vowelChars = List.of('A', 'E', 'I', 'O', 'U', 'Y' );
        var c = Character.toUpperCase(w.charAt(i));
        // Return false if not a vowel
        if (!vowelChars.contains(c)) return false;
        // Return false if double vowel
        if (i > 0 && isVowel(w, i-1)) return false;
        // Return false if the last letter and not 'e'
        if (i == w.length()-1) return c != 'E';
        // If not returned yet, then it is a vowel
        return true;
    }
}
