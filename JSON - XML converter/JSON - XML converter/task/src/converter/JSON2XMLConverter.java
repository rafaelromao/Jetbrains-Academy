package converter;

import java.util.Arrays;

class JSON2XMLConverter implements Converter {

    @Override
    public String convert(String content) {
        XMLWriter writer = new XMLWriter();
        var reader = new JSONReader();
        var value = reader.readObject(content);
        var properties = value.split("(?!\\B\\{[^\\}]*),(?![^\\{]*\\}\\B)");
        if (properties.length == 1) {
            var keyValuePair = reader.readProperty(properties[0]);
            writeElement(reader, writer, keyValuePair[0], keyValuePair[1]);
        }
        return writer.toString();
    }

    private void writeElement(JSONReader reader, XMLWriter writer, String name, String value, String... attributes) {
        var elementType = JSONReader.ElementType.of(value);
        switch (elementType) {
            case LITERAL:
            case STRING:
                writeLiteral(writer, name, value, attributes);
                break;
            case OBJECT:
                writeObject(reader, writer, name, value);
        }
    }

    private void writeObject(JSONReader reader, XMLWriter writer, String name, String value) {
        var properties = reader.readObject(value)
                .split("(?!\\B\\{[^\\}]*),(?![^\\{]*\\}\\B)");
        var attributes = Arrays.stream(properties)
                .filter(p -> p.startsWith("\"@"))
                .toArray(String[]::new);
        var elements = Arrays.stream(properties)
                .filter(p -> !p.startsWith("\"@"))
                .toArray(String[]::new);
        if (elements.length == 0) {
            writer.writeElement(name, attributes);
        } else {
            writer.writeBeginElement(name, attributes);
            for (var element : elements) {
                var keyValuePair = reader.readProperty(element);
                writeElement(
                        reader,
                        writer,
                        keyValuePair[0],
                        keyValuePair[1]);
            }
        }
        writer.writeEndElement(name);
    }

    private void writeLiteral(XMLWriter writer, String name, String value, String... attributes) {
        if (value == null || value.length() == 0) {
            writer.writeElement(name, attributes);
        } else {
            writer.writeBeginElement(name, attributes);
            writer.writeValue(value);
            writer.writeEndElement(name);
        }
    }
}
