package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

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
