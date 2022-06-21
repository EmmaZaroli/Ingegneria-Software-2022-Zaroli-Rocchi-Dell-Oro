package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.IslandCardDto;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message which contains the IslandCardDto
 */
public class IslandMessage extends Message {

    private final IslandCardDto island;

    public IslandMessage(MessageType messageType, IslandCard island) {
        super("server", messageType);
        this.island = new IslandCardDto(island);
    }

    public IslandCardDto getIsland() {
        return this.island;
    }

}
