package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

public class ExpertPlayer extends Player {
    private int coins;

    public ExpertPlayer(String nickname, Wizzard wizzard, Tower tower) {
        super(nickname, wizzard, tower);
        this.coins = 1;
    }

    public int getCoins() {
        return this.coins;
    }

    public void addCoin() {
        this.coins++;
    }

    public void decreaseCoins(int n){
        this.coins += n;
    }
}
