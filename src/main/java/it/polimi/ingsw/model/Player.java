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

    //TODO sicuramente da sistemare, espone il rep
    public List<AssistantCard> getAssistantDeck() {
        return assistantDeck;
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

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public Wizzard getWizzard() {
        return this.wizzard;
    }

    public boolean isDeckEmpty() {
        return assistantDeck.isEmpty();
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public void playAssistant(int assistantIndex) {
        if (assistantIndex >= assistantDeck.size() || assistantIndex < 0) {
            //TODO throw exception
        }
        this.discardPileHead = assistantDeck.get(assistantIndex);
        assistantDeck.remove(assistantIndex);
    }

    public void togglePlayerTurn() {
        isPlayerTurn = !isPlayerTurn;
    }

    //TODO move to game
    public void tryStealProfessor(PawnColor color, Player player) {
        if (!getBoard().isThereProfessor(color) &&
                player.getBoard().isThereProfessor(color) &&
                getBoard().getStudentsInDiningRoom(color) > player.getBoard().getStudentsInDiningRoom(color)) {
            try {
                player.getBoard().removeProfessor(color);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getBoard().addProfessor(color);
        }
    }

    protected void playAssistant(AssistantCard a) {
        if (!assistantDeck.contains(a)) {
            //TODO throw exception
        }
        this.discardPileHead = a;
        assistantDeck.remove(a);
    }
}
