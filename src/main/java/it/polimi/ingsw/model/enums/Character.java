package it.polimi.ingsw.model.enums;

public enum Character {
    CHARACTER_ONE(1),
    CHARACTER_TWO(2),
    CHARACTER_FOUR(1),
    CHARACTER_SIX(3),
    CHARACTER_SEVEN(1),
    CHARACTER_EIGHT(2),
    CHARACTER_NINE(3),
    CHARACTER_ELEVEN(2);

    private int initialPrice;

    public int getInitialPrice() {
        return initialPrice;
    }

    Character(int initialPrice) {
        this.initialPrice = initialPrice;
    }
}
