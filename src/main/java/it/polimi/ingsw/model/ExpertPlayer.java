package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

public class ExpertPlayer extends Player {
    private int coins;

    public ExpertPlayer(String nickname, Wizzard wizzard, Tower tower, int coins) {
        super(nickname, wizzard, tower);
        this.coins = coins;
    }

    public int getCoins() {
        return this.coins;
    }
}
