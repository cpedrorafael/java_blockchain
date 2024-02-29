package blockchain;

import java.util.concurrent.ThreadLocalRandom;

public class Miner extends Thread implements Transactable {
    private final Blockchain blockchain;
    private final int minerId;
    private volatile boolean running = true;

    public Miner(Blockchain blockchain, int minerId) {
        this.blockchain = blockchain;
        this.minerId = minerId;
    }

    @Override
    public void run() {
        while (running) {
            long timestamp = System.currentTimeMillis();
            long magicNumber;
            String previousHash = blockchain.getPreviousHash();
            int currentDifficulty = blockchain.getDifficulty();
            String hash;

            do {
                magicNumber = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
                String dataToHash = previousHash + timestamp + magicNumber;
                hash = StringUtil.applySha256(dataToHash);
            } while (running && !hash.startsWith("0".repeat(currentDifficulty)));

            if (!running) {
                break;
            }

            int generationDuration = (int) ((System.currentTimeMillis() - timestamp) / 1000);
            var messages = blockchain.getTransactions();
            Block newBlock = new Block(blockchain.getNextBlockId(), timestamp, magicNumber, previousHash, hash, minerId, generationDuration, messages);
            blockchain.addBlock(newBlock);
        }
    }

    public void stopMining() {
        running = false;
    }

    @Override
    public String getReceiverName() {
       return "Miner" + minerId;
    }

    @Override
    public void SendTransaction(String receiver, int amount) {
        try {
            var transaction = new Transaction("Miner" + minerId, receiver, amount);
            blockchain.addTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
