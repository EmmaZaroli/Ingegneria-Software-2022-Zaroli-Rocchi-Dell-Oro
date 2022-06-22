package it.polimi.ingsw.network;

import java.io.Serializable;

/**
 * Abstract message class which must be extended by each message type.
 * Both server and clients will communicate using this generic type of message.
 * This avoids the usage of the "instance of" primitive.
 */
public abstract class Message implements Serializable {

    private final String nickname;
    private final MessageType messageType;

    protected Message(String nickname, MessageType messageType) {
        this.nickname = nickname;
        this.messageType = messageType;
    }

    public String getNickname() {
        return nickname;
    }

    public MessageType getType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "nickname=" + nickname +
                ", messageType=" + messageType +
                '}';
    }
}
