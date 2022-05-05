package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

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
