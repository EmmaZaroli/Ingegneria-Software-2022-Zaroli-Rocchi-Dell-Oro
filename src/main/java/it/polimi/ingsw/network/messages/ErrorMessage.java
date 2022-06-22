package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message which contains an error occurred on the server
 */
public class ErrorMessage extends Message {

    private final String error;

    public ErrorMessage(String nickname, String error) {
        super(nickname, MessageType.ERROR);
        this.error = error;
    }

    public String getError() {
        return this.error;
    }
}
