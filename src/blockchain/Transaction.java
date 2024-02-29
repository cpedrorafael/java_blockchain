package blockchain;

public class Transaction {
    private final String sender;
    private final String receiver;
    private final int amount;

    public Transaction(String sender, String receiver, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    // Getters
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public int getAmount() { return amount; }

    @Override
    public String toString() {
        return sender + " sent " + amount + " VC to " + receiver;
    }
}
