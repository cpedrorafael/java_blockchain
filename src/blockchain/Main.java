package blockchain;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Miner> miners = new ArrayList<>();
        Blockchain blockchain = new Blockchain((List<String> output) -> {
            for (String line : output
            ) {
                System.out.println(line);
            }
            miners.forEach(Miner::stopMining);
        },
                15);
        miners.addAll(List.of(
                new Miner(blockchain, 1),
                new Miner(blockchain, 2),
                new Miner(blockchain, 3),
                new Miner(blockchain, 4),
                new Miner(blockchain, 5)
        ));
        List<User> users = new ArrayList<>(List.of(
                new User(1, blockchain),
                new User(2, blockchain),
                new User(3, blockchain),
                new User(4, blockchain),
                new User(5, blockchain)
        ));

        blockchain.initBalances(5, 5);
        miners.forEach(Thread::start);
        users.forEach(user -> {
            for (int i = 0; i < 5; i++) {
                int randomMinerId = (int) (Math.random() * 10) + 1;
                user.SendTransaction("Miner" + randomMinerId, 10);
                int sendUserId = (int) (Math.random() * 5);
                sendUserId = sendUserId == i ? (sendUserId + 1) % 5 : sendUserId;
                String receiver = "User" + sendUserId;
                user.SendTransaction(receiver, 10);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        miners.forEach(miner -> {
            try {
                miner.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Main thread was interrupted.");
            }
        });

    }
}