package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Wizzard;

public class ExpertPlayer extends Player {
    private int coins;

    public ExpertPlayer(String nickname, Wizzard wizzard, int coins) {
        super(nickname, wizzard);
        this.coins = coins;
    }

    public int getCoins() {
        return this.coins;
    }
}
