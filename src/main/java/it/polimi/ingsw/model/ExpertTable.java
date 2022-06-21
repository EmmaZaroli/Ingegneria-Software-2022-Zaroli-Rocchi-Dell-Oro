package it.polimi.ingsw.model;

import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;

/**
 * Expert Table
 */
public class ExpertTable extends Table {
    private int coins;

    /**
     * Instantiate a new ExpertTable
     * @param playersNumber the number of player's
     */
    public ExpertTable(PlayersNumber playersNumber) {
        super(playersNumber);
        coins = playersNumber == PlayersNumber.TWO ? 18 : 17;
    }

    /**
     * Remove a coin from the table
     * Notify the views the number of coins on the table
     */
    public void takeCoin() {
        this.coins--;
        notifyTableCoins(this.coins);
    }

    /**
     * Adds the coins on the table
     * Notify the views the number of coins on the table
     * @param coins the number of coins to add
     */
    public void depositCoins(int coins) {
        this.coins += coins;
        notifyTableCoins(this.coins);
    }

    /**
     *
     * @return the number of coins on the table
     */
    public int getCoins() {
        return this.coins;
    }
}
