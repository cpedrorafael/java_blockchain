package blockchain;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Blockchain {
    private final List<Block> chain;
    private int difficulty;
    private final long startTime;
    BlockChainDelegate delegate;
    private final int maxBlockNumber;
    private final CopyOnWriteArrayList<SignedMessage> currentMessages = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();

    private final AtomicInteger messageId = new AtomicInteger(1);

    private Map<String, Integer> balances = new HashMap<>();

    private final CopyOnWriteArrayList<String> logs = new CopyOnWriteArrayList<>();

    public Blockchain(BlockChainDelegate delegate, int maxBlockNumber) {
        this.chain = new ArrayList<>();
        // Genesis block
        this.chain.add(new Block(0, System.currentTimeMillis(), 0, "0",
                "0", 0, 0, transactions));
        this.difficulty = 3;
        this.startTime = System.currentTimeMillis();
        this.delegate = delegate;
        this.maxBlockNumber = maxBlockNumber;
    }

    void initBalances(int numberOfMiners, int numberOfUsers) {
        for (int i = 1; i <= numberOfMiners; i++) {
            balances.put("miner" + i, 100);
        }
        for (int i = 1; i <= numberOfUsers; i++) {
            balances.put("User" + i, 100);
        }
    }

    public int getNextMessageId() {
        return messageId.getAndIncrement();
    }

    public void addMessage(SignedMessage message) {
        currentMessages.add(message);
    }

    public synchronized boolean addTransaction(Transaction transaction) {
        String sender = transaction.getSender();
        int amount = transaction.getAmount();
        if (!balances.containsKey(sender) || balances.get(sender) < amount) {
            return false;
        }
        transactions.add(transaction);
        return true;
    }

    public int getBalance(String participant) {
        return balances.getOrDefault(participant, 100); // Everyone starts with 100 VCs
    }


    public List<SignedMessage> getMessages() {
        return currentMessages;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public synchronized String getPreviousHash() {
        if (this.chain.isEmpty()) {
            return "0";
        }
        return this.chain.get(this.chain.size() - 1).getHash();
    }

    private void rewardMiner(int minerId) {
        String miner = "miner" + minerId;
        balances.put(miner, balances.get(miner) + 100);
    }

    public synchronized int getNextBlockId() {
        return this.chain.size();
    }

    public synchronized void addBlock(Block newBlock) {
        if (isValidNewBlock(newBlock, this.chain.get(this.chain.size() - 1))) {
            List<Transaction> blockTransactions = new ArrayList<>(transactions);
            Block newBlockWithMessages = new Block(newBlock.getId(), newBlock.getTimestamp(),
                    newBlock.getMagicNumber(), newBlock.getPreviousHash(), newBlock.getHash(),
                    newBlock.getCreatedByMinerId(), newBlock.getGenerationDuration(), blockTransactions);
            currentMessages.clear();
            this.chain.add(newBlockWithMessages);
            rewardMiner(newBlock.getCreatedByMinerId());
            var difficulty = adjustDifficulty(newBlock);
            logs.add(newBlockWithMessages + "\n" + difficulty);
            if (newBlock.getId() == maxBlockNumber) {
                delegate.onMaxBlockAdded(logs);
            }
        }
    }

    private boolean isValidNewBlock(Block newBlock, Block previousBlock) {
        if (previousBlock.getId() + 1 != newBlock.getId()) {
            return false;
        }
        if (!previousBlock.getHash().equals(newBlock.getPreviousHash())) {
            return false;
        }
        return newBlock.getHash().startsWith(repeat(difficulty));
    }

    private String adjustDifficulty(Block lastBlock) {
        long blockGenerationTime = (lastBlock.getTimestamp() - startTime) ;
        if (blockGenerationTime < 100) {
            difficulty++;
            return "N was increased to " + difficulty + "\n";
        } else if (blockGenerationTime > 10000) {
            difficulty = Math.max(0, difficulty - 1);
            return "N was decreased by 1" + "\n";
        } else {
            return "N stays the same" + "\n";
        }
    }

    private String repeat(int times) {
        return new String(new char[times]).replace("\0", "0");
    }

    public int getDifficulty() {
        return difficulty;
    }


}
