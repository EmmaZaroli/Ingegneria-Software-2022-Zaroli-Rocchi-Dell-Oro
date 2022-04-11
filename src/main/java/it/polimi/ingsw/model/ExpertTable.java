package it.polimi.ingsw.model;

public class ExpertTable extends Table {
    private int coins;

    public ExpertTable(int playersCount) {
        super(playersCount);
        coins = playersCount == 2 ? 18 : 17;
    }

    public void takeCoin() {
        this.coins--;
    }

    public void depositCoins(int coins) {
        this.coins += coins;
    }
}
