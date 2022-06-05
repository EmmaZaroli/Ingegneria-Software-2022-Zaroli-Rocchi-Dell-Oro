package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class CoinMessage extends Message {

    //TODO do we need isOnTable?

    private final int coins;
    private final boolean isOnTable;
    //true: coin on table
    //false: coin on player

    public CoinMessage(String nickname, int coins, boolean isOnTable) {
        super(nickname, MessageType.UPDATE_COINS);
        this.coins = coins;
        this.isOnTable = isOnTable;
    }

    public int getCoins() {
        return this.coins;
    }

    public boolean isOnTable() {
        return isOnTable;
    }
}
