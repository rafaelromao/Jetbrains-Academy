package converter;

class JSONWriter {
    private StringBuilder builder = new StringBuilder();

    void writeBeginObject() {
        builder.append("{");
    }

    void writeEndObject() {
        builder.append("}");
    }

    void writeProperty(String key, String value) {
        writeString(key);
        builder.append(":");
        writeString(value);
    }

    void writeString(String value) {
        if (value == null || value.length() == 0) {
            builder.append("null");
        } else {
            builder.append("\"");
            builder.append(value);
            builder.append("\"");
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
