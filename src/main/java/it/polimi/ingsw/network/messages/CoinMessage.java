package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class CoinMessage extends Message {

    private final int coins;

    public CoinMessage(String nickname, int coins) {
        super(nickname, MessageType.UPDATE_COINS);
        this.coins = coins;
    }

    public int getCoins() {
        return this.coins;
    }
}
