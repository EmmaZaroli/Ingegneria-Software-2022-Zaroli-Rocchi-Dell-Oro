package it.polimi.ingsw.network.messages;

public class GametypeResponseMessage extends Message {
    private final boolean isOk;

    public GametypeResponseMessage(String nickname, MessageType messageType, boolean isOk) {
        super(nickname, messageType);
        this.isOk = isOk;
    }

    public boolean isOk() {
        return isOk;
    }
}
