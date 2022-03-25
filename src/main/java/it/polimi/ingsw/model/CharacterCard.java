package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.enums.Character;

public class CharacterCard {
    private final int initialPrice;
    private boolean hasCoin;
    private final Character character;


    protected CharacterCard(int initialPrice, Character character) {
        this.initialPrice = initialPrice;
        this.hasCoin = false;
        this.character = character;
    }

    public boolean hasCoins() {
        return this.hasCoin;
    }

    public int getCurrentPrice() {
        return this.hasCoin ? this.initialPrice + 1 : this.initialPrice;
    }

    public Character getCharacter() {
        return this.character;
    }

}
