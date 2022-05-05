package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class GameStartingMessage extends Message {
    public GameStartingMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }
}
