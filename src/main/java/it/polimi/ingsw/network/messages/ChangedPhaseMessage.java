package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 *  Message which contains the current GamePhase
 *  Used by the server to notify the new GamePhase to the players
 */
public class ChangedPhaseMessage extends Message {

    private final GamePhase newPhase;

    public ChangedPhaseMessage(String nickname, GamePhase newPhase) {
        super(nickname, MessageType.UPDATE_GAME_PHASE);
        this.newPhase = newPhase;
    }

    /**
     *
     * @return the new GamePhase
     */
    public GamePhase getNewPhase() {
        return newPhase;
    }
}
