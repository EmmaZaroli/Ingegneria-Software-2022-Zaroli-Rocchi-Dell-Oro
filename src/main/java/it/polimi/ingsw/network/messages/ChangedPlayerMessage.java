package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class ChangedPlayerMessage extends Message {

    public ChangedPlayerMessage(String nickname) {
        super(nickname, MessageType.CHANGE_PLAYER);
    }
}
