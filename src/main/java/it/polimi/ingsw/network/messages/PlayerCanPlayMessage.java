package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class PlayerCanPlayMessage extends Message {

    private final boolean canPlay;

    public PlayerCanPlayMessage(String nickname, boolean canPlay) {
        super(nickname, MessageType.UPDATE_PLAYER_CAN_PLAY);
        this.canPlay = canPlay;
    }

    public boolean canPlay() {
        return this.canPlay;
    }
}