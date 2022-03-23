package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;

public class CharacterCardWithSetUpAction extends CharacterCard {
    public CharacterCardWithSetUpAction(int initialPrice, Character character, Effect effect, Effect reverseEffect) {
        super(initialPrice, character, effect, reverseEffect);
    }
}
