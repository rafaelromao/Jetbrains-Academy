package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

public class Main {
    private static final String UNKNOWN_FILE_TYPE = "Unknown file type";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var folder = args[0];
        var pattern = args[1];
        var type = args[2];
        var searchStrategy = new KMPSearchStrategy(type);
        var executor = Executors.newCachedThreadPool();
        var analyzer = new Analyzer(searchStrategy);
        var files = new File(folder);
        var searches = Arrays.stream(files.listFiles())
                .<Callable<String>>map(file ->
                        () -> {
                            try (var stream = new FileInputStream(file)) {
                                var result = analyzer.search(stream, pattern);
                                result = result == null ? UNKNOWN_FILE_TYPE : result;
                                return file.getName() + ": " + result;
                            }
                        }
                )
                .collect(toList());
        for (var future : executor.invokeAll(searches)) {
            System.out.println(future.get());
        }
        System.out.println();
    }
}
