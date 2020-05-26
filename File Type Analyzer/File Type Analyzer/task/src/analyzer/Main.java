package analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        var folder = args[0];
        var pattern_db = args[1];
        var patterns = Files
                .readAllLines(Path.of(pattern_db))
                .stream()
                .map(SearchPattern::parse)
                .collect(toList());
        var searchStrategy = new KMPSearchStrategy();
        var analyzer = new Analyzer(searchStrategy);
        var files = new File(folder);

        var results = analyzer.search(files.listFiles(), patterns);

        for (var result : results) {
            System.out.println(result);
        }
        System.out.println();
    }
}
