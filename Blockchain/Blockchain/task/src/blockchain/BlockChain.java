package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BlockChain implements Serializable {

    private static final long serialVersionUID = 1L;

    public static BlockChain load(String fileName)
            throws IOException, ClassNotFoundException,
                   NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
    private int previousProofLength = 0;
    private Queue<Message> messagesForTheNextBlock = new ArrayDeque<>();
    private int nextMessageId = 1;

    public BlockChain() {
    }

    public BlockChain(String fileName) {
        this();
        this.fileName = fileName;
    }

    private Hash getLastBlockHash() {
        synchronized (blocks) {
            return blocks.size() == 0 ? null : blocks.peek().getHash();
        }
    }

    private boolean put(Block block) throws IOException {
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

    private int getNextBlockId() {
        synchronized (blocks) {
            return blocks.size();
        }
    }

    private void adjustProofLength() {
        var idleTime = Duration.ofMillis(System.currentTimeMillis() - lastBlockTime);
        lastBlockTime = System.currentTimeMillis();
        if (idleTime.toSeconds() < 1) {
            previousProofLength = proofLength;
            proofLength++;
        } else if (idleTime.toMinutes() > 1) {
            previousProofLength = proofLength;
            proofLength--;
        }
    }

    private void save() throws IOException {
        synchronized (blocks) {
            synchronized (messagesForTheNextBlock) {
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
    }

    private void validate() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        synchronized (blocks) {
            if (!blocks.empty()) {
                blocks.get(0).validate(null);
                for (int i = 1; i < blocks.size(); i++) {
                    var currentBlock = blocks.get(i);
                    var previousBlock = blocks.get(i - 1);
                    // Validate messages
                    MessageList previousBlockMessages = previousBlock.getData();
                    previousBlockMessages.validate();
                    MessageList currentBlockMessages = currentBlock.getData();
                    currentBlockMessages.validate();
                    if (previousBlockMessages
                            .messages()
                            .mapToInt(m -> m.getId()).max().orElse(-1) >=
                        currentBlockMessages
                            .messages()
                            .mapToInt(m -> m.getId()).min().orElse(0)) {
                        throw new IllegalArgumentException("Message ids are not valid!");
                    }
                    // Validate block
                    currentBlock.validate(previousBlock.getHash());
                }
            }
        }
    }

    public Stream<Block> blocks() {
        synchronized (blocks) {
            return blocks.stream();
        }
    }

    public int getNextMessageId() {
        synchronized (messagesForTheNextBlock)        {
            return nextMessageId++;
        }
    }

    public void queueMessage(Message message) {
        synchronized (messagesForTheNextBlock) {
            var lastMessageId = messagesForTheNextBlock.stream().mapToInt(m -> m.getId()).max().orElse(0);
            if (message.getId() <= lastMessageId) {
                return;
            }
            messagesForTheNextBlock.add(message);
        }
    }

    public boolean tryPutNewBlock(int minerId) throws IOException {
        var blockId = getNextBlockId();
        var previous = getLastBlockHash();
        synchronized (messagesForTheNextBlock) {
            var block = new Block(
                    minerId,
                    blockId,
                    proofLength,
                    previous,
                    Integer.compare(proofLength, previousProofLength),
                    messagesForTheNextBlock.isEmpty()
                            ? null
                            : new MessageList(
                            messagesForTheNextBlock
                                    .stream()
                                    .collect(toList()))
            );
            var result = put(block);
            if (result) {
                messagesForTheNextBlock.clear();
            }
            return result;
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
