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
            return lines.anyMatch(line -> line.contains(pattern.getPattern()));
        }
    }
}
