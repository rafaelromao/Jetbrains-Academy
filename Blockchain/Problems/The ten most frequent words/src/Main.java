import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var text = scanner.nextLine();
            var words = text.split("\\b");
            Arrays.stream(words)
                    .filter(w -> w.matches("\\w+"))
                    .map(String::toLowerCase)
                    .collect(groupingBy(identity(), counting()))
                    .entrySet()
                    .stream()
                    .sorted((e1, e2) -> {
                        var result = Long.compare(e2.getValue(), e1.getValue());
                        return result == 0 ? e1.getKey().compareTo(e2.getKey()) : result;
                    })
                    .map(Map.Entry::getKey)
                    .limit(10)
                    .forEach(System.out::println);
        }
    }
}
