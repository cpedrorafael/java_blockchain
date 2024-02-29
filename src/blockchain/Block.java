package blockchain;

import java.util.List;
import java.util.stream.Collectors;

public class Block {
    private final int id;
    private final long timestamp;
    private final long magicNumber;
    private final String previousHash;
    private final String hash;
    private final int createdByMinerId;
    private final int generationDuration;

    private List<Transaction> transactions;


    public Block(int id, long timestamp, long magicNumber, String previousHash, String hash,
                 int createdByMinerId, int generationDuration, List<Transaction> transactions) {
        this.id = id;
        this.timestamp = timestamp;
        this.magicNumber = magicNumber;
        this.previousHash = previousHash;
        this.hash = hash;
        this.createdByMinerId = createdByMinerId;
        this.generationDuration = generationDuration;
        this.transactions = transactions;
    }


    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getCreatedByMinerId() {
        return createdByMinerId;
    }

    public int getGenerationDuration() {
        return generationDuration;
    }

    public long getMagicNumber() {
        return magicNumber;
    }


    @Override
    public String toString() {
        String joinedTransactions = transactions.stream()
                .map(Transaction::toString)
                .collect(Collectors.joining("\n"));
        String transactionData = transactions.isEmpty() ? "No transactions" : joinedTransactions;
        return "Block:\n" +
                "Created by miner" + createdByMinerId + "\n" +
                "miner" + createdByMinerId + " gets 100 VC\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" + previousHash + "\n" +
                "Hash of the block:\n" + hash + "\n" +
                "Block data:\n" + transactionData + "\n" +
                "Block was generating for " + generationDuration + " seconds";
    }

}
