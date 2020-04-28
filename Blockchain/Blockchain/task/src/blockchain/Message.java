package blockchain;

import java.io.Serializable;
import java.security.*;

public class Message implements Serializable {
    private final int id;
    private final String from;
    private final String text;
    private final PublicKey publicKey;
    private final String signature;

    public Message(int id, String from, String text, PublicKey publicKey, String signature) {
        this.id = id;
        this.from = from;
        this.text = text;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public void validate() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        var sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        var data = id + from + text;
        sig.update(data.getBytes());
        if (!sig.verify(signature.getBytes())) {
            throw new IllegalArgumentException(String.format("Message %s is not valid!", id));
        }
    }

    @Override
    public String toString() {
        return from + ": " + text;
    }
}

