import java.util.*;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var list = new ArrayList<>(Arrays.asList(scanner.nextLine().split(" ")));
            var bit = 0;
            for (var i = 0; i < list.size(); i++) {
                if (i % 2 == bit) {
                    list.remove(i);
                }
                bit = (bit + 1) % 2;
            }
            for (var j = list.size() - 1; j >= 0; j--) {
                System.out.print(list.get(j) + " ");
            }
        }
    }
}
