package converter;

class XML2JSONConverter implements Converter {
    @Override
    public String convert(String content) {
        var reader = new XMLReader();
        var element = reader.readElement(content);
        var writer = new JSONWriter();
        writer.writeBeginObject();
        writer.writeProperty(element[0], element[1]);
        writer.writeEndObject();
        return writer.toString();
    }
}