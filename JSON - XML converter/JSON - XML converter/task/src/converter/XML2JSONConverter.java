package converter;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class XML2JSONConverter implements Converter {
    private Pattern simpleElementPattern = Pattern.compile("\\s*\\<(.*?)\\/\\>\\s*");
    private Pattern elementNameAndAttributesPattern = Pattern.compile("\\<?(\\w*)(.*)($|\\>)");
    private Pattern attributesPartsPattern = Pattern.compile("(\\w*)=\\\"(\\w*)\\\"");
    private Pattern elementStartingPattern = Pattern.compile("\\s*\\<\\/?(.*?)\\/?\\>\\s*");
    private Pattern elementContentPattern = Pattern.compile("\\>(.*)\\<");
    private Pattern elementsPartsPattern = Pattern.compile("(\\<.*?\\>)|(.+?(?=\\<|$))");
    private Pattern elementClosingPattern = Pattern.compile("\\<\\/(.*?)\\>|\\<(.*?)\\/\\>");
    private StringBuilder builder = new StringBuilder();

    @Override
    public String convert(String content) {
        var elements = readElements(content);
        var keyValuePair = readElement(elements[0]);
        writeRecursively(
                keyValuePair[0],
                keyValuePair[1],
                keyValuePair[2]);
        return builder.toString();
    }

    private String[] readElements(String elements) {
        var result = new ArrayList<String>();
        var partsMatcher = elementsPartsPattern.matcher(elements);
        partsMatcher.find();
        var parts = Stream.concat(
                Stream.of(partsMatcher.group().strip()),
                partsMatcher.results().map(r -> r.group().strip()))
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
                    currentElement.append(part);
                    continue;
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
            var isClosingCurrentElement =
                    isClosingTag &&
                            currentElementName.equals(closingMatcher.group(1));
            if (isClosingCurrentElement) {
                result.add(currentElement.toString());
                currentElement.setLength(0);
            }
        }
        return result.toArray(String[]::new);
    }

    private String[] readElement(String element) {
        var elementMatcher = simpleElementPattern.matcher(element);
        if (elementMatcher.find()) {
            return new String[]{elementMatcher.group(1), null};
        } else {
            var tagMatcher = elementStartingPattern.matcher(element);
            tagMatcher.find();
            var tag = tagMatcher.group(1);
            var nameAndAttributesMatcher = elementNameAndAttributesPattern.matcher(tag);
            nameAndAttributesMatcher.find();
            var name = nameAndAttributesMatcher.group(1);
            var attributes = nameAndAttributesMatcher.groupCount() == 3
                    ? nameAndAttributesMatcher.group(2)
                    : null;
            var contentMatcher = elementContentPattern.matcher(element);
            contentMatcher.find();
            var content = contentMatcher.group(1);
            content = content.strip().length() == 0 ? "null" : content;
            return new String[]{name, content, attributes};
        }
    }

    private void writeRecursively(String name, String content, String attributes) {
        var elementType = ValueType.of(content);
        if (attributes != null && attributes.length() > 0) {
            elementType = ValueType.OBJECT;
        }
        switch (elementType) {
            case LITERAL:
            case STRING:
                writeString(name, content);
                break;
            case OBJECT:
                writeObject(name, content, attributes);
        }
    }

    private void writeObject(String name, String value, String attributes) {
        writeBeginObject();
        builder.append("\"");
        builder.append(name);
        builder.append("\"");
        builder.append(":");
        if (attributes != null && attributes.length() > 0) {

        }
        var elements = readElements(value.replaceAll("\\s", ""));
        if (elements.length == 1) {
            writeBeginObject();
        } else {
            writeBeginArray();
        }
        for (var i = 0; i < elements.length; i++) {
            String element = elements[i];
            var keyValuePair = readElement(element);
            writeRecursively(
                    keyValuePair[0],
                    keyValuePair[1],
                    keyValuePair[2]);
            if (i < elements.length - 1) {
                builder.append(",");
            }
        }
        if (elements.length == 1) {
            writeEndObject();
        } else {
            writeEndArray();
        }
        writeEndObject();
    }

    private void writeString(String name, String value) {
        writeBeginObject();
        builder.append("\"");
        builder.append(name);
        builder.append("\"");
        builder.append(":");
        if (value == null || value.length() == 0) {
            builder.append("null");
        } else {
            builder.append("\"");
            builder.append(value);
            builder.append("\"");
        }
        writeEndObject();
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
            if (valueType.charAt(0) == '<') return ValueType.OBJECT;
            if (valueType.charAt(0) == '"') return ValueType.STRING;
            return ValueType.LITERAL;
        }
    }
}
