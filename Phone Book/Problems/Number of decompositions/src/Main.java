import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var result = decompose(List.of(List.of(scanner.nextInt())));
            for (var list: result) {
                System.out.print(list);
            }
        }
    }

    private static List<List<Integer>> decompose(List<List<Integer>> decompositions) {
        var result = new ArrayList<List<Integer>>();
        result.addAll(decompositions);
        var last = decompositions.get(decompositions.size()-1);
        // Find all decompositions of the last decomposition
        var newDecompositions = decompose(last);
        result.addAll(newDecompositions);
        return result;
    }

    private static List<List<Integer>> decompose(List<Integer> decomposition) {

    }
}