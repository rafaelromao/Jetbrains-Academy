package analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NaiveSearchStrategy implements SearchStrategy {
    private String type;

    public NaiveSearchStrategy(String type) {
        this.type = type;
    }

    @Override
    public String search(InputStream content, String pattern) throws IOException {
        try (var reader = new InputStreamReader(content);
             var buffer = new BufferedReader(reader)) {
            var lines = buffer.lines();
            return lines.anyMatch(line -> line.contains(pattern))
                    ? type
                    : null;
        }
    }
}
