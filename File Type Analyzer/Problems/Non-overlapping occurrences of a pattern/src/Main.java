import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new java.util.Scanner(System.in)) {
            var s = scanner.nextLine();
            var t = scanner.nextLine();
            var r = new ArrayList<Integer>();
            if ("".equals(s)) {
                r.add(0);
            } else {
                r.addAll(KMPSearchNonOverlapping(t, s));
            }
            System.out.println(r.size());
            r.forEach(i -> System.out.print(i + " "));
        }
    }

    private static List<Integer> KMPSearchNonOverlapping(String text, String pattern) {
        var prefix = prefix(pattern);
        var occurrences = new ArrayList<Integer>();
        var j = 0;
        var i = 0;
        while (i < text.length()) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefix[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j += 1;
            }
            if (j == pattern.length()) {
                var r = i - j + 1;
                occurrences.add(r);
                j = prefix[j - 1];
                i += j + 1;
            } else {
                i++;
            }
        }
        return occurrences;
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