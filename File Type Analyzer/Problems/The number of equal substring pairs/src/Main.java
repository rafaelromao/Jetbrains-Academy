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
            var prefixHashes = prefixHashes(s);
            var count = 0;
            for (var ijkn : ijkns) {
                var sij = substringHash(prefixHashes, ijkn, 0, 1);
                var skn = substringHash(prefixHashes, ijkn, 2, 3);
                if (sij == skn) count++;
            }
            System.out.println(count);
        }
    }

    private static long substringHash(List<Long> prefixHashes, int[] ijkn, int ik, int jn) {
        var i = ijkn[ik];
        var j = ijkn[jn];
        var pj = prefixHashes.get(j - 1);
        var pi = i > 0 ? prefixHashes.get(i - 1) : 0;
        var ai = POWS.get(i);
        var p = (pj - pi + M) % M;
        return p / ai;
    }

    private static List<Long> prefixHashes(String text) {
        var hashes = new ArrayList<Long>();
        var hash = 0L;
        for (var index = 0; index < text.length(); index++) {
            hash = prefixHash(text, index, hash);
            hashes.add(hash);
        }
        return hashes;
    }

    private static long prefixHash(String text, int index, long hash) {
        var pow = pow(index);
        var includedChar = text.charAt(index);
        var includedLong = charToLong(includedChar);
        hash += includedLong * pow;
        hash %= M;
        return hash;
    }

    private static long pow(int index) {
        var pow = 1L;
        if (POWS.size() > 0) {
            if (POWS.size() > index) {
                pow = POWS.get(index);
            } else {
                pow = POWS.get(POWS.size() - 1);
                pow = pow * A % M;
                POWS.add(pow);
            }
        } else {
            POWS.add(pow);
        }
        return pow;
    }

    public static long charToLong(char ch) {
        return ch - 'A' + 1;
    }
}
