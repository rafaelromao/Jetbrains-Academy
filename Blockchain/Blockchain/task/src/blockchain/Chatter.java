package blockchain;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Chatter implements Runnable {
    private BlockChain blockChain;
    private String name;

    public Chatter(BlockChain blockChain, String name) {
        this.blockChain = blockChain;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                blockChain.queueMessage(new Message(name, UUID.randomUUID().toString()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
