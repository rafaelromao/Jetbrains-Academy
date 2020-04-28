package blockchain;

import java.io.Serializable;
import java.security.*;

public class Transaction implements Serializable {
    private final int id;
    private final String from;
    private final String to;
    private final int value;
    private final PublicKey publicKey;
    private final String signature;

    public Transaction(int id, String from, String to, int value, PublicKey publicKey, String signature) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.value = value;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public int getBalance(String owner) {
        if (from.equals(owner)) {
            return -1 * value;
        } else if (to.equals(owner)) {
            return value;
        } else {
            return 0;
        }
    }

    public int getValue() {
        return value;
    }

    public void validate() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        var sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        var data = id + from + to + value;
        sig.update(data.getBytes());
        if (!sig.verify(signature.getBytes())) {
            throw new IllegalArgumentException(String.format("Transaction %s is not valid!", id));
        }
    }

    @Override
    public String toString() {
        return from + " sent " + value + " VC to " + to;
    }
}

