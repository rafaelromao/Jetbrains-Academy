package analyzer;

import java.io.IOException;
import java.io.InputStream;

public interface SearchStrategy {
    boolean search(InputStream content, SearchPattern pattern) throws IOException;
}
