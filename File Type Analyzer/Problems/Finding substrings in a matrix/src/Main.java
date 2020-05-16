import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Task {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var pattern = readMatrix(scanner);
            var matrix = readMatrix(scanner);
            var occurrences = findOccurrences(matrix, pattern);
            System.out.println(occurrences.size());
        }
    }

    private static char[][] readMatrix(Scanner scanner) {
        var n = scanner.nextInt();
        var m = scanner.nextInt();
        scanner.nextLine();
        var matrix = new char[n][m];
        for (var i = 0; i < n; i++) {
            matrix[i] = scanner.nextLine().toCharArray();
        }
        return matrix;
    }

    private static List<Integer[]> findOccurrences(char[][] matrix, char[][] pattern) {
        var prefix = prefix(pattern);
        var occurrences = new ArrayList<Integer[]>();
        if (matrix.length == 0 || pattern.length == 0) {
            return occurrences;
        }
        var indexInPattern = 0;
        // Loop through the matrix searching for the pattern
        for (var indexInMatrix = 0; indexInMatrix < length(matrix); indexInMatrix++) {

            var patternWidth = pattern[0].length;
            var startLine = line(matrix, indexInMatrix);
            var startColumn = column(matrix, indexInMatrix);
            // If the pattern does not fit in the remaining lines or columns, no need to search
            if (startLine > matrix.length - pattern.length ||
                startColumn > matrix[0].length - patternWidth) {
                continue;
            }
            // Get the char in a sub matrix of the size of the pattern
            var charAtSubMatrix = charAt(
                    matrix,
                    indexInPattern,
                    startLine,
                    startColumn,
                    patternWidth);
            // If indexInMatrix does not match the pattern, move to the next indexInMatrix using the prefix
            while (indexInPattern > 0 && charAtSubMatrix != charAt(pattern, indexInPattern)) {
                indexInPattern = prefix[indexInPattern - 1];
            }
            // If the indexInMatrix match the pattern, increase the indexInPattern counter
            if (charAtSubMatrix == charAt(pattern, indexInPattern)) {
                indexInPattern += 1;
            }
            // Check if found the pattern
            if (indexInPattern == length(pattern)) {
                var r = indexInMatrix - indexInPattern - 1;
                var l = line(matrix, r);
                var c = column(matrix, r);
                occurrences.add(new Integer[]{l, c});
                indexInPattern = prefix[indexInPattern - 1];
            }

        }
        return occurrences;
    }

    private static int length(char[][] matrix) {
        var n = matrix.length;
        var m = matrix[0].length;
        return n * m;
    }

    private static char charAt(char[][] matrix, int linearIndex) {
        return charAt(matrix, linearIndex, 0, 0, 0);
    }

    private static char charAt(char[][] matrix, int linearIndex, int startLine, int startColumn, int width) {
        var line = line(matrix, linearIndex, startLine, width);
        var column = column(matrix, linearIndex, startColumn, width);
        return matrix[line][column];
    }

    private static int line(char[][] matrix, int linearIndex) {
        return line(matrix, linearIndex, 0, 0);
    }

    private static int line(char[][] matrix, int linearIndex, int start, int width) {
        var m = width == 0 ? matrix[0].length : width;
        var line = linearIndex / m;
        return line + start;
    }

    private static int column(char[][] matrix, int linearIndex) {
        return column(matrix, linearIndex, 0, 0);
    }

    private static int column(char[][] matrix, int linearIndex, int start, int width) {
        var m = width == 0 ? matrix[0].length : width;
        var column = linearIndex % m;
        return column + start;
    }

    private static int[] prefix(char[][] str) {
        var prefix = new int[length(str)];
        for (var i = 1; i < length(str); i++) {
            var j = prefix[i - 1];
            while (j > 0 && charAt(str, i) != charAt(str, j)) {
                j = prefix[j - 1];
            }
            if (charAt(str, i) == charAt(str, j)) {
                j += 1;
            }
            prefix[i] = j;
        }
        return prefix;
    }
}
