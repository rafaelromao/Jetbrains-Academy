package analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RabinKarpSearchStrategy implements SearchStrategy {
    @Override
    public boolean search(InputStream content, SearchPattern pattern) throws IOException {
        try (var reader = new InputStreamReader(content);
             var buffer = new BufferedReader(reader)) {
            var lines = buffer.lines();
            return lines.anyMatch(line -> !rabinKarp(line,pattern.getPattern()).isEmpty());
        }
    }

    public static long charToLong(char ch) {
        return ch;
    }

    public static List<Integer> rabinKarp(String text, String pattern) {
        var a = 53;
        var m = 1_000_000_000 + 9L;

        var patternHash = 0L;
        var currSubstrHash = 0L;
        var pow = 1L;

        for (var i = 0; i < pattern.length(); i++) {
            patternHash += charToLong(pattern.charAt(i)) * pow;
            patternHash += m;
            patternHash %= m;

            currSubstrHash += charToLong(text.charAt(text.length() - pattern.length() + i)) * pow;
            currSubstrHash += m;
            currSubstrHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        var occurrences = new ArrayList<Integer>();

        for (var i = text.length(); i >= pattern.length(); i--) {
            if (patternHash == currSubstrHash) {
                var patternIsFound = true;

                for (var j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                        patternIsFound = false;
                        break;
                    }
                }

                if (patternIsFound) {
                    occurrences.add(i - pattern.length());
                }
            }

            if (i > pattern.length()) {
                currSubstrHash = (currSubstrHash - charToLong(text.charAt(i - 1)) * pow % m + m) * a % m;
                currSubstrHash = (currSubstrHash + charToLong(text.charAt(i - pattern.length() - 1))) % m;
            }
        }

        Collections.reverse(occurrences);
        return occurrences;
    }
}
