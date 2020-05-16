package analyzer;

import java.io.FileInputStream;
import java.io.IOException;

import static java.lang.System.nanoTime;

public class Main {
    private static final String UNKNOWN_FILE_TYPE = "Unknown file type";

    public static void main(String[] args) {
        var strategy = args[0];
        var fileName = args[1];
        var pattern = args[2];
        var type = args[3];

        SearchStrategy searchStrategy;
        switch (strategy) {
            case "--naive":
                searchStrategy = new NaiveSearchStrategy(type);
                break;
            case "--KMP":
                searchStrategy = new KMPSearchStrategy(type);
                break;
            default:
                throw new IllegalArgumentException("Invalid search strategy: " + strategy);
        }

        var analyzer = new Analyzer(searchStrategy);
        try (var file = new FileInputStream(fileName)) {
            var start = nanoTime();
            var result = analyzer.search(file, pattern);
            result = result == null ? UNKNOWN_FILE_TYPE : result;
            var duration = nanoTime() - start;
            System.out.println(result);
            System.out.println("It took " + (double) duration / 1_000_000_000.0 + " seconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
