package converter;

import java.util.regex.Pattern;

class JSONReader {
    private Pattern objectPattern = Pattern.compile("\\s*\\{\\s*(.*)\\s*\\}\\s*");
    private Pattern propertyNamePattern = Pattern.compile("\\s*\"(\\w*)\"\\s*:\\s*");
    private Pattern propertyValuePattern = Pattern.compile("\\s*:\\s*\"*([^\\\"|}]*)\"*\\s*");

    String readObject(String content) {
        var objectMatcher = objectPattern.matcher(content);
        objectMatcher.find();
        return objectMatcher.group(1);
    }

    String[] readProperty(String content) {
        var keyMatcher = propertyNamePattern.matcher(content);
        keyMatcher.find();
        var key = keyMatcher.group(1);
        var valueMatcher = propertyValuePattern.matcher(content);
        valueMatcher.find();
        var value = valueMatcher.group(1);
        value = "null".equals(value) ? null : value;
        return new String[]{key, value};
    }
}
