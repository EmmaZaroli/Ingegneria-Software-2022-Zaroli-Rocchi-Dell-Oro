package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;


/**
 * Character card factory.
 */
public class CharacterCardFactory {
    /**
     * depending on the character, the corresponding card is instantiated
     *
     * @param character the character
     * @return the character card created
     */
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
        return new CharacterCardWithSetUpAction(Character.CHARACTER_ONE.getInitialPrice(), Character.CHARACTER_ONE);
    }

    private CharacterCard get2() {
        return new CharacterCard(Character.CHARACTER_TWO.getInitialPrice(), Character.CHARACTER_TWO);
    }

    private CharacterCard get4() {
        return new CharacterCard(Character.CHARACTER_FOUR.getInitialPrice(), Character.CHARACTER_FOUR);
    }

    private CharacterCard get6() {
        return new CharacterCard(Character.CHARACTER_SIX.getInitialPrice(), Character.CHARACTER_SIX);
    }

    private CharacterCard get7() {
        return new CharacterCardWithSetUpAction(Character.CHARACTER_SEVEN.getInitialPrice(), Character.CHARACTER_SEVEN);
    }

    private CharacterCard get8() {
        return new CharacterCard(Character.CHARACTER_EIGHT.getInitialPrice(), Character.CHARACTER_EIGHT);
    }

    private CharacterCard get9() {
        return new CharacterCard(Character.CHARACTER_NINE.getInitialPrice(), Character.CHARACTER_NINE);
    }

    private CharacterCard get11() {
        return new CharacterCardWithSetUpAction(Character.CHARACTER_ELEVEN.getInitialPrice(), Character.CHARACTER_ELEVEN);
    }
}
