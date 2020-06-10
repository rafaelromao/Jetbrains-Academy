package converter;

interface Converter {

    String convert(String content);

    class Factory {
        public static Converter createFor(String input) {
            return input.charAt(0) == '<' ? new XML2JSONConverter() : new JSON2XMLConverter();
        }
    }
}
