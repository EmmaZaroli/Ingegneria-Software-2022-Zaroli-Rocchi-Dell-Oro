package it.polimi.ingsw.network.messages;

public class CoinMessage extends Message {

    private final int coins;

    public CoinMessage(String nickname, int coins) {
        super(nickname, MessageType.COINS);
        this.coins = coins;
    }

    public int getCoins() {
        return this.coins;
    }
}
