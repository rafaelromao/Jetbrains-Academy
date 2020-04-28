package blockchain;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.stream.Stream;

public class TransactionList implements Serializable {
    private final List<Transaction> transactions;

    public TransactionList(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Stream<Transaction> transactions() {
        return transactions.stream();
    }

    public void validate() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        for (var transaction : transactions) {
            transaction.validate();
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var transaction: transactions) {
            sb.append(transaction);
            sb.append("\n");
        }
        return sb.toString();
    }
}
