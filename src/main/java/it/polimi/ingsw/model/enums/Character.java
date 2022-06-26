package it.polimi.ingsw.model.enums;

/**
 * This enum contains all possible CharacterCards
 */
public enum Character {
    CHARACTER_ONE(1),
    CHARACTER_TWO(2),
    CHARACTER_FOUR(1),
    CHARACTER_SIX(3),
    CHARACTER_SEVEN(1),
    CHARACTER_EIGHT(2),
    CHARACTER_NINE(3),
    CHARACTER_ELEVEN(2);

    private final int initialPrice;

    /**
     *
     * @return the character card's initial price
     */
    public int getInitialPrice() {
        return initialPrice;
    }

    /**
     *
     * @param initialPrice the character card's initial price
     */
    Character(int initialPrice) {
        this.initialPrice = initialPrice;
    }
}
