package blockchain;

import java.io.File;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Trader {
    private BlockChain blockChain;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Trader(String publicKeyFile, String privateKeyFile)
            throws Exception {
        this.publicKey = getPublic(publicKeyFile);
        this.privateKey = getPrivate(privateKeyFile);
    }

    public Trader(BlockChain blockChain, String publicKeyFile, String privateKeyFile)
            throws Exception {
        this(publicKeyFile, privateKeyFile);
        this.blockChain = blockChain;
    }

    public void setBlockChain(BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    private PrivateKey getPrivate(String filename) throws Exception {
        var keyBytes = Files.readAllBytes(new File(filename).toPath());
        var spec = new PKCS8EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey getPublic(String filename) throws Exception {
        var keyBytes = Files.readAllBytes(new File(filename).toPath());
        var spec = new X509EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public Transaction trade(String from, String to, int value)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        if (blockChain == null) {
            throw new IllegalArgumentException("BlockChain is not defined!");
        }
        var transactionId = blockChain.getNextTransactionId();
        var signature = sign(transactionId, from, to, value);
        return new Transaction(transactionId, from, to, value, publicKey, signature);
    }

    private String sign(int transactionId, String from, String to, int value)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        var rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        var data = transactionId + from + to + value;
        rsa.update(data.getBytes());
        return String.valueOf(rsa.sign());
    }


    public void startTrading(List<String> subjects, int primeSubjects)
            throws InterruptedException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        TimeUnit.MILLISECONDS.sleep(1500);

        var size = subjects.size();
        var transactionCount = 0;

        while (true) {
            var rand = (int) (Math.random() * size);
            var fromIndex = transactionCount < primeSubjects / 2
                    ? rand % primeSubjects
                    : rand % size;

            var toIndex = fromIndex;
            while (toIndex == fromIndex) {
                rand = (int) (Math.random() * size);
                toIndex = transactionCount < primeSubjects / 2
                        ? (rand % (size - primeSubjects)) + primeSubjects + 1
                        : rand % size;
            }

            var from = subjects.get(fromIndex);
            var to = subjects.get(toIndex);
            var value = ((5 * ++transactionCount) % 145) + 5;

            var transaction = trade(from, to, value);
            blockChain.enqueue(transaction);
        }
    }
}
