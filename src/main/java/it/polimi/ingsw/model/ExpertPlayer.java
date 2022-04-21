package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

/**
 * Expert player.
 */
public class ExpertPlayer extends Player {
    private int coins;

    /**
     * Instantiates a new Expert player.
     *
     * @param nickname the player's nickname
     * @param wizzard  the wizzard
     * @param tower    the tower's color
     */
    public ExpertPlayer(String nickname, Wizzard wizzard, Tower tower, int numberOfPlayers) {
        super(nickname, wizzard, tower, numberOfPlayers);
        this.coins = 1;
    }

    /**
     * @return the number of coins the player has
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Add a coin
     */
    public void addCoin() {
        this.coins++;
        notify(coins);
    }

    /**
     * remove n coins from the player
     *
     * @param n The coins to remove
     */
    public void decreaseCoins(int n) {
        this.coins -= n;
        notify(coins);
    }
}
