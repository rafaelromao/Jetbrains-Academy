package search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class InvertedIndex {
    public static void main(String[] args) throws IOException {
        var index = Arrays.asList(args).indexOf("--data")+1;
        if (index == 0) return;
        var fileName = args[index];
        processFile(fileName);
    }

    private static void processFile(String fileName) throws IOException {
        var people = Files.readAllLines(Paths.get(fileName));
        var index = buildInvertedIndex(people);
        try (var scanner = new Scanner(System.in)) {
            while (true) {
                var option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 0:
                        return;
                    case 1:
                        searchInformation(scanner, people, index);
                        break;
                    case 2:
                        printEveryone(people);
                    default:
                        System.out.println("Incorrect option! Try again.");
                }
            }
        }
    }

    private static Map<String, List<Integer>> buildInvertedIndex(List<String> people) {
        var result = new TreeMap<String, List<Integer>>();
        for (int i = 0; i < people.size(); i++) {
            var person = people.get(i);
            var words = person.split(" ");
            for (var word: words) {
                fillIndex(result, word, i);
            }
        }
        return result;
    }

    private static void fillIndex(Map<String, List<Integer>> index, String word, int position) {
        word = word.toUpperCase();
        var positions = index.getOrDefault(word, new ArrayList<>());
        positions.add(position);
        index.put(word, positions);
    }

    private static void printEveryone(List<String> people) {
        for (var person : people) {
            System.out.println(person);
        }
    }

    private static void searchInformation(Scanner scanner, List<String> people, Map<String, List<Integer>> index) {
        var query = scanner.nextLine().toUpperCase();
        var results = index.getOrDefault(query, new ArrayList<>());
        if (results.isEmpty()) {
            System.out.println("No matching people found.");
        }
        else {
            for (var position : results) {
                var result = people.get(position);
                System.out.println(result);
            }
        }
    }
}
