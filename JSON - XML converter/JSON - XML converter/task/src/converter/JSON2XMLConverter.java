package converter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class JSON2XMLConverter implements Converter {
    private Pattern contentPattern = Pattern.compile("\\s*[\\[\\{]?\\s*(.*)\\s*[\\}\\]]?\\s*", Pattern.DOTALL);
    private Pattern propertyNamePattern = Pattern.compile("\\s*\"([\\w@#.-_]*)\"\\s*:\\s*");
    private Pattern separatorPattern = Pattern.compile("(\\\"[#@]?[\\w.-_]*\\\"\\s*)(:)\\s*([\\{\\[\\\"]|\\bnull\\b|\\d+)");
    private StringBuilder builder = new StringBuilder();
    private PrintStream out;

    @Override
    public String convert(String content) {
        var properties = splitContent(content.replace('\n', ' '));
        var keyValuePair = readProperty(properties.get(0));
        writeRecursively(null, keyValuePair[0], keyValuePair[1]);
        return builder.toString();
    }

    @Override
    public void logTo(PrintStream out) {
        this.out = out;
    }

    private void println(String fmt, Object... params) {
        if (out != null) {
            out.printf(fmt + "\n", params);
        }
    }

    private void logElement(String path, String name, Optional<String> value, String... attributes) {
        if (!name.startsWith("@")) {
            println("Element:");
            println("path = %s", path);
            if (value != null) {
                var elementType = JSON2XMLConverter.ElementType.of(value.orElse(null));
                switch (elementType) {
                    case STRING:
                    case LITERAL:
                        println("value = %s", value.orElse(null));
                        break;
                }
            }
            if (attributes != null && attributes.length > 0) {
                println("attributes:");
                for (var attribute : attributes) {
                    var attr = readProperty(attribute);
                    println("%s = %s", attr[0].substring(1), attr[1]);
                }
            }
            println("");
        }
    }

    private List<String> splitContent(String content) {
        content = readContent(content);
        var result = new ArrayList<String>();
        var matcher = separatorPattern.matcher(content);
        var start = 0;
        while (start < content.length() && matcher.find(start)) {
            var property = readContent(
                    matcher.group(1),
                    content,
                    matcher.start(3));
            if (property != null) {
                start = matcher.start(1) + property.length();
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
                value = readUntilMatching("}", content, valueStart);
                break;
            case ARRAY:
                value = readUntilMatching("]", content, valueStart);
                break;
            case STRING:
                value = readUntilMatching("\"", content, valueStart);
                break;
            default:
                value = readUntilMatching(",}]", content + ",", valueStart);
                break;
        }
        value = value.strip();
        return String.format("%s:%s", name, value);
    }

    private String readUntilMatching(String enclosingChars, String content, int valueStart) {
        boolean inArray = enclosingChars.equals("]");
        boolean inObject = enclosingChars.equals("}");
        boolean inString = enclosingChars.equals("\"");
        int openObjects = 0;
        int openArrays = 0;
        for (int index = valueStart; index < content.length(); index++) {
            if (index == valueStart && inString) continue;
            char currentChar = content.charAt(index);
            if (currentChar == '[') openArrays++;
            if (currentChar == ']') openArrays--;
            if (currentChar == '{') openObjects++;
            if (currentChar == '}') openObjects--;
            if (!inArray && !inObject && (currentChar == '[' || currentChar == '{')) {
                return null;
            }
            if (enclosingChars.indexOf(currentChar) > -1 && openObjects <= 0 && openArrays <= 0) {
                return enclosingChars.contains(",")
                        ? content.substring(valueStart, index)
                        : content.substring(valueStart, index + 1);
            }
        }
        return content.substring(valueStart);
    }

    private String readContent(String content) {
        if (content.length() == 0) return content;
        var matcher = contentPattern.matcher(content);
        matcher.find();
        return matcher.group(1).trim();
    }

    private String[] readProperty(String content) {
        content = !content.strip().endsWith("\"") ? content.strip() + "\n" : content.strip();
        var keyMatcher = propertyNamePattern.matcher(content);
        keyMatcher.find();
        var key = keyMatcher.group(1);
        var separatorMatcher = separatorPattern.matcher(content);
        separatorMatcher.find();
        var value = content.substring(separatorMatcher.start(3)).strip();
        return new String[]{key, value};
    }

    private void writeRecursively(String parentPath, String name, String value) {
        if (name.strip().length() == 0) return;
        var elementType = ElementType.of(value);
        switch (elementType) {
            case LITERAL:
            case STRING:
                writeElementWithSimpleContent(parentPath, name, value);
                break;
            case OBJECT:
            case ARRAY:
                writeElementWithComplexContent(parentPath, name, value);
        }
    }

    private void writeElementWithComplexContent(String parentPath, String name, String value) {
        var properties = splitContent(value);

        var invalids = properties.stream()
                .filter(p -> hasInvalidContent(name, p) ||
                        hasInvalidAttribute(p) ||
                        hasInvalidElement(p))
                .collect(toList());

        properties = invalids.isEmpty() ? properties : properties.stream()
                .filter(p -> !hasInvalidContent(p))
                .filter(p -> !hasInvalidAttribute(p))
                .filter(p -> !hasInvalidElement(p))
                .map(p -> fixInvalidContent(p))
                .map(p -> fixInvalidAttributes(p))
                .collect(toList());

        var content = properties.stream()
                .filter(p -> p.startsWith("\"#" + name + "\":"))
                .findAny();
        var attributes = properties.stream()
                .filter(p -> p.startsWith("\"@"))
                .map(p -> p.replace("\"@", "\""))
                .toArray(String[]::new);
        var elements = properties.stream()
                .filter(p -> !p.startsWith("\"@") && !hasContent(p))
                .toArray(String[]::new);
        if (content.isPresent()) {
            var keyValuePair = readProperty(content.get());
            if (keyValuePair[1] == null) {
                content = Optional.empty();
            }
        }

        var path = computePath(parentPath, name);
        if (content.isPresent()) {
            var contentValue = readProperty(content.get())[1];
            logElement(path, name, Optional.ofNullable(contentValue), attributes);
        } else {
            logElement(path, name, null, attributes);
        }

        if (!content.isPresent() && elements.length == 0) {
            writeSimpleElement(name, attributes);
        } else {
            writeBeginElement(name, attributes);
            if (content.isPresent()) {
                var keyValuePair = readProperty(content.get());
                var elementType = ElementType.of(keyValuePair[1]);
                if (elementType == ElementType.LITERAL || elementType == ElementType.STRING) {
                    path = computePath(path, name);
                    logElement(path, keyValuePair[0].substring(1), Optional.ofNullable(keyValuePair[1]), attributes);
                    writeValue(keyValuePair[1]);
                } else {
                    var contentProperties = splitContent(keyValuePair[1]);
                    for (var contentProperty : contentProperties) {
                        keyValuePair = readProperty(contentProperty);
                        writeRecursively(path, keyValuePair[0], keyValuePair[1]);
                    }
                }
            }
            for (var element : elements) {
                var keyValuePair = readProperty(element);
                writeRecursively(path, keyValuePair[0], keyValuePair[1]);
            }
            writeEndElement(name);
        }
    }

    private boolean hasContent(String p) {
        return p.startsWith("\"#");
    }

    private String fixInvalidContent(String p) {
        return p.replace("\"#", "\"");
    }

    private String fixInvalidAttributes(String p) {
        return p.replace("\"@", "\"");
    }

    private boolean hasInvalidContent(String p) {
        return p.startsWith("\"#:");
    }

    private boolean hasInvalidElement(String p) {
        return p.startsWith("\"\":");
    }

    private boolean hasInvalidAttribute(String p) {
        return p.startsWith("\"@\":") || p.matches("\\\"@.*\\\":[\\{\\[]");
    }

    private boolean hasInvalidContent(String name, String p) {
        return !p.startsWith("\"#" + name + "\":") && hasContent(p);
    }

    private String computePath(String parent, String name) {
        return parent == null
                ? name
                : name.startsWith("#")
                ? parent
                : parent != null
                ? parent + ", " + name
                : name;
    }

    private String dequote(String value) {
        if (value != null &&
                value.length() > 1 &&
                value.charAt(0) == '\"' &&
                value.charAt(value.length() - 1) == '\"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private void writeElementWithSimpleContent(String parentPath, String name, String value) {
        var path = computePath(parentPath, name);
        logElement(path, name, Optional.ofNullable(value), null);

        value = dequote(value);
        if (value == null || value.length() == 0) {
            writeSimpleElement(name);
        } else {
            writeBeginElement(name);
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
        var keyValuePair = attribute.split(":");
        var name = dequote(keyValuePair[0].strip());
        var value = keyValuePair[1].strip();
        if (value == null || "null".equals(value)) {
            value = "\"\"";
        }
        builder.append(name);
        builder.append("=");
        builder.append(value);
    }

    private void writeValue(String value) {
        value = dequote(value.strip());
        if (value == null || "null".equals(value)) {
            value = "";
        }
        builder.append(value);
    }

    private enum ElementType {
        OBJECT,
        ARRAY,
        STRING,
        LITERAL;

        public static ElementType of(String elementValue) {
            if (elementValue == null) return ElementType.LITERAL;
            if (elementValue.length() == 0) return ElementType.STRING;
            if (elementValue.charAt(0) == '{') return ElementType.OBJECT;
            if (elementValue.charAt(0) == '[') return ElementType.ARRAY;
            if (elementValue.charAt(0) == '"') return ElementType.STRING;
            return ElementType.LITERAL;
        }
    }
}
