package blockchain;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.stream.Stream;

public class MessageList implements Serializable {
    private final List<Message> messages;

    public MessageList(List<Message> messages) {
        this.messages = messages;
    }

    public Stream<Message> messages() {
        return messages.stream();
    }

    public void validate() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        for (var message : messages) {
            message.validate();
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var message: messages) {
            sb.append(message);
            sb.append("\n");
        }
        return sb.toString();
    }
}
