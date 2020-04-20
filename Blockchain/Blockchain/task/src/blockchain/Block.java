package blockchain;

import java.io.Serializable;
import java.time.Duration;

public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int minerId;
    private final int id;
    private final int proofLength;
    private final int proofLengthState;
    private final long timestamp;
    private final Hash current;
    private final Hash previous;
    private int magicNumber;
    private transient long generatingTime;

    public Block(int minerId, int id, int proofLength, Hash hashOfPreviousBlock, int proofLengthState) {
        this.minerId = minerId;
        this.id = id;
        this.proofLength = proofLength;
        this.proofLengthState = proofLengthState;
        this.timestamp = System.currentTimeMillis();
        this.previous = hashOfPreviousBlock;
        this.current = hash();
    }

    private Hash hash() {
        var start = System.currentTimeMillis();
        try {
            while (true) {
                magicNumber = (int)(Math.random() * Integer.MAX_VALUE);
                var hash = new Hash(getValues());
                if (hash.validate(proofLength, getValues())) {
                    return hash;
                }
            }
        } finally {
            var end = System.currentTimeMillis();
            generatingTime = Duration.ofMillis(end - start).toSeconds();
        }
    }

    public int getId() {
        return id;
    }

    private String getValues() {
        return Integer.toString(magicNumber) + minerId + id + timestamp + (previous != null ? previous : 0);
    }

    public Hash getHash() {
        return current;
    }

    public void validate(Hash previous) {
        if (!ValidatePrevious(previous)) {
            return;
        }
        if (!current.validate(proofLength, getValues())) {
            return;
        }
        throw new IllegalArgumentException(String.format("Block %s is not valid!", id));
    }

    private boolean ValidatePrevious(Hash previous) {
        if (this.previous == null && previous == null) {
            return true;
        }
        if (this.previous.equals(previous)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Block:");
        sb.append("\n");
        sb.append("Created by miner # ");
        sb.append(minerId);
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
        sb.append("Block was generating for ");
        sb.append(generatingTime);
        sb.append(" seconds");
        sb.append("\n");
        sb.append(proofLengthState == 0
                ? "N stays the same"
                : proofLengthState < 0
                    ? "N decreased to " + proofLength
                    : "N increased to " + proofLength);
        sb.append("\n");
        return sb.toString();
    }
}
