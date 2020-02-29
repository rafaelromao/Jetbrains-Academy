package search;

import java.util.ArrayList;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

public class ExpandAndSearch {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var numberOfPeople = scanner.nextInt();
            scanner.nextLine();
            var people = new ArrayList<String>();
            for (var i = 0; i < numberOfPeople; i++){
                people.add(scanner.nextLine());
            }
            var numberOfQueries = scanner.nextInt();
            scanner.nextLine();
            for (var i = 0; i < numberOfQueries; i++){
                var query = scanner.nextLine().toUpperCase();
                var results = people
                        .stream()
                        .filter(p -> p.toUpperCase().contains(query))
                        .collect(toList());
                if (results.isEmpty()) {
                    System.out.println("No matching people found.");
                }
                else {
                    for (var result : results) {
                        System.out.println(result);
                    }
                }
            }
        }
    }
}
