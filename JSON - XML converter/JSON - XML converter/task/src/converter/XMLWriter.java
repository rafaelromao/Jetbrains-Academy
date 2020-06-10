package converter;

class XMLWriter {
    private StringBuilder builder = new StringBuilder();

    void writeBeginElement(String elementName) {
        builder.append("<");
        builder.append(elementName);
        builder.append(">");
    }

    void writeEndElement(String elementName) {
        builder.append("</");
        builder.append(elementName);
        builder.append(">");
    }

    void writeElement(String elementName) {
        builder.append("<");
        builder.append(elementName);
        builder.append("/>");
    }

    void writeValue(String value) {
        builder.append(value == null ? "" : value);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
