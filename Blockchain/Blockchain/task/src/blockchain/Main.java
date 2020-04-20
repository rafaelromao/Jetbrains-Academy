package blockchain;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            var threadCount = Runtime.getRuntime().availableProcessors();
            var executor = Executors.newFixedThreadPool(threadCount);
            var blockChain = new BlockChain();
            for (var i = 0; i < threadCount; i++) {
                executor.submit(new Miner(blockChain, i));
            }
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
            var i = 0;
            for (var block: blockChain.blocks()) {
                if (i++ == 5) {
                    break;
                }
                System.out.println(block);
                System.out.println();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
