package converter;

import java.util.Arrays;

class XMLWriter {
    private StringBuilder builder = new StringBuilder();

    void writeBeginElement(String elementName, String... attributes) {
        builder.append("<");
        builder.append(elementName.startsWith("#") ? elementName.substring(1) : elementName);
        writeAttributes(attributes);
        builder.append(">");
    }

    void writeEndElement(String elementName) {
        builder.append("</");
        builder.append(elementName.startsWith("#") ? elementName.substring(1) : elementName);
        builder.append(">");
    }

    void writeElement(String elementName, String... attributes) {
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

    void writeValue(String value) {
        builder.append(value == null ? "" : value);
    }

    void writeElement(JSONReader reader, String name, String value, String... attributes) {
        var elementType = JSONReader.ElementType.of(value);
        switch (elementType) {
            case LITERAL:
            case STRING:
                writeLiteral(name, value, attributes);
                break;
            case OBJECT:
                writeObject(reader, name, value);
        }
    }

    void writeObject(JSONReader reader, String name, String value) {
        var properties = reader.readObject(value)
                .split("(?!\\B\\{[^\\}]*),(?![^\\{]*\\}\\B)");
        var attributes = Arrays.stream(properties)
                .filter(p -> p.startsWith("\"@"))
                .toArray(String[]::new);
        var elements = Arrays.stream(properties)
                .filter(p -> !p.startsWith("\"@"))
                .toArray(String[]::new);
        if (elements.length == 0) {
            writeElement(name, attributes);
        } else {
            writeBeginElement(name, attributes);
            for (var element : elements) {
                var keyValuePair = reader.readProperty(element);
                writeElement(
                        reader,
                        keyValuePair[0],
                        keyValuePair[1]);
            }
        }
        writeEndElement(name);
    }

    void writeLiteral(String name, String value, String... attributes) {
        if (value == null || value.length() == 0) {
            writeElement(name, attributes);
        } else {
            writeBeginElement(name, attributes);
            writeValue(value);
            writeEndElement(name);
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
