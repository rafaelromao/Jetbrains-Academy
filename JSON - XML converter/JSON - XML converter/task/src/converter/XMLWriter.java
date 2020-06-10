package converter;

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

    @Override
    public String toString() {
        return builder.toString();
    }
}
