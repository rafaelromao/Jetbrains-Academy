package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Score {
    static final String[] ages = new String[] {
            "5-6", "6-7", "7-9", "9-10", "10-11", "11-12", "12-13",
            "13-14", "14-15", "15-16", "16-17", "17-18", "18-24", "24+"
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
        var words = Arrays
                .stream(content.split("\\.|\\?|!| "))
                .filter(s -> s.strip().length() > 0)
                .count();
        var score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
        var age = (int)Math.round(score)-1;
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Test score is: " + score);
        System.out.printf("This text should be understood by %s year olds.", ages[age]);
    }
}