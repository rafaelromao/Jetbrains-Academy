import java.math.BigInteger;
import java.util.*;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            scanner.nextLine();
            var table = new int[n][n][n][n];
            var solved = true;
            final var targetSum = LongStream
                    .range(1, n * n + 1)
                    .mapToObj(i -> BigInteger.valueOf((long) Math.pow(2, i)))
                    .reduce((i, j) -> i.add(j))
                    .get();
            mainLoop:
            for (var fullSquareLine = 0; fullSquareLine < n * n; fullSquareLine++) {
                // Check each line of the full square
                var fullSquareLineSum = BigInteger.valueOf(0);
                var lineValues = scanner.nextLine().split(" ");
                var lineOfSquare = fullSquareLine / n;
                var lineInSquare = fullSquareLine % n;
                for (var fullSquareColumn = 0; fullSquareColumn < n * n; fullSquareColumn++) {
                    var columnOfSquare = fullSquareColumn / n;
                    var columnInSquare = fullSquareColumn % n;
                    var item = Integer.parseInt(lineValues[fullSquareColumn]);
                    table[lineOfSquare][columnOfSquare][lineInSquare][columnInSquare] = item;
                    fullSquareLineSum = fullSquareLineSum.add(BigInteger.valueOf((long)Math.pow(2, item)));
                }
                solved = fullSquareLineSum.equals(targetSum);
                if (!solved) {
                    break mainLoop;
                }
                // When we scan lines enough to cover a line of squares, check the squares in that line
                if ((fullSquareLine+1) % n == 0) {
                    for (var columnOfSquare2 = 0; columnOfSquare2 < n; columnOfSquare2++) {
                        var lineOfSquare2 = fullSquareLine / n;
                        var squareSum = BigInteger.valueOf(0);
                        for (var columnInSquare2 = 0; columnInSquare2 < n; columnInSquare2++) {
                            for (var lineInSquare2 = 0; lineInSquare2 < n; lineInSquare2++) {
                                var item = table[lineOfSquare2][columnOfSquare2][lineInSquare2][columnInSquare2];
                                squareSum = squareSum.add(BigInteger.valueOf((long)Math.pow(2, item)));
                            }
                        }
                        solved = squareSum.equals(targetSum);
                        if (!solved) {
                            break mainLoop;
                        }
                    }
                }
            }
            // When we finish scanning all lines, check the columns
            for (var fullSquareColumn3 = 0; fullSquareColumn3 < n * n; fullSquareColumn3++) {
                // Check each column of the full square
                var fullSquareColumnSum = BigInteger.valueOf(0);
                var columnOfSquare3 = fullSquareColumn3 / n;
                var columnInSquare3 = fullSquareColumn3 % n;
                for (var fullSquareLine3 = 0; fullSquareLine3 < n * n; fullSquareLine3++) {
                    var lineOfSquare3 = fullSquareLine3 / n;
                    var lineInSquare3 = fullSquareLine3 % n;
                    var item = table[lineOfSquare3][columnOfSquare3][lineInSquare3][columnInSquare3];
                    fullSquareColumnSum = fullSquareColumnSum.add(BigInteger.valueOf((long)Math.pow(2, item)));
                }
                solved = fullSquareColumnSum.equals(targetSum);
                if (!solved) {
                    break;
                }
            }
            System.out.println(solved ? "YES" : "NO");
        }
    }
}