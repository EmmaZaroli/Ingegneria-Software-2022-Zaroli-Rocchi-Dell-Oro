package it.polimi.ingsw.network.messages;

public class ChangedPhaseMessage extends Message {

    private String phrase;

    public ChangedPhaseMessage(String nickname, MessageType messageType, String string) {
        super(nickname, messageType);
        this.phrase = string;
    }

    public String getPhrase() {
        return phrase;
    }

}
