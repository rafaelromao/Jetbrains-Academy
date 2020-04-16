package blockchain;

public class Block {

    private int id;
    private long timestamp;
    private Hash previous;

    public Block(int id, Hash hashOfPreviousBlock) {
        this.id = id;
        this.timestamp = System.currentTimeMillis();
        this.previous = hashOfPreviousBlock;
    }

    Hash hash() {
        return new Hash(this.getValues());
    }

    private String getValues() {
        return Integer.toString(id) + timestamp + previous;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Block:");
        sb.append("\n");
        sb.append("Id: ");
        sb.append(id);
        sb.append("\n");
        sb.append("Timestamp: ");
        sb.append(timestamp);
        sb.append("\n");
        sb.append("Hash of the previous block:");
        sb.append("\n");
        sb.append(previous == null ? 0 : previous);
        sb.append("\n");
        sb.append("Hash of the block:");
        sb.append("\n");
        sb.append(hash());
        sb.append("\n");
        return sb.toString();
    }
}
