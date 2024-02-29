package blockchain;

import java.util.List;

public interface BlockChainDelegate {
    void onMaxBlockAdded(List<String> transactions);
}
