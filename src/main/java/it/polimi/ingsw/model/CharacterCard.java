package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.enums.Character;

public class CharacterCard {
    private final int initialPrice;
    private boolean hasCoin;
    private final Character character;
    private final Effect effect;
    private final Effect reverseEffect;

    public CharacterCard(int initialPrice, Character character, Effect effect, Effect reverseEffect) {
        this.initialPrice = initialPrice;
        this.hasCoin = false;
        this.character = character;
        this.effect = effect;
        this.reverseEffect = reverseEffect;
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

    public void activateEffect(GameParameters parameters) {
        this.effect.activateEffect(parameters);
    }
}
