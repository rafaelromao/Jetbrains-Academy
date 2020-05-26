package analyzer;

import java.io.File;

public class SearchResult {
    private SearchPattern pattern;
    private File file;
    private boolean found;

    public SearchResult(SearchPattern pattern, File file, boolean found) {
        this.pattern = pattern;
        this.file = file;
        this.found = found;
    }

    public File getFile() {
        return file;
    }

    public int getPriority() {
        return pattern.getPriority();
    }

    public boolean isFound() {
        return found;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", file.getName(), pattern.getType());
    }

}
