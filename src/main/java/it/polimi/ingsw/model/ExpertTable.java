package it.polimi.ingsw.model;

import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;

public class ExpertTable extends Table {
    private int coins;

    public ExpertTable(PlayersNumber playersNumber) {
        super(playersNumber);
        coins = playersNumber == PlayersNumber.TWO ? 18 : 17;
    }

    public void takeCoin() {
        this.coins--;
    }

    public void depositCoins(int coins) {
        this.coins += coins;
    }

    public int getCoins() {
        return this.coins;
    }
}
