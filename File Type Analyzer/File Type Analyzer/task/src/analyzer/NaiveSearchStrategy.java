package analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NaiveSearchStrategy implements SearchStrategy {
    @Override
    public boolean search(InputStream content, SearchPattern pattern) throws IOException {
        try (var reader = new InputStreamReader(content);
             var buffer = new BufferedReader(reader)) {
            var lines = buffer.lines();
            return lines.anyMatch(line -> contains(line, pattern.getPattern()));
        }
    }

    private boolean contains(String text, String pattern) {
        var found = true;
        for (var i = 0; i < text.length(); i++) {
            found = true;
            for (var j = 0; j < pattern.length(); j++) {
                if (i + j == text.length()) {
                    found = false;
                    break;
                }
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        return found;
    }
}
