package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BlockChain implements Serializable {

    private static final long serialVersionUID = 1L;

    public static BlockChain load(Trader trader, String fileName) throws Exception {
        BlockChain blockChain;
        if (Files.exists(Paths.get(fileName))) {
            try (var file = new FileInputStream(fileName);
                 var buffer = new BufferedInputStream(file);
                 var stream = new ObjectInputStream(buffer)) {

                blockChain = (BlockChain) stream.readObject();
                blockChain.validate();
            }
        } else {
            blockChain = new BlockChain(trader, fileName);
        }
        return blockChain;
    }

    private String fileName;
    private final Stack<Block> blocks = new Stack<>();
    private int proofLength = 0;
    private int previousProofLength = 0;
    private Queue<Transaction> transactionsForTheNextBlock = new ArrayDeque<>();
    private int nextTransactionId = 1;
    private Trader trader;

    public BlockChain(Trader trader) {
        this.trader = trader;
        this.trader.setBlockChain(this);
    }

    public BlockChain(Trader trader, String fileName) {
        this(trader);
        this.fileName = fileName;
    }

    private Hash getLastBlockHash() {
        synchronized (blocks) {
            return blocks.size() == 0 ? null : blocks.peek().getHash();
        }
    }

    private boolean tryPut(Block block) throws IOException {
        synchronized (blocks) {
            if (block.getId() == getNextBlockId()) {
                blocks.add(block);
                adjustProofLength();
                save();
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
        synchronized (blocks) {
            var generatingTime = blocks.empty() ? 0 : blocks.peek().getGeneratingTime();
            if (generatingTime < 80) {
                previousProofLength = proofLength;
                proofLength++;
            } else if (generatingTime > 150) {
                previousProofLength = proofLength;
                proofLength--;
            }
        }
    }

    private void save() throws IOException {
        if (fileName == null) {
            return;
        }
        synchronized (blocks) {
            synchronized (transactionsForTheNextBlock) {
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

    private int getFunds(String owner) {
        var result = blocks()
                .flatMap(b -> ((TransactionList) b.getData()).transactions())
                .mapToInt(t -> t.getBalance(owner))
                .filter(v -> v != 0)
                .sum();
        return result;
    }

    private void validate() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        synchronized (blocks) {
            if (!blocks.empty()) {
                blocks.get(0).validate(null);
                for (int i = 1; i < blocks.size(); i++) {
                    var currentBlock = blocks.get(i);
                    var previousBlock = blocks.get(i - 1);
                    // Validate transactions
                    TransactionList previousBlockTransactions = previousBlock.getData();
                    previousBlockTransactions.validate();
                    TransactionList currentBlockTransactions = currentBlock.getData();
                    currentBlockTransactions.validate();
                    if (previousBlockTransactions
                            .transactions()
                            .mapToInt(m -> m.getId()).max().orElse(-1) >=
                            currentBlockTransactions
                                    .transactions()
                                    .mapToInt(m -> m.getId()).min().orElse(0)) {
                        throw new IllegalArgumentException("Transaction ids are not valid!");
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

    public int getNextTransactionId() {
        synchronized (transactionsForTheNextBlock) {
            return nextTransactionId++;
        }
    }

    public void enqueue(Transaction transaction) {
        synchronized (transactionsForTheNextBlock) {
            var lastTransactionId = transactionsForTheNextBlock.stream().mapToInt(m -> m.getId()).max().orElse(0);
            if (transaction.getId() <= lastTransactionId) {
                return; // reject the transaction due to invalid id
            }
            if (getFunds(transaction.getFrom()) < transaction.getValue()) {
                return; // reject the transaction due to insufficient funds
            }
            transactionsForTheNextBlock.add(transaction);
        }
    }

    public boolean tryPutNewBlock(String minerId)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        List<Transaction> blockTransactions;
        synchronized (transactionsForTheNextBlock) {
            blockTransactions = transactionsForTheNextBlock.isEmpty()
                    ? new ArrayList<>()
                    : transactionsForTheNextBlock
                    .stream()
                    .collect(toList());
        }
        var payment = trader.trade("BlockChain", minerId, 100);
        blockTransactions.add(payment);
        var blockId = getNextBlockId();
        var previous = getLastBlockHash();
        var block = new Block(
                minerId,
                blockId,
                proofLength,
                previous,
                Integer.compare(proofLength, previousProofLength),
                new TransactionList(blockTransactions)
        );
        var result = tryPut(block);
        if (result) {
            synchronized (transactionsForTheNextBlock) {
                transactionsForTheNextBlock.clear();
            }
        }
        return result;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
