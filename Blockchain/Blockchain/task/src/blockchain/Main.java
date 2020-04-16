package blockchain;

public class Main {
    public static void main(String[] args) {
        var chain = new BlockChain();
        for (var i = 0; i < 5; i++) {
            chain.put();
        }
        System.out.println(chain);
    }
}
