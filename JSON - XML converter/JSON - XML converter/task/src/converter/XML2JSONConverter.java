package converter;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class XML2JSONConverter implements Converter {
    private Pattern simpleElementPattern = Pattern.compile("\\s*\\<(.*?)\\/\\>\\s*");
    private Pattern elementNameAndAttributesPattern = Pattern.compile("\\<?\\/?(\\w*)(.*)($|\\>)");
    private Pattern attributesPartsPattern = Pattern.compile("\\s*(\\w*)\\s*=\\s*\\\"(\\w*)\\\"\\s*");
    private Pattern elementStartingPattern = Pattern.compile("\\s*\\<\\/?(.*?)\\/?\\>\\s*");
    private Pattern elementContentPattern = Pattern.compile("\\>(.*)\\<");
    private Pattern elementsPartsPattern = Pattern.compile("(\\<.*?\\>)|(.+?(?=\\<|$))");
    private Pattern elementClosingPattern = Pattern.compile("\\<\\/(.*?)\\>|\\<(.*?)\\/\\>");
    private StringBuilder builder = new StringBuilder();
    private PrintStream out;

    @Override
    public String convert(String content) {
        writeBeginObject();
        var elements = readElements(content
                .replace("\r", "")
                .replace("\n", ""));
        var keyValuePair = readElement(elements.get(0));
        writeRecursively(null,
                keyValuePair[0],
                keyValuePair[1],
                keyValuePair[2]);
        writeEndObject();
        return builder.toString();
    }

    @Override
    public void logTo(PrintStream out) {
        this.out = out;
    }

    private void println(String fmt, String... params) {
        if (out != null) {
            out.printf(fmt + "\n", params);
        }
    }

    private List<String> readElements(String elements) {
        var result = new ArrayList<String>();
        var partsMatcher = elementsPartsPattern.matcher(elements);
        partsMatcher.find();
        var parts = Stream.concat(
                Stream.of(partsMatcher.group().strip()),
                partsMatcher.results().map(r -> r.group().strip()))
                .filter(r -> r.length() > 0)
                .collect(toList());
        var currentElement = new StringBuilder();
        var currentElementName = "";
        for (var part : parts) {
            // If starting element
            var isOpeningTag = currentElement.length() == 0;
            if (isOpeningTag) {
                var elementNameAndAttributesMatcher = elementNameAndAttributesPattern.matcher(part);
                if (elementNameAndAttributesMatcher.find()) {
                    currentElementName = elementNameAndAttributesMatcher.group(1);
                }
            }
            // If literal
            var isLiteral = !part.contains("<");
            if (isLiteral) {
                var isInElement = currentElement.length() > 0;
                if (isInElement) {
                    currentElement.append(part);
                } else {
                    result.add(part);
                    currentElement.setLength(0);
                }
                continue;
            }
            // At this point, the part will compose the current element no matter what
            currentElement.append(part);
            // If closing element
            var closingMatcher = elementClosingPattern.matcher(part);
            var isClosingTag = closingMatcher.find();
            if (isClosingTag) {
                var elementNameAndAttributesMatcher = elementNameAndAttributesPattern.matcher(part);
                if (elementNameAndAttributesMatcher.find()) {
                    var closingElementName = elementNameAndAttributesMatcher.group(1);
                    if (currentElementName.equals(closingElementName)) {
                        result.add(currentElement.toString());
                        currentElement.setLength(0);
                    }
                }
            }
        }
        return result;
    }

    private String[] readElement(String element) {
        var elementMatcher = elementContentPattern.matcher(element);
        if (!elementMatcher.find()) {
            var tagMatcher = simpleElementPattern.matcher(element);
            tagMatcher.find();
            var tag = tagMatcher.group(1);
            var nameAndAttributes = extractNameAndAttributes(tag);
            return new String[]{nameAndAttributes[0], null, nameAndAttributes[1].strip()};
        } else {
            var tagMatcher = elementStartingPattern.matcher(element);
            tagMatcher.find();
            var tag = tagMatcher.group(1);
            var nameAndAttributes = extractNameAndAttributes(tag);
            var content = extractContent(element);
            return new String[]{nameAndAttributes[0], content, nameAndAttributes[1].strip()};
        }
    }

    @NotNull
    private String extractContent(String element) {
        var contentMatcher = elementContentPattern.matcher(element);
        contentMatcher.find();
        var content = contentMatcher.group(1);
        content = content == null ? "null" : content;
        return content;
    }

    @NotNull
    private String[] extractNameAndAttributes(String tag) {
        var nameAndAttributes = new String[2];
        var nameAndAttributesMatcher = elementNameAndAttributesPattern.matcher(tag);
        nameAndAttributesMatcher.find();
        nameAndAttributes[0] = nameAndAttributesMatcher.group(1);
        nameAndAttributes[1] = nameAndAttributesMatcher.groupCount() == 3
                ? nameAndAttributesMatcher.group(2)
                : null;
        return nameAndAttributes;
    }

    private void writeRecursively(String parentPath, String name, String content, String attributes) {
        var elementType = ValueType.of(content);
        if (attributes != null && attributes.length() > 0) {
            elementType = ValueType.OBJECT;
        }
        switch (elementType) {
            case LITERAL:
            case STRING:
                writeString(parentPath, name, content);
                break;
            case OBJECT:
                writeObject(parentPath, name, content, attributes);
        }
    }

    private void writeObject(String parentPath, String name, String value, String attributes) {
        var path = computePath(parentPath, name);
        logElement(path, name, value, attributes);

        builder.append("\"");
        builder.append(name);
        builder.append("\"");
        builder.append(":");
        var valueType = ValueType.of(value);
        var children = valueType == ValueType.LITERAL || valueType == ValueType.STRING
                ? List.<String[]>of()
                : readElements(value.strip())
                .stream()
                .map(this::readElement)
                .collect(toList());
        if (children.size() > 0 || (attributes != null && attributes.length() > 0)) {
            valueType = ValueType.OBJECT;
        }
        if (children.size() > 1 && (attributes == null || attributes.length() == 0)) {
            valueType = ValueType.ARRAY;
        }
        if (attributes != null && attributes.length() > 0) {
            var element = new ArrayList<String[]>();
            element.add(new String[]{"#" + name, value, null});
            var attrs = readAttributes(attributes);
            children = Stream
                    .of(element.stream(), attrs.stream())
                    .reduce(Stream::concat)
                    .get()
                    .collect(toList());
        }
        switch (valueType) {
            case OBJECT:
                writeBeginObject();
                break;
            case ARRAY:
                writeBeginArray();
                break;
        }
        for (var i = 0; i < children.size(); i++) {
            if (valueType == ValueType.ARRAY) {
                writeBeginObject();
            }
            var keyValuePair = children.get(i);
            writeRecursively(path,
                    keyValuePair[0],
                    keyValuePair[1],
                    keyValuePair[2]);
            if (valueType == ValueType.ARRAY) {
                writeEndObject();
            }
            if (i < children.size() - 1) {
                builder.append(",");
            }
        }
        switch (valueType) {
            case OBJECT:
                writeEndObject();
                break;
            case ARRAY:
                writeEndArray();
                break;
        }
    }

    private void logElement(String path, String name, String value, String attributes) {
        if (!name.startsWith("#") && !name.startsWith("@")) {
            println("Element:");
            println("path = %s", path);
            var valueType = ValueType.of(value);
            switch (valueType) {
                case STRING:
                    println("value = \"%s\"", value);
                    break;
                case LITERAL:
                    println("value = %s", value);
                    break;
            }
            if (attributes != null && attributes.length() > 0) {
                println("attributes:");
                var attrs = readAttributes(attributes);
                for (var attr : attrs) {
                    println("%s = \"%s\"", attr[0].substring(1), attr[1]);
                }
            }
            println("");
        }
    }

    private String computePath(String parent, String name) {
        return name.startsWith("#")
                ? parent
                : parent != null
                ? parent + ", " + name
                : name;
    }

    private List<String[]> readAttributes(String attributes) {
        var result = new ArrayList<String[]>();
        var matcher = attributesPartsPattern.matcher(attributes);
        while (matcher.find()) {
            result.add(new String[]{"@" + matcher.group(1), matcher.group(2), null});
        }
        return result;
    }

    private void writeString(String parentPath, String name, String value) {
        var path = computePath(parentPath, name);
        logElement(path, name, value, null);

        builder.append("\"");
        builder.append(name);
        builder.append("\"");
        builder.append(":");
        if (value == null) {
            builder.append("null");
        } else {
            builder.append("\"");
            builder.append(value);
            builder.append("\"");
        }
    }

    private void writeBeginObject() {
        builder.append("{");
    }

    private void writeEndObject() {
        builder.append("}");
    }

    private void writeBeginArray() {
        builder.append("[");
    }

    private void writeEndArray() {
        builder.append("]");
    }

    private enum ValueType {
        OBJECT,
        ARRAY,
        STRING,
        LITERAL;

        public static ValueType of(String valueType) {
            if (valueType == null || valueType.equals("null")) return ValueType.LITERAL;
            if (valueType.length() == 0 || valueType.charAt(0) == '"') return ValueType.STRING;
            if (valueType.charAt(0) == '<') return ValueType.OBJECT;
            return ValueType.STRING;
        }
    }
}
