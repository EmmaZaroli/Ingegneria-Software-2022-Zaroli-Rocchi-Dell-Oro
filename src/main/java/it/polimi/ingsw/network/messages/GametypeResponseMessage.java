package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message used by the server to confirm or discard the GametypeRequestMessage
 */
public class GametypeResponseMessage extends Message {
    private final boolean isOk;

    public GametypeResponseMessage(String nickname, boolean isOk) {
        super(nickname, MessageType.GAME_TYPE_RESPONSE);
        this.isOk = isOk;
    }

    public boolean isOk() {
        return isOk;
    }
}
