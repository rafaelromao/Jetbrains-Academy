import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Task {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var p = scanner.nextLine();
            var n = scanner.nextInt();
            scanner.nextLine();
            var m = scanner.nextInt();
            var matrix = new char[n][m];
            for (var i = 0; i < n; i++) {
                matrix[i] = scanner.nextLine().toCharArray();
            }
            var occurrences = KMPSearch(matrix, p);
            System.out.println(occurrences.size());
            occurrences.stream().sorted(Comparator
                    .comparingInt((Integer[] a) -> a[0])
                    .thenComparingInt(a -> a[1]))
            .forEach(a -> System.out.println(a[0] + " " + a[1]));
        }
    }

    private static List<Integer[]> KMPSearch(char[][] matrix, String pattern) {
        var prefix = prefix(pattern);
        var occurrences = new ArrayList<Integer[]>();
        if (matrix.length == 0) {
            return occurrences;
        }
        var n = matrix.length;
        var m = matrix[0].length;
        var j = 0;
        for (var i = 0; i < n * m; i++) {
            var line = i / m;
            var column = i % m;
            while (j > 0 && matrix[line][column] != pattern.charAt(j)) {
                j = prefix[j - 1];
            }
            if (matrix[line][column] == pattern.charAt(j)) {
                j += 1;
            }
            if (j == pattern.length()) {
                var r = i - j - 1;
                occurrences.add(new Integer[] { r / m, r % m });
                j = prefix[j - 1];
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
