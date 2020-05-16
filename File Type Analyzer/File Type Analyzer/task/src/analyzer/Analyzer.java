package analyzer;

import java.io.*;

public class Analyzer {
    private SearchStrategy searchStrategy;

    public Analyzer(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public String search(InputStream content, String pattern) throws IOException {
        return searchStrategy.search(content, pattern);
    }
}
