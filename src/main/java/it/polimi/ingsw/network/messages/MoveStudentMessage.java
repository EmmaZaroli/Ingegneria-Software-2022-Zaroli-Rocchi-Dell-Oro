package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.IslandCardDto;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class MoveStudentMessage extends Message {

    private final PawnColor studentColor;
    private IslandCardDto islandCard;

    public MoveStudentMessage(String nickname, MessageType messageType, PawnColor color) {
        super(nickname, messageType);
        this.studentColor = color;
    }

    public void setIslandCard(IslandCardDto island) {
        this.islandCard = island;
    }

    public void setIslandCard(IslandCard island) {
        this.islandCard = new IslandCardDto(island);
    }

    public PawnColor getStudentColor() {
        return this.studentColor;
    }

    public IslandCardDto getIslandCard() {
        return this.islandCard;
    }
}
