package blockchain;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Miner implements Runnable {
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
                blockChain.tryPutNewBlock(id);
                TimeUnit.MILLISECONDS.sleep(2);
            }
        } catch (IOException | InterruptedException e) {
        }
    }
}
