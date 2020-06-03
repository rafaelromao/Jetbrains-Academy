import java.util.*;

public class Main {
    private static final int A = 53;
    private static final long M = 1_000_000_000 + 9L;

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var t = scanner.nextLine();
            var k = scanner.nextInt();
            var ijs = new int[k][];
            for (var i = 0; i < k; i++) {
                ijs[i] = new int[2];
                ijs[i][0] = scanner.nextInt();
                ijs[i][1] = scanner.nextInt();
            }
            var prefixes = getPrefixHashes(t);
            prefixes.stream().forEach(p -> System.out.printf("%s ", p));
            System.out.println();
            for (var ij : ijs) {
                var i = ij[0];
                var j = ij[1];
                var r = (prefixes.get(j - 1) - prefixes.get(i - 1) + M) % M;
                System.out.printf("%s ", r);
            }
        }
    }

    private static List<Long> getPrefixHashes(String text) {
        var hashes = new ArrayList<Long>();
        for (var limit = 1; limit <= text.length(); limit++) {
            var hash = hash(text, limit);
            hashes.add(hash);
        }
        return hashes;
    }

    private static long hash(String text, int limit) {
        var hash = 0L;
        var pow = 1L;
        for (var i = 0; i < limit; i++) {
            var includedChar = text.charAt(i);
            var includedLong = charToLong(includedChar);
            hash += includedLong * pow;
            hash %= M;
            pow = pow * A % M;
        }
        return hash;
    }

    public static long charToLong(char ch) {
        return ch - 'A' + 1;
    }
}