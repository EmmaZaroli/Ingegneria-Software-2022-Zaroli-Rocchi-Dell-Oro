package it.polimi.ingsw.network.message;

import java.io.Serializable;

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
