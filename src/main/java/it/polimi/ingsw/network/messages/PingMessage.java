package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 *  Message used to keep the connection alive
 */
public class PingMessage extends Message {
    public PingMessage(MessageType messageType) {
        super("", messageType);
    }
}
