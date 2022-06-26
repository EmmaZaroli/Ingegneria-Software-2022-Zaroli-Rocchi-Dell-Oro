package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message which contains the updated total number of coins on the table or of a player
 */
public class CoinMessage extends Message {

    private final int coins;
    private final boolean isOnTable;

    /**
     *
     * @param nickname the nickname of the current player
     * @param coins the total number of coins
     * @param isOnTable true if the coins are on the table, false if they belong to a player
     */
    public CoinMessage(String nickname, int coins, boolean isOnTable) {
        super(nickname, MessageType.UPDATE_COINS);
        this.coins = coins;
        this.isOnTable = isOnTable;
    }

    /**
     *
     * @return the number of coins
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     *
     * @return true if the coins are on the table, false if they belong to a player
     */
    public boolean isOnTable() {
        return isOnTable;
    }
}
