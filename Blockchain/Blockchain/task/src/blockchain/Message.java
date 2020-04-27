package blockchain;

import java.io.Serializable;

public class Message implements Serializable {
    private final String from;
    private String value;

    public Message(String from, String value) {
        this.from = from;
        this.value = value;
    }

    @Override
    public String toString() {
        return from + ": " + value;
    }
}

