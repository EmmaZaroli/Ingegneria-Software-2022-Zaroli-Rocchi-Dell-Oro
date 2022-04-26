package it.polimi.ingsw.network.messages;

public class GameStartingMessage extends Message {
    public GameStartingMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }
}
