package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observable;

import java.io.Serial;
import java.io.Serializable;

/**
 * Character card.
 */
public class CharacterCard extends Observable implements Serializable {
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
        notify(new Message() {
        });
    }
}
