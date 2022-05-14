package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.CloudTileDto;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class CloudMessage extends Message {

    private CloudTileDto cloud;

    public CloudMessage(String nickname, MessageType messageType, CloudTile cloud) {
        super(nickname, messageType);
        this.cloud = new CloudTileDto(cloud);
    }

    public CloudTileDto getCloud() {
        return this.cloud;
    }
}
