package search;

import java.util.ArrayList;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

public class UserMenu {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var numberOfPeople = scanner.nextInt();
            scanner.nextLine();
            var people = new ArrayList<String>();
            for (var i = 0; i < numberOfPeople; i++){
                people.add(scanner.nextLine());
            }
            while (true) {
                var option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 0:
                        return;
                    case 1:
                        searchInformation(scanner, people);
                        break;
                    case 2:
                        printEveryone(people);
                    default:
                        System.out.println("Incorrect option! Try again.");
                }
            }
        }
    }

    private static void printEveryone(ArrayList<String> people) {
        for (var person : people) {
            System.out.println(person);
        }
    }

    private static void searchInformation(Scanner scanner, ArrayList<String> people) {
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
