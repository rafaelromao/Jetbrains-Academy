import java.util.*;

class SetUtils {

    public static Set<Integer> getSetFromString(String str) {
        var result = new HashSet<Integer>();
        for (var e: str.split(" ")) {
            result.add(Integer.parseInt(e));
        }
        return result;
    }

    public static void removeAllNumbersGreaterThan10(Set<Integer> set) {
        set.removeIf(e -> e > 10);
    }

}

/* Do not change code below */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String numbers = scanner.nextLine();
        Set<Integer> set = SetUtils.getSetFromString(numbers);
        SetUtils.removeAllNumbersGreaterThan10(set);
        set.forEach(e -> System.out.print(e + " "));
    }
}