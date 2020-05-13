import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Task {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String p = scanner.nextLine();
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            scanner.nextLine();
            char[][] matrix = new char[n][m];
            for (int i = 0; i < n; i++) {
                matrix[i] = scanner.nextLine().toCharArray();
            }
            List<Integer[]> occurrences = KMPSearch(matrix, p);
            System.out.println(occurrences.size());
            occurrences.stream().sorted(Comparator
                    .comparingInt((Integer[] a) -> a[0])
                    .thenComparingInt(a -> a[1]))
            .forEach(a -> System.out.println(a[0] + " " + a[1]));
        }
    }

    private static List<Integer[]> KMPSearch(char[][] matrix, String pattern) {
        int[] prefix = prefix(pattern);
        ArrayList<Integer[]> occurrences = new ArrayList<Integer[]>();
        if (matrix.length == 0) {
            return occurrences;
        }
        int n = matrix.length;
        int m = matrix[0].length;
        int j = 0;
        for (int i = 0; i < n * m; i++) {
            int line = i / m;
            int column = i % m;
            while (j > 0 && matrix[line][column] != pattern.charAt(j)) {
                j = prefix[j - 1];
            }
            if (matrix[line][column] == pattern.charAt(j)) {
                j += 1;
            }
            if (j == pattern.length()) {
                int r = i - j + 1;
                occurrences.add(new Integer[] { r / m, r % m });
                j = prefix[j - 1];
            }
        }
        return occurrences;
    }

    private static int[] prefix(String str) {
        int[] prefix = new int[str.length()];
        for (int i = 1; i < str.length(); i++) {
            int j = prefix[i - 1];
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
