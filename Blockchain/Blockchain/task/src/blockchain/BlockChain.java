package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Stack;

public class BlockChain implements Serializable {

    private static final long serialVersionUID = 1L;

    public static BlockChain load(String fileName) throws IOException, ClassNotFoundException {
        BlockChain blockChain;
        if (Files.exists(Paths.get(fileName))) {
            try (var file = new FileInputStream(fileName);
                 var buffer = new BufferedInputStream(file);
                 var stream = new ObjectInputStream(buffer)) {

                blockChain = (BlockChain) stream.readObject();
                blockChain.validate();
            }
        } else {
            blockChain = new BlockChain(fileName);
        }
        return blockChain;
    }

    private String fileName;
    private final Stack<Block> blocks = new Stack<>();
    private long lastBlockTime = System.currentTimeMillis();
    private int proofLength = 0;

    public BlockChain() {
    }

    public BlockChain(String fileName) {
        this();
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void validate() {
        synchronized (blocks) {
            if (!blocks.empty()) {
                blocks.get(0).validate(null);
                for (int i = 1; i < blocks.size(); i++) {
                    var block = blocks.get(i);
                    block.validate(blocks.get(i - 1).getHash());
                }
            }
        }
    }

    private void save() throws IOException {
        synchronized (blocks) {
            if (fileName == null) {
                return;
            }
            var path = Paths.get(fileName);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            try (var file = new FileOutputStream(fileName);
                 var buffer = new BufferedOutputStream(file);
                 var stream = new ObjectOutputStream(buffer)) {
                stream.writeObject(this);
            }
        }
    }

    private void adjustProofLength() {
        synchronized (blocks) {
            var idleTime = Duration.ofMillis(System.currentTimeMillis() - lastBlockTime);
            lastBlockTime = System.currentTimeMillis();
            if (idleTime.toSeconds() < 1) {
                proofLength++;
            } else if (idleTime.toMinutes() > 1) {
                proofLength--;
            }
        }
    }

    public boolean put(Block block) throws IOException {
        synchronized (blocks) {
            if (block.getId() == getNextBlockId()) {
                blocks.add(block);
                save();
                adjustProofLength();
                return true;
            }
            return false;
        }
    }

    public Iterable<Block> blocks() {
        synchronized (blocks) {
            return blocks;
        }
    }

    public Hash getLastBlockHash() {
        synchronized (blocks) {
            return blocks.size() == 0 ? null : blocks.peek().getHash();
        }
    }

    public int getNextBlockId() {
        synchronized (blocks) {
            return blocks.size();
        }
    }

    public int getProofLength() {
        synchronized (blocks) {
            return proofLength;
        }
    }
}
