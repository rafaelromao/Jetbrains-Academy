package converter;

import java.io.PrintStream;

interface Converter {

    String convert(String content);

    void logTo(PrintStream out);

    class Factory {
        public static Converter createFor(String input) {
            return input.charAt(0) == '<' ? new XML2JSONConverter() : new JSON2XMLConverter();
        }
    }
}
