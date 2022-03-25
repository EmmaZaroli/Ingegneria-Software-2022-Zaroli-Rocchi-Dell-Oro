package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;

public class CharacterCardStandard extends CharacterCard {
    private final Effect effect;
    private final Effect reverseEffect;

    public CharacterCardStandard(int initialPrice, Character character, Effect effect, Effect reverseEffect) {
        super(initialPrice, character);
        this.effect = effect;
        this.reverseEffect = reverseEffect;
    }

    public void activateEffect(GameParameters parameters) {
        this.effect.activateEffect(parameters);
    }

    public void reverseEffect(GameParameters parameters) {
        this.reverseEffect.activateEffect(parameters);
    }
}
