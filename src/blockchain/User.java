package blockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class User implements Transactable {
    private final int id;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private final Blockchain blockchain;

    public User(int id, Blockchain blockchain) {
        this.id = id;
        this.blockchain = blockchain;
        generateKeys();
    }

    public void generateKeys() {
        try {
            KeyPair keyPair = CryptoUtil.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            var id = blockchain.getNextMessageId();
            byte[] signature = CryptoUtil.signMessage(id + message, privateKey);
            var signedMessage = new SignedMessage(
                    id,
                    message,
                    signature,
                    publicKey
            );
            blockchain.addMessage(signedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getIdForTransactions() {
        return id;
    }

    @Override
    public String getReceiverName() {
       return "User" + id;
    }

    @Override
    public void SendTransaction(String receiver, int amount) {
        try {
            var transaction = new Transaction("User" + id, receiver, amount);
            blockchain.addTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
