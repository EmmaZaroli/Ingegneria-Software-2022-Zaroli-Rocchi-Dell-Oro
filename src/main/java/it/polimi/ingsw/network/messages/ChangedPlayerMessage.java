package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 *  Message which contains the nickname of the current player
 *  Used by the server to notify the new current player
 */
public class ChangedPlayerMessage extends Message {

    public ChangedPlayerMessage(String nickname) {
        super(nickname, MessageType.CHANGE_PLAYER);
    }
}
