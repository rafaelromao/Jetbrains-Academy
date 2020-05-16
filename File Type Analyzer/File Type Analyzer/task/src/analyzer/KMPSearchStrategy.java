package analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class KMPSearchStrategy implements SearchStrategy {
    private String type;

    public KMPSearchStrategy(String type) {
        this.type = type;
    }

    @Override
    public String search(InputStream content, String pattern) throws IOException {
        try (var reader = new InputStreamReader(content);
             var buffer = new BufferedReader(reader)) {
            var lines = buffer.lines();
            return lines.anyMatch(line -> !kmpSearch(line, pattern).isEmpty())
                    ? type
                    : null;
        }
    }

    private static List<Integer> kmpSearch(String text, String pattern) {
        var prefix = prefix(pattern);
        var occurrences = new ArrayList<Integer>();
        var j = 0;
        for (var i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefix[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j += 1;
            }
            if (j == pattern.length()) {
                occurrences.add(i - j + 1);
                j = prefix[j - 1];
            }
        }
        return occurrences;
    }

    private static int[] prefix(String str) {
        var prefix = new int[str.length()];
        for (var i = 1; i < str.length(); i++) {
            var j = prefix[i - 1];
            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefix[j - 1];
            }
            if (str.charAt(i) == str.charAt(j)) {
                j += 1;
            }
            prefix[i] = j;
        }
        return prefix;
    }
}
