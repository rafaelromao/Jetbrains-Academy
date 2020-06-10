package converter;

import java.util.regex.Pattern;

class XML2JSONConverter implements Converter {
    private Pattern elementPattern = Pattern.compile("\\s*\\<(.*?)\\/\\>\\s*");
    private Pattern elementNamePattern = Pattern.compile("\\s*\\<(.*?)\\>\\s*");
    private Pattern elementValuePattern = Pattern.compile("\\>(.*)\\<");
    private StringBuilder builder = new StringBuilder();

    @Override
    public String convert(String content) {
        var element = readElement(content);
        writeBeginObject();
        writeProperty(element[0], element[1]);
        writeEndObject();
        return builder.toString();
    }

    private String[] readElement(String content) {
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

    private void writeBeginObject() {
        builder.append("{");
    }

    private void writeEndObject() {
        builder.append("}");
    }

    private void writeProperty(String key, String value) {
        writeString(key);
        builder.append(":");
        writeString(value);
    }

    private void writeString(String value) {
        if (value == null || value.length() == 0) {
            builder.append("null");
        } else {
            builder.append("\"");
            builder.append(value);
            builder.append("\"");
        }
    }
}
