import java.util.*;

public class RabinKarp {

    public static long charToLong(char ch) {
        return (long)(ch - 'A' + 1);
    }

    public static List<String> rabinKarp(String text, String pattern) {
        var a = 53;
        var m = 1_000_000_000 + 9L;

        var patternHash = 0L;
        var currSubstrHash = 0L;
        var pow = 1L;

        for (var i = 0; i < pattern.length(); i++) {
            patternHash += charToLong(pattern.charAt(i)) * pow;
            patternHash %= m;

            currSubstrHash += charToLong(text.charAt(text.length() - pattern.length() + i)) * pow;
            currSubstrHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        var occurrences = new ArrayList<String>();

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
                    var index = i - pattern.length();
                    occurrences.add(text.substring(index, i));
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