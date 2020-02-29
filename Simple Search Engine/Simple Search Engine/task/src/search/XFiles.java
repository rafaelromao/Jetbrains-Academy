package search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

public class XFiles {
    public static void main(String[] args) throws IOException {
        var index = Arrays.asList(args).indexOf("--data")+1;
        if (index == 0) return;
        var fileName = args[index];
        processFile(fileName);
    }

    private static void processFile(String fileName) throws IOException {
        var people = Files.readAllLines(Paths.get(fileName));
        try (var scanner = new Scanner(System.in)) {
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

    private static void printEveryone(List<String> people) {
        for (var person : people) {
            System.out.println(person);
        }
    }

    private static void searchInformation(Scanner scanner, List<String> people) {
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
