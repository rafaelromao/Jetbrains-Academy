package converter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

class JSON2XMLConverter implements Converter {
    private Pattern contentPattern = Pattern.compile("\\s*[\\[\\{]\\s*(.*)\\s*[\\}\\]]\\s*");
    private Pattern propertyNamePattern = Pattern.compile("\\s*\"([\\w|@|#]*)\"\\s*:\\s*");
    private Pattern propertyValuePattern = Pattern.compile("\\s*:\\s*\"*(.*)[$|\"?\\s*]");
    private Pattern separatorPattern = Pattern.compile("(\\\"\\w*\\\"\\s*)(:)\\s*([\\{\\[\\\"]|\\bnull\\b|\\d+)");
    private StringBuilder builder = new StringBuilder();
    private PrintStream out;

    @Override
    public String convert(String content) {
        var properties = splitContent(content);
        var keyValuePair = readProperty(properties.get(0));
        writeRecursively(keyValuePair[0], keyValuePair[1]);
        return builder.toString();
    }

    @Override
    public void logTo(PrintStream out) {
        this.out = out;
    }

    private List<String> splitContent(String content) {
        content = readContent(content);
        var result = new ArrayList<String>();
        var matcher = separatorPattern.matcher(content);
        var start = 0;
        while (true) {
            matcher.find(start);
            start = matcher.start(3);
            var property = readContent(
                    matcher.group(1),
                    content,
                    start);
            if (property != null) {
                result.add(property);
            } else {
                break;
            }
        }
        return result;
    }

    private String readContent(String name, String content, int valueStart) {
        String value;
        switch (ElementType.of(content.substring(valueStart, valueStart + 1))) {
            case OBJECT:
                value = readUntilMatching('}', content, valueStart);
                break;
            case ARRAY:
                value = readUntilMatching(']', content, valueStart);
                break;
            case STRING:
                value = readUntilMatching('"', content, valueStart);
                break;
            default:
                value = readUntilMatching(',', content + ",", valueStart);
                break;
        }
        if (value == null) {
            return null;
        }
        return String.format("%s:%s", name, value);
    }

    private String readUntilMatching(char enclosingChar, String content, int valueStart) {
        boolean inArray = enclosingChar == '}';
        boolean inObject = enclosingChar == ']';
        int openObjects = 0;
        int openArrays = 0;
        for (int index = valueStart + 1; index < content.length(); index++) {
            char currentChar = content.charAt(index);
            if (currentChar == '[') openArrays++;
            if (currentChar == ']') openArrays--;
            if (currentChar == '{') openObjects++;
            if (currentChar == '}') openObjects--;
            if (!inArray && !inObject && (currentChar == '[' || currentChar == '{')) {
                return null;
            }
            if (currentChar == enclosingChar && openObjects == 0 && openArrays == 0) {
                return content.substring(valueStart, index);
            }
        }
        return null;
    }

    private String readContent(String content) {
        var matcher = contentPattern.matcher(content);
        matcher.find();
        return matcher.group(1).trim();
    }

    private String[] readProperty(String content) {
        content = !content.strip().endsWith("\"") ? content.strip() + "\n" : content.strip();
        var keyMatcher = propertyNamePattern.matcher(content);
        keyMatcher.find();
        var key = keyMatcher.group(1);
        var valueMatcher = propertyValuePattern.matcher(content);
        valueMatcher.find();
        var value = valueMatcher.group(1).strip();
        value = "null".equals(value) ? null : value;
        return new String[]{key, value};
    }

    private void writeRecursively(String name, String value, String... attributes) {
        var elementType = ElementType.of(value);
        switch (elementType) {
            case LITERAL:
            case STRING:
                writeLiteral(name, value, attributes);
                break;
            case OBJECT:
            case ARRAY:
                writeElement(name, value);
        }
    }

    private void writeElement(String name, String value) {
        var properties = splitContent(value);
        var content = properties.stream()
                .filter(p -> p.startsWith("\"#"))
                .findAny();
        var attributes = properties.stream()
                .filter(p -> p.startsWith("\"@"))
                .toArray(String[]::new);
        var elements = properties.stream()
                .filter(p -> !p.startsWith("\"@") && !p.startsWith("\"#"))
                .toArray(String[]::new);
        if (!content.isPresent() && elements.length == 0) {
            writeSimpleElement(name, attributes);
        } else {
            writeBeginElement(name, attributes);
            if (content.isPresent()) {
                var keyValuePair = readProperty(content.get());
                var contentValue = keyValuePair[1];
                if (ElementType.of(contentValue) == ElementType.LITERAL) {
                    writeValue(contentValue);
                } else {
                    var contentProperty = splitContent(keyValuePair[1]).get(0);
                    keyValuePair = readProperty(contentProperty);
                    writeRecursively(keyValuePair[0], keyValuePair[1]);
                }
            }
            for (var element : elements) {
                var keyValuePair = readProperty(element);
                writeRecursively(keyValuePair[0], keyValuePair[1]);
            }
            writeEndElement(name);
        }
    }

    private void writeLiteral(String name, String value, String... attributes) {
        if (value == null || value.length() == 0) {
            writeSimpleElement(name, attributes);
        } else {
            writeBeginElement(name, attributes);
            writeValue(value);
            writeEndElement(name);
        }
    }

    private void writeBeginElement(String elementName, String... attributes) {
        builder.append("<");
        builder.append(elementName.startsWith("#") ? elementName.substring(1) : elementName);
        writeAttributes(attributes);
        builder.append(">");
    }

    private void writeEndElement(String elementName) {
        builder.append("</");
        builder.append(elementName.startsWith("#") ? elementName.substring(1) : elementName);
        builder.append(">");
    }

    private void writeSimpleElement(String elementName, String... attributes) {
        builder.append("<");
        builder.append(elementName.startsWith("#") ? elementName.substring(1) : elementName);
        writeAttributes(attributes);
        builder.append("/>");
    }

    private void writeAttributes(String[] attributes) {
        if (attributes.length > 0) {
            builder.append(" ");
            for (var i = 0; i < attributes.length; i++) {
                String attribute = attributes[i];
                writeAttribute(attribute);
                if (i < attributes.length - 1) {
                    builder.append(" ");
                }
            }
        }
    }

    private void writeAttribute(String attribute) {
        var keyValuePair = attribute.replace("\"", "").split(":");
        builder.append(keyValuePair[0].strip().substring(1));
        builder.append(" = ");
        builder.append("\"");
        builder.append(keyValuePair[1].strip());
        builder.append("\"");
    }

    private void writeValue(String value) {
        builder.append(value == null ? "" : value);
    }

    private enum ElementType {
        OBJECT,
        ARRAY,
        STRING,
        LITERAL;

        public static ElementType of(String elementValue) {
            if (elementValue.charAt(0) == '{') return ElementType.OBJECT;
            if (elementValue.charAt(0) == '[') return ElementType.ARRAY;
            if (elementValue.charAt(0) == '"') return ElementType.STRING;
            return ElementType.LITERAL;
        }
    }
}
