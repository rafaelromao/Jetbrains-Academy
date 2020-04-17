package blockchain;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var fileName = "C:\\Users\\rafae\\Downloads\\blockchain.data";
            var chain = BlockChain.load(n, fileName);
            for (var i = 0; i < 5; i++) {
                chain.put();
            }
            var i = 0;
            for (var block: chain.blocks()) {
                if (i++ == 5) break;
                System.out.println(block);
            }
        }
    }
}
