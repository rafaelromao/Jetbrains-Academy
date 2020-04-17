package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class BlockChain implements Serializable {

    private static final long serialVersionUID = 1L;

    public static BlockChain load(int proofLength, String fileName) throws IOException, ClassNotFoundException {
        BlockChain blockChain;
//        if (Files.exists(Paths.get(fileName))) {
//            try (var file = new FileInputStream(fileName);
//                 var buffer = new BufferedInputStream(file);
//                 var stream = new ObjectInputStream(buffer)) {
//
//                blockChain = (BlockChain) stream.readObject();
//                blockChain.validate();
//            }
//        } else {
            blockChain = new BlockChain(proofLength, fileName);
//        }
        return blockChain;
    }

    private final int proofLength;
    private final String fileName;
    private final Stack<Block> blocks = new Stack<>();

    private BlockChain(int proofLength, String fileName) {
        this.proofLength = proofLength;
        this.fileName = fileName;
    }

    private void validate() {
        if (!blocks.empty()) {
            blocks.get(0).validate(null);
            for (int i = 1; i < blocks.size(); i++) {
                var block = blocks.get(i);
                block.validate(blocks.get(i-1).getHash());
            }
        }
    }

    public Block put() throws IOException {
        var id = blocks.size();
        var previous = blocks.size() == 0 ? null : blocks.peek().getHash();
        var block = new Block(id, proofLength, previous);
        blocks.add(block);
        save();
        return block;
    }

    private void save() throws IOException {
        var path = Paths.get(fileName);
        if (Files.exists(path)) {
            Files.delete(path);
        }
        try(var file = new FileOutputStream(fileName);
            var buffer = new BufferedOutputStream(file);
            var stream = new ObjectOutputStream(buffer)) {
            stream.writeObject(this);
        }
    }

    public Iterable<Block> blocks() {
        return this.blocks;
    }
}
