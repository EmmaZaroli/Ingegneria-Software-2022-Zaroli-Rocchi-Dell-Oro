package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class ChangedPhaseMessage extends Message {

    private GamePhase newPhase;

    public ChangedPhaseMessage(String nickname, GamePhase newPhase) {
        super(nickname, MessageType.UPDATE_GAME_PHASE);
        this.newPhase = newPhase;
    }

    public GamePhase getNewPhase() {
        return newPhase;
    }
}
