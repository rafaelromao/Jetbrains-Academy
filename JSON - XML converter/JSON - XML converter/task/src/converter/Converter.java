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
            XMLWriter writer = new XMLWriter();
            var reader = new JSONReader();
            var value = reader.readObject(content);
            var properties = value.split("(?!\\B\\{[^\\}]*),(?![^\\{]*\\}\\B)");
            properties = properties.length == 0 ? new String[] { value } : properties;
            for (var property: properties) {
                var keyValuePair = reader.readProperty(property);
                writeElement(writer, keyValuePair[0], keyValuePair[1]);
            }
            return writer.toString();
        }

        private void writeElement(XMLWriter writer, String name, String value) {
            var elementType = JSONReader.ElementType.of(value);
            switch (elementType) {
                case LITERAL:
                case STRING:
                    writeLiteral(writer, name, value);
                    break;
                case OBJECT:
                    var obj = convert(value);
                    writeElement(writer, name, obj);
                    break;
            }
        }

        private void writeLiteral(XMLWriter writer, String name, String value) {
            if (value == null || value.length() == 0) {
                writer.writeElement(name);
            } else {
                writer.writeBeginElement(name);
                writer.writeValue(value);
                writer.writeEndElement(name);
            }
        }
    }
}
