package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class PingMessage extends Message {
    protected PingMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }
}
