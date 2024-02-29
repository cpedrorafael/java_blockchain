package blockchain;

public interface Transactable {
    String getReceiverName();
    void SendTransaction(String receiver, int amount);
}
