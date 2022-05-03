package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.SchoolBoard;

public class PlayerInfo {
    private String nickname;
    private SchoolBoard schoolBoard;
    private AssistantCard discardPileHead;
    private int coins;

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
}
