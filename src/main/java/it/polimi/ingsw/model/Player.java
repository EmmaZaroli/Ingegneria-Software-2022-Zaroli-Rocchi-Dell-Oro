package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

public class Player {
    private final String nickname;
    private final Wizzard wizzard;
    private AssistantCard discardPileHead;
    private final SchoolBoard schoolBoard;

    public Player(String nickname, Wizzard wizzard) {
        this.nickname = nickname;
        this.wizzard = wizzard;
        this.discardPileHead = null;
        this.schoolBoard = new SchoolBoard(7, Tower.BLACK); //TODO
    }

    public String getNickname() {
        return this.nickname;
    }

    public Wizzard getWizzard() {
        return this.wizzard;
    }

    public AssistantCard getDiscardPileHead() {
        return discardPileHead;
    }

    protected void playAssistant(int a) {
        //TODO
    }

    protected SchoolBoard getBoard() {
        return this.schoolBoard;
    }
}
