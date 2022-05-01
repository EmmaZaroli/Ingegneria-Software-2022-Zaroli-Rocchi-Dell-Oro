package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;

import java.io.Serial;
import java.io.Serializable;

/**
 * Character card.
 */
public class CharacterCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    private final int initialPrice;
    private boolean hasCoin;
    private final Character character;

    /**
     * Instantiates a new Character card.
     *
     * @param initialPrice the initial price
     * @param character    the character
     */
    protected CharacterCard(int initialPrice, Character character) {
        this.initialPrice = initialPrice;
        this.character = character;
        this.hasCoin = false;
    }

    /**
     * @return true if the card has already been used, false otherwise
     */
    public boolean hasCoin() {
        return this.hasCoin;
    }

    /**
     * @return the current price
     */
    public int getCurrentPrice() {
        return this.hasCoin ? this.initialPrice + 1 : this.initialPrice;
    }

    /**
     * @return the character
     */
    public Character getCharacter() {
        return this.character;
    }

    /**
     * Sets the card as used
     */
    protected void setUsed() {
        this.hasCoin = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CharacterCard)) return false;

        return ((CharacterCard) obj).character == this.character;
    }

    @Override
    protected Object clone() {
        CharacterCard retVal = new CharacterCard(this.initialPrice, this.character);
        retVal.setUsed();
        return retVal;
    }
}
