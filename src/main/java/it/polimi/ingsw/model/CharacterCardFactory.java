package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;


/**
 * Character card factory.
 */
public class CharacterCardFactory {
    //Prevent this class to be instantiated
    private CharacterCardFactory() {}

    /**
     * Returns the card corresponding to the character
     *
     * @param character the character
     * @return the character card created
     */
    public static CharacterCard getCharacterCard(Character character) {
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

    private static CharacterCard get1() {
        return new CharacterCardWithSetUpAction(Character.CHARACTER_ONE.getInitialPrice(), Character.CHARACTER_ONE);
    }

    private static CharacterCard get2() {
        return new CharacterCard(Character.CHARACTER_TWO.getInitialPrice(), Character.CHARACTER_TWO);
    }

    private static CharacterCard get4() {
        return new CharacterCard(Character.CHARACTER_FOUR.getInitialPrice(), Character.CHARACTER_FOUR);
    }

    private static CharacterCard get6() {
        return new CharacterCard(Character.CHARACTER_SIX.getInitialPrice(), Character.CHARACTER_SIX);
    }

    private static CharacterCard get7() {
        return new CharacterCardWithSetUpAction(Character.CHARACTER_SEVEN.getInitialPrice(), Character.CHARACTER_SEVEN);
    }

    private static CharacterCard get8() {
        return new CharacterCard(Character.CHARACTER_EIGHT.getInitialPrice(), Character.CHARACTER_EIGHT);
    }

    private static CharacterCard get9() {
        return new CharacterCard(Character.CHARACTER_NINE.getInitialPrice(), Character.CHARACTER_NINE);
    }

    private static CharacterCard get11() {
        return new CharacterCardWithSetUpAction(Character.CHARACTER_ELEVEN.getInitialPrice(), Character.CHARACTER_ELEVEN);
    }
}
