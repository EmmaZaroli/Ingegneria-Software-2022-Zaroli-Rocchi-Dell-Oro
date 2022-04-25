package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;

public class MoveStudentMessage extends Message {

    private final PawnColor studentColor;
    private IslandCard islandCard;

    public MoveStudentMessage(String nickname, MessageType messageType, PawnColor color) {
        super(nickname, messageType);
        this.studentColor = color;
    }

    public void setIslandCard(IslandCard island) {
        this.islandCard = island;
    }

    public PawnColor getStudentColor() {
        return this.studentColor;
    }

    public IslandCard getIslandCard() {
        return this.islandCard;
    }
}
