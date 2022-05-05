package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.SchoolBoard;

public class PlayerInfo {
    private String nickname;
    private SchoolBoard schoolBoard;
    private AssistantCard discardPileHead;
    private int coins;

    public PlayerInfo() {
        this.nickname = "";
        this.coins = 0;
    }

    private PlayerInfo(String nickname, SchoolBoard schoolBoard, AssistantCard discardPileHead, int coins) {
        this.nickname = nickname;
        this.schoolBoard = schoolBoard;
        this.discardPileHead = discardPileHead;
        this.coins = coins;
    }

    public PlayerInfo with(String nickname) {
        PlayerInfo retVal = this.deepClone();
        retVal.nickname = nickname;
        return retVal;
    }

    public PlayerInfo with(AssistantCard assistantCard) {
        PlayerInfo retVal = this.deepClone();
        retVal.discardPileHead = assistantCard;
        return retVal;
    }

    public PlayerInfo with(SchoolBoard board) {
        PlayerInfo retVal = this.deepClone();
        retVal.schoolBoard = board;
        return retVal;
    }

    public PlayerInfo with(int coins) {
        this.coins = coins;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public SchoolBoard getBoard() {
        return schoolBoard;
    }

    public AssistantCard getDiscardPileHead() {
        return discardPileHead;
    }

    public int getCoins() {
        return coins;
    }

    public PlayerInfo deepClone() {
        //TODO dtos if we have time
        return new PlayerInfo(this.nickname, this.schoolBoard, this.discardPileHead, this.coins);
    }
}
