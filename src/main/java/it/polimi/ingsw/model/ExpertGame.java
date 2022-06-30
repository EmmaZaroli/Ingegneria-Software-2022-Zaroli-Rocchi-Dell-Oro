package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

import java.util.Arrays;
import java.util.List;

/**
 * Expert Game
 */
public class ExpertGame extends Game {

    private CharacterCard[] characterCards;

    /**
     * Instantiates a new Expert Game
     * @param players the list of players
     * @param table the table
     * @param parameters the Expert Game parameters
     */
    public ExpertGame(ExpertPlayer[] players, ExpertTable table, ExpertGameParameters parameters) {
        super(players, table, parameters);
    }

    /**
     *
     * @return a copy of the array containing the character cards
     */
    public CharacterCard[] getCharacterCards() {
        return characterCards.clone();
    }

    @Override
    public ExpertPlayer[] getPlayers() {
        return (ExpertPlayer[]) super.getPlayers();
    }

    @Override
    public ExpertTable getTable() {
        return (ExpertTable) super.getTable();
    }

    @Override
    public ExpertGameParameters getParameters() {
        return (ExpertGameParameters) super.getParameters();
    }

    /**
     *
     * @param cards the array of character cards
     */
    public void setCharacterCards(CharacterCard[] cards) {
        this.characterCards = cards;
    }

    /**
     * Set as used the character card
     * Notify the view
     * @param c the card that was activated
     */
    public void useCharacterCard(CharacterCard c) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    x.setUsed();
                    notifyCharacterCard(x, new Object[0]);
                });
        expertParameters(getParameters());
    }

    /**
     * Add a student to the character card
     * @param c the character card
     * @param student teh student to add
     */
    public void addStudent(CharacterCard c, PawnColor student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).addStudent(student);
                });
    }

    /**
     * Add a list of students to the character card
     * @param c the character card
     * @param student the list of students to add
     */
    public void addStudent(CharacterCard c, List<PawnColor> student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).addStudent(student);
                });
    }

    /**
     * Remove a student from the character card
     * @param c the character card
     * @param student the student to remove
     */
    public void removeStudent(CharacterCard c, PawnColor student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).removeStudent(student);
                });
    }

    /**
     * Decrease the player's number of coins
     * Notify the view that the player's number of coins has changed
     * @param player the player who took the coins
     */
    public void addCoin(ExpertPlayer player) {
        player.addCoin();
        notifyPlayerCoins(player.getCoins());
    }

    /**
     * Decrease the player's number of coins
     * Notify the view that the player's number of coins has changed
     * @param player the player that spent the coins
     * @param n the number of coins spent
     */
    public void decreaseCoins(ExpertPlayer player, int n) {
        player.decreaseCoins(n);
        notifyPlayerCoins(player.getCoins());
    }

    /**
     * Notify the views that some parameters have been change
     * @param parameters the Expert Game parameters
     */
    public void expertParameters(ExpertGameParameters parameters){
        notifyExpertParameters(parameters);
    }
}
