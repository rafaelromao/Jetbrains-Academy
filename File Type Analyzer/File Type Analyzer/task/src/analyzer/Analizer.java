package analyzer;

import java.io.*;

public class Analizer {
    public boolean isMatch(InputStream content, String pattern) throws IOException {
        try (var reader = new InputStreamReader(content);
             var buffer = new BufferedReader(reader)) {
            var lines = buffer.lines();
            return lines.anyMatch(line -> line.matches(".*" + pattern + ".*"));
        }
    }
}
