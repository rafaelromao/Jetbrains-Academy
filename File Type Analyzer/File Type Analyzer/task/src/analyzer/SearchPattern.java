package analyzer;

public class SearchPattern {
    private int priority;
    private String pattern;
    private String type;

    public static SearchPattern parse(String serializedSearchPattern) {
        var parts = serializedSearchPattern.split(";");
        var searchPattern = new SearchPattern(
                Integer.parseInt(parts[0]),
                parts[1].replace("\"", ""),
                parts[2].replace("\"", ""));
        return searchPattern;
    }

    public SearchPattern(int priority, String pattern, String type) {
        this.priority = priority;
        this.pattern = pattern;
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getType() {
        return type;
    }
}
