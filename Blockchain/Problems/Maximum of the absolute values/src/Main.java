import java.util.*;

public class Main {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            System.out.println(Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(s -> Integer.parseInt(s))
                    .map(i -> Math.abs(i))
                    .max().getAsInt());
        }
    }
}