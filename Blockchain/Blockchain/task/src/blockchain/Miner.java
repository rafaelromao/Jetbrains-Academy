package blockchain;

import java.util.concurrent.TimeUnit;

public class Miner implements Runnable {
    private final BlockChain blockChain;
    private final String id;

    public Miner(BlockChain blockChain, String id) {
        this.blockChain = blockChain;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 1000));
                blockChain.tryPutNewBlock(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
