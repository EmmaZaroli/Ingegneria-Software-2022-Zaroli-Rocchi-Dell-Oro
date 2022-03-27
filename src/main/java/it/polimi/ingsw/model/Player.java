package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private final String nickname;
    private final Wizzard wizzard;
    private final SchoolBoard schoolBoard;
    private final List<AssistantCard> assistantDeck;
    private AssistantCard discardPileHead;
    private boolean isPlayerTurn;

    public Player(String nickname, Wizzard wizzard, Tower tower) {
        this(nickname, wizzard, tower, false);
    }

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

    public AssistantCard getAssistant(int assistantIndex) {
        return assistantDeck.get(assistantIndex);
    }


    public List<AssistantCard> getAssistantDeck() {
        return (LinkedList) ((LinkedList) assistantDeck).clone();
    }


    public SchoolBoard getBoard() {
        return this.schoolBoard;
    }

    public AssistantCard getDiscardPileHead() {
        return this.discardPileHead;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Wizzard getWizzard() {
        return this.wizzard;
    }

    public boolean isDeckEmpty() {
        return assistantDeck.isEmpty();
    }

    //TODO maybe we don't need this anymore
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public void playAssistant(int assistantIndex) {
        if (assistantIndex >= assistantDeck.size() || assistantIndex < 0) {
            //TODO throw exception or let the controller do it
        }
        this.discardPileHead = assistantDeck.get(assistantIndex);
        assistantDeck.remove(assistantIndex);
    }

    public void togglePlayerTurn() {
        isPlayerTurn = !isPlayerTurn;
    }


    protected void playAssistant(AssistantCard a) {
        if (!assistantDeck.contains(a)) {
            //TODO throw exception or let the controller do it
        }
        this.discardPileHead = a;
        assistantDeck.remove(a);
    }
}
