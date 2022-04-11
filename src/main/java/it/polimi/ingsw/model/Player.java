package it.polimi.ingsw.model;


import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
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

    /**
     * Instantiates a new Player, sets by default isPlayerTurn to false
     *
     * @param nickname the nickname of the player
     * @param wizzard  the wizzard's type
     * @param tower    the tower's color
     */
    public Player(String nickname, Wizzard wizzard, Tower tower) {
        this(nickname, wizzard, tower, false);
    }

    /**
     * Instantiates a new Player, creating the Assistant's deck and the school board
     *
     * @param nickname     the nickname
     * @param wizzard      the wizzard
     * @param tower        the tower
     * @param isPlayerTurn
     */
    public Player(String nickname, Wizzard wizzard, Tower tower, boolean isPlayerTurn) {
        this.nickname = nickname;
        this.wizzard = wizzard;
        this.discardPileHead = null;

        this.isPlayerTurn = isPlayerTurn;
        this.schoolBoard = new SchoolBoard(8, tower);
        //TODO parametrizzare numero di torri
        this.assistantDeck = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            //TODO parametrizzare il numero di carte
            this.assistantDeck.add(new AssistantCard(i + 1, ((i / 2) + 1)));
        }
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
//TODO maybe we don't need this anymore
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Sets player turn
     *
     * @param playerTurn
     */
    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    /**
     * Toggle player turn.
     */
    public void togglePlayerTurn() {
        isPlayerTurn = !isPlayerTurn;
    }


    /**
     * Plays assistant card by removing it from the deck and put it in the discard pile head
     *
     * @param a the assistant card
     */
    public void playAssistant(AssistantCard a) {
        if (!assistantDeck.contains(a)) {
            //TODO throw exception or let the controller do it
        }
        this.discardPileHead = a;
        notify(a);
        assistantDeck.remove(a);

    }
}
