import java.util.*;

public class Main {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            Arrays.stream(scanner.nextLine().split(" "))
                .mapToInt(s -> Integer.parseInt(s))
                .map(i -> Math.abs(i))
                .sorted()
                .forEach(i -> System.out.print(i + " "));
        }
    }
}