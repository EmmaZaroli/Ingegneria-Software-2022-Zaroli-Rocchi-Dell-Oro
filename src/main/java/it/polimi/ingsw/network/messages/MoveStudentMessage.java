package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.IslandCardDto;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message which contains the PawnColor to move and its destination
 */
public class MoveStudentMessage extends Message {

    private final PawnColor studentColor;
    private IslandCardDto islandCard;

    public MoveStudentMessage(String nickname, MessageType messageType, PawnColor color) {
        super(nickname, messageType);
        this.studentColor = color;
    }

    /**
     *
     * @param island the islandCardDto
     */
    public void setIslandCard(IslandCardDto island) {
        this.islandCard = island;
    }

    //used only in tests
    public void setIslandCard(IslandCard island) {
        this.islandCard = new IslandCardDto(island);
    }

    /**
     *
     * @return the PawnColor
     */
    public PawnColor getStudentColor() {
        return this.studentColor;
    }

    /**
     *
     * @return the islandCardDto
     */
    public IslandCardDto getIslandCard() {
        return this.islandCard;
    }
}
