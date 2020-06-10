package converter;

import java.util.regex.Pattern;

public class XMLReader {
    private Pattern elementPattern = Pattern.compile("\\s*\\<(.*?)\\/\\>\\s*");
    private Pattern elementNamePattern = Pattern.compile("\\s*\\<(.*?)\\>\\s*");
    private Pattern elementValuePattern = Pattern.compile("\\>(.*)\\<");

    String[] readElement(String content) {
        var elementMatcher = elementPattern.matcher(content);
        if (elementMatcher.find()) {
            return new String[] { elementMatcher.group(1), null };
        } else {
            var keyMatcher = elementNamePattern.matcher(content);
            keyMatcher.find();
            var key = keyMatcher.group(1);
            var valueMatcher = elementValuePattern.matcher(content);
            valueMatcher.find();
            var value = valueMatcher.group(1);
            return new String[]{key, value};
        }
    }
}
