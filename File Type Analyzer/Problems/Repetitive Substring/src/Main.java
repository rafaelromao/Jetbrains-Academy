import java.util.*;

public class Main {
    private static int k;
    private static final int a = 53;
    private static final long m = 1_000_000_000 + 9L;
    private static final Map<Long, String> map = new HashMap<>();

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
            currentHash %= m;
            if (i < t.length() - 1) {
                pow = pow * a % m;
            }
        }
        map(t, t.length() - k, currentHash);
        // Hash and compare all remaining substring, from the end to the beginning
        for (var i = t.length() - k - 1; i >= 0; i--) {
            var removedChar = t.charAt(i + k);
            var removedLong = charToLong(removedChar);
            var includedChar = t.charAt(i);
            var includedLong = charToLong(includedChar);
            currentHash = (currentHash - removedLong * pow % m + m) * a % m;
            currentHash = (currentHash + includedLong) % m;
            var f = map(t, i, currentHash);
            if (f.isPresent()) {
                return f;
            }
        }
        return Optional.empty();
    }

    private static Optional<String> map(String t, int i, long h) {
        var s = t.substring(i, i + k);
        if (map.containsKey(h)) {
            if (map.get(h).equals(s)) {
                return Optional.of(s);
            }
        }
        map.put(h, s);
        return Optional.empty();
    }

    public static long charToLong(char ch) {
        return ch - 'A' + 1;
    }
}