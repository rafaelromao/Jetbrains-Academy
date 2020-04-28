package blockchain;

import java.io.Serializable;

public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String minerId;
    private final int id;
    private final int proofLength;
    private final int proofLengthState;
    private final long timestamp;
    private final Hash current;
    private final Hash previous;
    private int magicNumber;
    private long generatingTime;
    private Serializable data;

    public Block(String minerId, int id, int proofLength, Hash hashOfPreviousBlock, int proofLengthState, Serializable data) {
        this.minerId = minerId;
        this.id = id;
        this.proofLength = proofLength;
        this.proofLengthState = proofLengthState;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.previous = hashOfPreviousBlock;
        this.current = hash();
    }

    private Hash hash() {
        var start = System.currentTimeMillis();
        try {
            while (true) {
                magicNumber = (int) (Math.random() * Integer.MAX_VALUE);
                var hash = new Hash(getValues());
                if (hash.validate(proofLength, getValues())) {
                    return hash;
                }
            }
        } finally {
            var end = System.currentTimeMillis();
            generatingTime = end - start;
        }
    }

    public long getGeneratingTime() {
        return generatingTime;
    }

    private String getValues() {
        return magicNumber +
                minerId +
                id +
                timestamp +
                data +
                (previous != null ? previous : 0);
    }

    public Hash getHash() {
        return current;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Block:");
        sb.append("\n");
        sb.append("Created by ");
        sb.append(minerId);
        sb.append("\n");
        sb.append(minerId);
        sb.append(" gets 100 VC");
        sb.append("\n");
        sb.append("Id: ");
        sb.append(id);
        sb.append("\n");
        sb.append("Timestamp: ");
        sb.append(timestamp);
        sb.append("\n");
        sb.append("Magic number: ");
        sb.append(magicNumber);
        sb.append("\n");
        sb.append("Hash of the previous block:");
        sb.append("\n");
        sb.append(previous == null ? 0 : previous);
        sb.append("\n");
        sb.append("Hash of the block:");
        sb.append("\n");
        sb.append(current);
        sb.append("\n");
        sb.append("Block data:");
        if (data == null) {
            sb.append(" no transactions");
            sb.append("\n");
        } else {
            sb.append("\n");
            sb.append(data);
        }
        sb.append("Block was generating for ");
        sb.append(generatingTime / 1000);
        sb.append(" seconds");
        sb.append("\n");
        sb.append(proofLengthState == 0
                ? "N stays the same"
                : proofLengthState < 0
                ? "N decreased to " + proofLength
                : "N increased to " + proofLength);
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void validate(Hash previous) {
        var isValid = true;

        isValid &= (this.previous == null && previous == null) || this.previous.equals(previous);
        isValid &= !current.validate(proofLength, getValues());

        if (!isValid) {
            throw new IllegalArgumentException(String.format("Block %s is not valid!", id));
        }
    }

    public <T> T getData() {
        return (T)data;
    }
}
