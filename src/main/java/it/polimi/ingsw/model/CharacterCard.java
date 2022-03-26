package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;

public abstract class CharacterCard {
    private final int initialPrice;
    private boolean hasCoin;
    private final Character character;

    protected CharacterCard(int initialPrice, Character character) {
        this.initialPrice = initialPrice;
        this.character = character;
        this.hasCoin = false;
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

    protected void setUsed() {
        this.hasCoin = true;
    }
}
