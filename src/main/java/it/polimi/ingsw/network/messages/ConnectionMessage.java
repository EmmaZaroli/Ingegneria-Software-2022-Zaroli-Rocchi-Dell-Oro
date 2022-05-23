package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class ConnectionMessage extends Message {
    protected ConnectionMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }
}
