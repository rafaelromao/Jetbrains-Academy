package analyzer;

import java.io.IOException;
import java.io.InputStream;

public interface SearchStrategy {
    String search(InputStream content, String pattern) throws IOException;
}
