package analyzer;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class Analyzer {
    private SearchStrategy searchStrategy;

    public Analyzer(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public SearchResult search(File file, SearchPattern pattern) throws IOException {
        try (var stream = new FileInputStream(file)) {
            var result = searchStrategy.search(stream, pattern);
            return new SearchResult(pattern, file, result);
        }
    }

    public List<SearchResult> search(File[] files, List<SearchPattern> patterns) throws InterruptedException {
        var executor = Executors.newCachedThreadPool();
        var searches = Arrays.stream(files)
                .<Callable<SearchResult>>flatMap(file ->
                        patterns.stream()
                                .map(pattern -> () -> search(file, pattern)))
                .collect(toList());

        var foundResults = executor.invokeAll(searches)
                .stream()
                .<Optional<SearchResult>>map(future -> {
                    try {
                        return Optional.of(future.get());
                    } catch (Exception e) {
                        return Optional.empty();
                    }
                });

        var notFoundResults = Arrays.stream(files)
                .flatMap(file -> patterns.stream()
                        .map(pattern -> Optional.of(new SearchResult(
                                new SearchPattern(
                                        0,
                                        pattern.getPattern(),
                                        "Unknown file type"),
                                file,
                                true))));

        var allResults = Stream.concat(foundResults, notFoundResults);

        var selectedResults = allResults
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(SearchResult::isFound)
                .collect(
                        groupingBy(SearchResult::getFile,
                                maxBy(
                                        comparingInt(SearchResult::getPriority))));
        return selectedResults
                .values()
                .stream()
                .map(Optional::get)
                .collect(toList());
    }
}
