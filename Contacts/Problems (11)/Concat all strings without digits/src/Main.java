import java.util.Scanner;
import java.util.stream.Stream;

class ConcatenateStringsProblem {

    public static String concatenateStringsWithoutDigits(String[] strings) {
        var sb = new StringBuilder();
        for (var s: strings) {
            for (var c: s.toCharArray()) {
                if (!Character.isDigit(c)) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] strings = Stream
                .of(scanner.nextLine().split("\\s+"))
                .toArray(String[]::new);

        String result = concatenateStringsWithoutDigits(strings);

        System.out.println(result);
    }
}