import java.util.*;

public class Main {
    private static int k;
    private static final int A = 53;
    private static final long M = 1_000_000_000 + 9L;
    private static final Map<Long, Integer> HASH_MAP = new HashMap<>();

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var t = scanner.nextLine();
            k = scanner.nextInt();
            var r = getFirstRepeatedSubstring(t, k);
            System.out.println(r.orElse("does not contain"));
        }
    }

    private static Optional<String> getFirstRepeatedSubstring(String t, int k) {
        var currentHash = 0L;
        var pow = 1L;
        // Hash the last substring to use as reference
        for (var i = t.length() - k; i < t.length(); i++) {
            var includedChar = t.charAt(i);
            var includedLong = charToLong(includedChar);
            currentHash += includedLong * pow;
            currentHash %= M;
            if (i < t.length() - 1) {
                pow = pow * A % M;
            }
        }
        map(t, t.length() - k, currentHash);
        // Hash and compare all remaining substring, from the end to the beginning
        for (var i = t.length() - k - 1; i >= 0; i--) {
            var removedChar = t.charAt(i + k);
            var removedLong = charToLong(removedChar);
            var includedChar = t.charAt(i);
            var includedLong = charToLong(includedChar);
            currentHash = (currentHash - removedLong * pow % M + M) * A % M;
            currentHash = (currentHash + includedLong) % M;
            var occurrence = map(t, i, currentHash);
            if (occurrence.isPresent()) {
                return occurrence;
            }
        }
        return Optional.empty();
    }

    private static Optional<String> map(String t, int i, long h) {
        if (HASH_MAP.containsKey(h)) {
            // It is important to store the start position instead of the substring,
            // otherwise it will cause 4 seconds of GC and exceed the time limit
            var now = t.substring(i, i + k);
            var j = HASH_MAP.get(h);
            var then = t.substring(j, j + k);
            if (then.equals(now)) {
                return Optional.of(now);
            }
        }
        HASH_MAP.put(h, i);
        return Optional.empty();
    }

    public static long charToLong(char ch) {
        return ch - 'A' + 1;
    }
}