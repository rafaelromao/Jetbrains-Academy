import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            scanner.nextLine();
            System.out.println(IntStream.range(0, n)
                    .mapToObj(i -> scanner.nextLine().split("\\s+"))
                    .flatMap(Stream::of)
                    .map(String::toLowerCase)
                    .distinct()
                    .count());
        }
    }
}