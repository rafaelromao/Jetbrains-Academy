package blockchain;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) throws Exception {
        var publicKeyFile = "C:\\Users\\rafae\\Downloads\\publicKey";
        var privateKeyFile = "C:\\Users\\rafae\\Downloads\\privateKey";
        var minersCount = Runtime.getRuntime().availableProcessors();
        var executor = Executors.newCachedThreadPool();
        var trader = new Trader(publicKeyFile, privateKeyFile);
        var blockChain = new BlockChain(trader);
        startMining(minersCount, executor, blockChain);
        startTrading(minersCount, executor, trader);
        TimeUnit.SECONDS.sleep(14);
        executor.shutdownNow();
        for (var block : blockChain
                .blocks()
                .limit(15)
                .collect(toList())) {
            System.out.println(block);
            System.out.println();
        }
    }

    private static void startMining(int minersCount, ExecutorService executor, BlockChain blockChain) {
        for (var i = 0; i < minersCount; i++) {
            executor.submit(new Miner(blockChain, "miner" + (i + 1)));
        }
    }

    private static void startTrading(int minersCount, ExecutorService executor, Trader trader) {
        executor.submit(() -> {
            var subjects = IntStream.range(0, minersCount)
                    .mapToObj(i -> "miner" + (i + 1))
                    .collect(toList());
            subjects.addAll(List.of("Nick", "CarShop", "Worker1", "Worker2", "Worker3"));
            try {
                trader.startTrading(subjects, minersCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
