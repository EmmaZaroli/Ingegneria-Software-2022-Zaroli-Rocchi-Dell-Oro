package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;

public class StandardCharacterCard extends CharacterCard {
    private final Effect effect;
    private final Effect reverseEffect;

    protected StandardCharacterCard(int initialPrice, Character character, Effect effect, Effect reverseEffect) {
        super(initialPrice, character);
        this.effect = effect;
        this.reverseEffect = reverseEffect;
    }

    public void activateEffect(ExpertGameParameters parameters) {
        this.setUsed();
        this.effect.activateEffect(parameters);
    }

    public void reverseEffect(ExpertGameParameters parameters) {
        this.reverseEffect.activateEffect(parameters);
    }
}
