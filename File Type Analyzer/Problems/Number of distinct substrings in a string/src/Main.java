public class Main {
    public static void main(String[] args) {
        try (var scanner = new java.util.Scanner(System.in)) {
            var s = scanner.nextLine();
            var r = countSubstrings(s);
            System.out.println(r);
        }
    }

    private static int countSubstrings(String str) {
        var count = 1;
        var t = "";
        for (var i = str.length() - 1; i >= 0; i--) {
            var s = t;
            var c = str.charAt(i);
            t = c + s;
            var n = s.length() + 1 - pMax(t);
            count += n;
        }
        return count;
    }

    private static int pMax(String t) {
        var p = prefix(t);
        var n = 0;
        for (var j = 0; j < p.length; j++) {
            n = Math.max(n, p[j]);
        }
        return n;
    }

    private static int[] prefix(String str) {
        var prefix = new int[str.length()];
        for (var i = 1; i < str.length(); i++) {
            var j = prefix[i - 1];
            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefix[j - 1];
            }
            if (str.charAt(i) == str.charAt(j)) {
                j += 1;
            }
            prefix[i] = j;
        }
        return prefix;
    }
}