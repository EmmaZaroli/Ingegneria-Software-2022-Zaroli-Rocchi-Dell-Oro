package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class IslandMessage extends Message {

    private final IslandCard island;

    public IslandMessage(MessageType messageType, IslandCard island) {
        super("server", messageType);
        this.island = island;
    }

    public IslandCard getIsland() {
        return this.island;
    }

}
