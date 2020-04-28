package blockchain;

import java.io.File;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Chatter implements Runnable {
    private BlockChain blockChain;
    private String name;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Chatter(BlockChain blockChain, String name, String publicKeyFile, String privateKeyFile)
            throws Exception {
        this.blockChain = blockChain;
        this.name = name;
        this.publicKey = getPublic(publicKeyFile);
        this.privateKey = getPrivate(privateKeyFile);
    }

    public PrivateKey getPrivate(String filename) throws Exception {
        var keyBytes = Files.readAllBytes(new File(filename).toPath());
        var spec = new PKCS8EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public PublicKey getPublic(String filename) throws Exception {
        var keyBytes = Files.readAllBytes(new File(filename).toPath());
        var spec = new X509EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                var messageId = blockChain.getNextMessageId();
                var text = UUID.randomUUID().toString();
                var signature = sign(messageId, text);
                blockChain.queueMessage(new Message(messageId, name, text, publicKey, signature));
            } catch (InterruptedException | NoSuchAlgorithmException |
                     InvalidKeyException | SignatureException e) {
                e.printStackTrace();
            }
        }
    }

    private String sign(int messageId, String text)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        var rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        var data = messageId + name + text;
        rsa.update(data.getBytes());
        return String.valueOf(rsa.sign());
    }
}
