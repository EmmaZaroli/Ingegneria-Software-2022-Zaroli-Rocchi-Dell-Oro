package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;

import java.util.List;

public class CloudMessage extends Message {

    private CloudTile cloud;

    public CloudMessage(String nickname, MessageType messageType, CloudTile cloud) {
        super(nickname, messageType);
        this.cloud = cloud;
    }

    public CloudTile getCloud() {
        return this.cloud;
    }
}
