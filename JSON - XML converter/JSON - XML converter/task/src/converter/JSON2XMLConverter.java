package converter;

import java.util.Arrays;
import java.util.regex.Pattern;

class JSON2XMLConverter implements Converter {
    private Pattern objectPattern = Pattern.compile("\\s*\\{\\s*(.*)\\s*\\}\\s*");
    private Pattern propertyNamePattern = Pattern.compile("\\s*\"([\\w|@|#]*)\"\\s*:\\s*");
    private Pattern propertyValuePattern = Pattern.compile("\\s*:\\s*\"*(.*)[$|\"?\\s*]");
    private Pattern propertiesPattern = Pattern.compile("(?!\\B\\{[^\\}]*),(?![^\\{]*\\}\\B)");
    private StringBuilder builder = new StringBuilder();

    @Override
    public String convert(String content) {
        var value = readObject(content);
        var properties = propertiesPattern.split(value);
        for (var property : properties) {
            var keyValuePair = readProperty(property);
            writeElement(keyValuePair[0], keyValuePair[1]);
        }
        return builder.toString();
    }

    private String readObject(String content) {
        var objectMatcher = objectPattern.matcher(content.replaceAll("\\s", ""));
        objectMatcher.find();
        return objectMatcher.group(1);
    }

    private String[] readProperty(String content) {
        content = content.strip().endsWith("}") ? content.strip() + "\n" : content.strip();
        var keyMatcher = propertyNamePattern.matcher(content);
        keyMatcher.find();
        var key = keyMatcher.group(1);
        var valueMatcher = propertyValuePattern.matcher(content);
        valueMatcher.find();
        var value = valueMatcher.group(1).strip();
        value = "null".equals(value) ? null : value;
        return new String[]{key, value};
    }

    private void writeElement(String name, String value, String... attributes) {
        var elementType = ElementType.of(value);
        switch (elementType) {
            case LITERAL:
            case STRING:
                writeLiteral(name, value, attributes);
                break;
            case OBJECT:
                writeObject(name, value);
        }
    }

    private void writeObject(String name, String value) {
        var properties = propertiesPattern.split(readObject(value));
        var attributes = Arrays.stream(properties)
                .filter(p -> p.startsWith("\"@"))
                .toArray(String[]::new);
        var elements = Arrays.stream(properties)
                .filter(p -> !p.startsWith("\"@"))
                .toArray(String[]::new);
        if (elements.length == 0) {
            writeSimpleElement(name, attributes);
        } else {
            writeBeginElement(name, attributes);
            for (var element : elements) {
                var keyValuePair = readProperty(element);
                writeElement(keyValuePair[0], keyValuePair[1]);
            }
        }
        writeEndElement(name);
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
        builder.append("=");
        builder.append(keyValuePair[1].strip());
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