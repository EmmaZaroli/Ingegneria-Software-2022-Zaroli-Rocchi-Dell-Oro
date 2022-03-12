package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Wizzard;

public class Player {
    private final String nickname;
    private final Wizzard wizzard;
    private AssistantCard discardPileHead;

    public Player(String nickname, Wizzard wizzard) {
        this.nickname = nickname;
        this.wizzard = wizzard;
        this.discardPileHead = null;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Wizzard getWizzard() {
        return this.wizzard;
    }

    protected void playAssistant(AssistantCard a) {
        this.discardPileHead = a;
    }
}
