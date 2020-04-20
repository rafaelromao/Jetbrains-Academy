package blockchain;

public class Miner implements Runnable {
    private static volatile int proofLength;
    private static volatile int prints;

    private final BlockChain blockChain;
    private final int id;

    public Miner(BlockChain blockChain, int id) {
        this.blockChain = blockChain;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (Miner.class) {
                    var blockId = blockChain.getNextBlockId();
                    var previous = blockChain.getLastBlockHash();
                    int oldProofLength;
                    oldProofLength = proofLength;
                    proofLength = blockChain.getProofLength();
                    var block = new Block(
                            id,
                            blockId,
                            proofLength,
                            previous,
                            Integer.compare(oldProofLength, proofLength));
                    blockChain.put(block);
                }
            }
        } catch (Exception e) {
        }
    }
}
