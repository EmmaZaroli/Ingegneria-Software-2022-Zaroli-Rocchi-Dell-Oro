package it.polimi.ingsw.model;


import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.observer.Observable;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Player
 */
public class Player extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 9L;

    private final String nickname;
    private final Wizzard wizzard;
    private final SchoolBoard schoolBoard;
    private final LinkedList<AssistantCard> assistantDeck;
    private AssistantCard discardPileHead;
    private boolean isPlayerTurn;
    private boolean isOnline;

    /**
     * Instantiates a new Player, sets by default isPlayerTurn to false
     *
     * @param nickname the nickname of the player
     * @param wizzard  the wizzard's type
     * @param tower    the tower's color
     */
    public Player(String nickname, Wizzard wizzard, Tower tower, int numberOfPlayers) {
        this(nickname, wizzard, tower, false, numberOfPlayers, true);
    }

    /**
     * Instantiates a new Player, creating the Assistant's deck and the school board
     *
     * @param nickname        the nickname
     * @param wizzard         the wizzard
     * @param tower           the tower
     * @param numberOfPlayers the number of players of the game this player will be added to
     * @param isPlayerTurn
     */
    public Player(String nickname, Wizzard wizzard, Tower tower, boolean isPlayerTurn, int numberOfPlayers, boolean isOnline) {
        this.nickname = nickname;
        this.wizzard = wizzard;
        this.discardPileHead = null;

        this.isPlayerTurn = isPlayerTurn;
        this.schoolBoard = new SchoolBoard(numberOfPlayers == 2 ? 8 : 6, tower);
        this.assistantDeck = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
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
    public Wizzard getWizzard() {
        return this.wizzard;
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

    /**
     * Sets player turn
     *
     * @param playerTurn
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
        notify(a);
        assistantDeck.remove(a);

    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        //TODO notify
    }
}
