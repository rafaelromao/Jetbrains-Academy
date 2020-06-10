package converter;

interface Converter {

    String convert(String content);

    class Factory {
        public static Converter createFor(String input) {
            return input.charAt(0) == '<' ? new XML2JSONConverter() : new JSON2XMLConverter();
        }
    }

    class JSON2XMLConverter implements Converter {

        @Override
        public String convert(String content) {
            XMLWriter writer = new XMLWriter();
            var reader = new JSONReader();
            var value = reader.readObject(content);
            var properties = value.split("(?!\\B\\{[^\\}]*),(?![^\\{]*\\}\\B)");
            if (properties.length == 1) {
                var keyValuePair = reader.readProperty(properties[0]);
                writer.writeElement(reader, keyValuePair[0], keyValuePair[1]);
            }
            return writer.toString();
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
}
