package it.polimi.ingsw.model;


import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizard;
import it.polimi.ingsw.observer.ModelObservable;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.utils.ApplicationConstants.*;

/**
 * Player
 */
public class Player extends ModelObservable implements Serializable {
    @Serial
    private static final long serialVersionUID = 9L;

    private final String nickname;
    private final Wizard wizard;
    private final SchoolBoard schoolBoard;
    private final LinkedList<AssistantCard> assistantDeck;
    private AssistantCard discardPileHead;
    private boolean isFromActualTurn = false;
    private boolean isPlayerTurn;
    private boolean isOnline;
    private boolean canPlayThisRound = true;

    /**
     * Instantiates a new Player, sets by default isPlayerTurn to false
     *
     * @param nickname the nickname of the player
     * @param wizard  the wizzard's type
     * @param tower    the tower's color
     */
    public Player(String nickname, Wizard wizard, Tower tower, int numberOfPlayers) {
        this(nickname, wizard, tower, false, numberOfPlayers, true);
    }

    /**
     * Instantiates a new Player, creating the Assistant's deck and the school board
     *
     * @param nickname        the nickname
     * @param wizard         the wizzard
     * @param tower           the tower
     * @param numberOfPlayers the number of players of the game this player will be added to
     * @param isPlayerTurn true if it's the player's turn
     */
    public Player(String nickname, Wizard wizard, Tower tower, boolean isPlayerTurn, int numberOfPlayers, boolean isOnline) {
        this.nickname = nickname;
        this.wizard = wizard;
        this.discardPileHead = null;

        this.isPlayerTurn = isPlayerTurn;
        this.schoolBoard = new SchoolBoard(numberOfPlayers == 2 ? TOWER_2_PLAYERS : TOWER_3_PLAYERS, tower);
        this.assistantDeck = new LinkedList<>();
        for (int i = 0; i < ASSISTANTS_IN_DECK; i++) {
            this.assistantDeck.add(new AssistantCard(i + 1, ((i / 2) + 1)));
        }
        this.isOnline = isOnline;
    }

    /**
     * Return the assistant card based on the index
     *
     * @param assistantIndex the assistant index
     * @return the assistant card
     */
    public AssistantCard getAssistant(int assistantIndex) {
        return assistantDeck.get(assistantIndex);
    }


    /**
     * return the list of assistant card still in the player's deck
     *
     * @return the assistant deck
     */
    public List<AssistantCard> getAssistantDeck() {
        return (LinkedList) ((LinkedList) assistantDeck).clone();
    }


    /**
     * @return the school board
     */
    public SchoolBoard getBoard() {
        return this.schoolBoard;
    }

    /**
     * Gets discard pile head, containing the last assistant card played by the player
     *
     * @return assistant card
     */
    public AssistantCard getDiscardPileHead() {
        return this.discardPileHead;
    }

    /**
     * Gets nickname.
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Gets wizzard.
     *
     * @return the wizzard's type
     */
    public Wizard getWizzard() {
        return this.wizard;
    }

    /**
     * @return true if the assistant's deck is empty, false otherwise
     */
    public boolean isDeckEmpty() {
        return assistantDeck.isEmpty();
    }

    /**
     * @return true if it's the player's turn, false otherwise
     */
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean canPlayThisRound() {
        return canPlayThisRound;
    }

    /**
     * Sets player turn
     *
     * @param playerTurn true if it's the player's turn
     */
    protected void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    /**
     * Toggle player turn.
     */
    protected void togglePlayerTurn() {
        isPlayerTurn = !isPlayerTurn;
    }

    /**
     * Plays assistant card by removing it from the deck and put it in the discard pile head
     *
     * @param a the assistant card
     */
    public void playAssistant(AssistantCard a) {
        this.discardPileHead = a;
        this.isFromActualTurn = true;
        notifyAssistantCard(a);
        removeAssistant(a);
    }

    /**
     *
     * @param a the assistant card to remove from the deck
     */
    public void removeAssistant(AssistantCard a){
        assistantDeck.remove(a);
    }

    /**
     * Notify to the views the current object
     * @param isOnline is true if the player is online
     */
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        notifyPlayerOnline(this);
    }

    /**
     *
     * @return the number of professor on the schoolBoard
     */
    public int getProfessorsCount(){
        return schoolBoard.getProfessorsCount();
    }

    public boolean isFromActualTurn() {
        return isFromActualTurn;
    }

    public void setFromActualTurn(boolean fromActualTurn) {
        isFromActualTurn = fromActualTurn;
    }

    public void setCanPlayThisRound(boolean canPlayThisRound) {
        this.canPlayThisRound = canPlayThisRound;
        notifyPlayerCanPlay(this);
    }
}
