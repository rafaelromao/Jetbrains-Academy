package blockchain;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            var publicKeyFile = "C:\\Users\\rafae\\Downloads\\publicKey";
            var privateKeyFile = "C:\\Users\\rafae\\Downloads\\privateKey";
            var threadCount = Runtime.getRuntime().availableProcessors();
            var executor = Executors.newFixedThreadPool(threadCount);
            var blockChain = new BlockChain();
            for (var i = 0; i < threadCount / 2; i++) {
                executor.submit(new Miner(blockChain, i));
                executor.submit(new Chatter(blockChain, "Chatter " + i, publicKeyFile, privateKeyFile));
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
            for (var block: blockChain
                    .blocks()
                    .limit(5)
                    .collect(Collectors.toList())) {
                System.out.println(block);
                System.out.println();
            }
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
