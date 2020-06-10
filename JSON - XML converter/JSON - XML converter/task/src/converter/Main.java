package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var input = scanner.nextLine().strip();
            var converter = Converter.Factory.createFor(input);
            var output = converter.convert(input);
            System.out.println(output);
        }
    }
}
