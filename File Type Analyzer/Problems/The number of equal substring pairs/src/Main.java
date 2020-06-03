import java.util.*;

public class Main {
    private static final int A = 53;
    private static final long M = 1_000_000_000 + 9L;
    private static final List<Long> POWS = new ArrayList<>();

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var s = scanner.nextLine().trim();
            var t = scanner.nextInt();
            var ijkns = new int[t][];
            for (var i = 0; i < t; i++) {
                ijkns[i] = new int[4];
                ijkns[i][0] = scanner.nextInt();
                ijkns[i][1] = scanner.nextInt();
                ijkns[i][2] = scanner.nextInt();
                ijkns[i][3] = scanner.nextInt();
            }
            var prefixHashes = getPrefixHashes(s);
            var count = 0;
            for (var ijkn : ijkns) {
                var sij = getSubstringHash(prefixHashes, ijkn, 0, 1);
                var skn = getSubstringHash(prefixHashes, ijkn, 2, 3);
                if (sij == skn) count++;
            }
            System.out.println(count);
        }
    }

    private static long getSubstringHash(List<Long> prefixHashes, int[] ijkn, int ik, int jn) {
        var i = ijkn[ik];
        var j = ijkn[jn];
        var pj = prefixHashes.get(j);
        var pi = prefixHashes.get(i);
        var ai = POWS.get(i);
        return (pj - pi + M) % M / ai;
    }

    private static List<Long> getPrefixHashes(String text) {
        var hashes = new ArrayList<Long>();
        for (var limit = 0; limit <= text.length(); limit++) {
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
            if (POWS.size() < i + 1) {
                POWS.add(pow);
                pow = pow * A % M;
            } else {
                pow = POWS.get(i);
            }
        }
        return hash;
    }

    public static long charToLong(char ch) {
        return ch - 'A' + 1;
    }
}