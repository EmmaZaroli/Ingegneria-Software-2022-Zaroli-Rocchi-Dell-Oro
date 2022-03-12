package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;

public class CharacterCard {
    private final int initialPrice;
    private int coins;
    private final Character character;
    private final Effect effect;

    public CharacterCard(int initialPrice, Character character, Effect effect) {
        this.initialPrice = initialPrice;
        this.coins = 0;
        this.character = character;
        this.effect = effect;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getCurrentPrice() {
        return this.initialPrice + this.coins;
    }

    public Character getCharacter() {
        return this.character;
    }

    public Effect getEffect() {
        return this.effect;
    }
}
