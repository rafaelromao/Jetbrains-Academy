package converter;

public class XMLWriter {
    private StringBuilder builder = new StringBuilder();

    public void writeBeginElement(String elementName) {
        builder.append("<");
        builder.append(elementName);
        builder.append(">");
    }

    public void writeEndElement(String elementName) {
        builder.append("</");
        builder.append(elementName);
        builder.append(">");
    }

    public void writeElement(String elementName) {
        builder.append("<");
        builder.append(elementName);
        builder.append("/>");
    }

    public void writeValue(String value) {
        builder.append(value == null ? "" : value);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
