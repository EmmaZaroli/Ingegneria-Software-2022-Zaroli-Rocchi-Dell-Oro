package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.List;

public class CharacterCardFactory {
    public CharacterCard getCharacterCard(Character character) {
        return switch (character) {
            case CHARACTER_ONE -> get1();
            case CHARACTER_TWO -> get2();
            case CHARACTER_FOUR -> get4();
            case CHARACTER_SIX -> get6();
            case CHARACTER_SEVEN -> get7();
            case CHARACTER_EIGHT -> get8();
            case CHARACTER_NINE -> get9();
            case CHARACTER_ELEVEN -> get11();
        };
    }

    private CharacterCard get1() {
        return new CharacterCardWithSetUpAction(1, Character.CHARACTER_ONE);
    }

    private CharacterCard get2() {
        return new CharacterCard(2, Character.CHARACTER_TWO);
    }

    private CharacterCard get4() {
        return new CharacterCard(1, Character.CHARACTER_FOUR);
    }

    private CharacterCard get6() {
        return new CharacterCard(3, Character.CHARACTER_SIX);
    }

    private CharacterCard get7() {
        return new CharacterCardWithSetUpAction(1, Character.CHARACTER_SEVEN);
    }

    private CharacterCard get8() {
        return new CharacterCard(2, Character.CHARACTER_EIGHT);
    }

    private CharacterCard get9() {
        return new CharacterCard(3, Character.CHARACTER_NINE);
    }

    private CharacterCard get11() {
        return new CharacterCardWithSetUpAction(2, Character.CHARACTER_ELEVEN);
    }
}
