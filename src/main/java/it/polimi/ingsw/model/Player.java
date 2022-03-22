package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private final String nickname;
    private final Wizzard wizzard;
    private AssistantCard discardPileHead;
    private boolean isPlayerTurn;
    private final SchoolBoard schoolBoard;
    private final List<AssistantCard> assistantDeck;

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
            this.assistantDeck.add(new AssistantCard(i + 1, ((int) (i / 2)) + 1));
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    public Wizzard getWizzard() {
        return this.wizzard;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public void togglePlayerTurn() {
        isPlayerTurn = !isPlayerTurn;
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    //TODO sicuramente da sistemare, espone il rep
    public List<AssistantCard> getAssistantDeck() {
        return assistantDeck;
    }

    public AssistantCard getAssistant(int assistantIndex) {
        return assistantDeck.get(assistantIndex);
    }

    protected void playAssistant(AssistantCard a) {
        if (!assistantDeck.contains(a)) {
            //TODO throw exception
        }
        this.discardPileHead = a;
        assistantDeck.remove(a);
    }

    protected void playAssistant(int assistantIndex) {
        if (assistantIndex >= assistantDeck.size() || assistantIndex < 0) {
            //TODO throw exception
        }
        this.discardPileHead = assistantDeck.get(assistantIndex);
        assistantDeck.remove(assistantIndex);
    }

    protected AssistantCard getDiscardPileHead() {
        return this.discardPileHead;
    }

    protected SchoolBoard getBoard() {
        return this.schoolBoard;
    }

    //TODO move to game
    public void tryStealProfessor(PawnColor color, Player player) {
        if (!getBoard().isThereProfessor(color) &&
                player.getBoard().isThereProfessor(color) &&
                getBoard().getStudentsInDinigRoom(color) > player.getBoard().getStudentsInDinigRoom(color)) {
            player.getBoard().removeProfessor(color);
            getBoard().addProfessor(color);
        }
    }
}
