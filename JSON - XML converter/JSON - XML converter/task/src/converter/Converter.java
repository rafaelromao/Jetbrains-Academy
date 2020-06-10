package converter;

interface Converter {

    String convert(String content);

    class Factory {
        public static Converter createFor(String input) {
            return input.charAt(0) == '<' ? new XML2JSONConverter() : new JSON2XMLConverter();
        }
    }

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

    class JSON2XMLConverter implements Converter {
        @Override
        public String convert(String content) {
            var reader = new JSONReader();
            var obj = reader.readObject(content);
            var property = reader.readProperty(obj);
            var writer = new XMLWriter();
            if (property[1] == null || property[1].length() == 0) {
                writer.writeElement(property[0]);
            } else {
                writer.writeBeginElement(property[0]);
                writer.writeValue(property[1]);
                writer.writeEndElement(property[0]);
            }
            return writer.toString();
        }
    }
}
