import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static final long A = 53;
    private static final long M = 1_000_000_000 + 9L;
    private static final List<Long> POWS = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try (var reader = new InputStreamReader(System.in);
             var buffer = new BufferedReader(reader)) {
            var s = buffer.readLine();
            var t = Integer.parseInt(buffer.readLine());
            var ijkns = new int[t][];
            for (var i = 0; i < t; i++) {
                ijkns[i] = Arrays.stream(buffer.readLine().split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
            var prefixHashes = prefixHashes(s);
            var count = 0;
            for (var ijkn : ijkns) {
                var hsij = substringHashWithMultiplier(prefixHashes, ijkn, 0, 1);
                var hskn = substringHashWithMultiplier(prefixHashes, ijkn, 2, 3);

                //Compensate the multipliers (p^i factors), according to https://cp-algorithms.com/string/string-hashing.html
                if (ijkn[0] < ijkn[2]) {
                    hsij *= POWS.get(ijkn[2] - ijkn[0]);
                } else {
                    hsij *= POWS.get(ijkn[0] - ijkn[2]);
                }
                hsij %= M;
                hskn %= M;

                if (hsij == hskn) count++;
            }
            System.out.println(count);
        }
    }

    private static long substringHashWithMultiplier(List<Long> prefixHashes, int[] ijkn, int ik, int jn) {
        var i = ijkn[ik];
        var j = ijkn[jn];
        if (i > j) {
            throw new IllegalArgumentException();
        }
        var hpj = prefixHashes.get(j - 1);
        var hpi = i == 0 ? 0 : prefixHashes.get(i - 1);
        var hij = (hpj - hpi + M) % M;
        return hij;
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
        hash += (includedLong * pow) % M;
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
                pow = (pow * A) % M;
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
