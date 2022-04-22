package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.CloudTile;

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
