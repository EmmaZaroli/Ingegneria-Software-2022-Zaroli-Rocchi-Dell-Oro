package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class MoveMotherNatureMessage extends Message {

    private final int steps;

    public MoveMotherNatureMessage(String nickname, int steps) {
        super(nickname, MessageType.ACTION_MOVE_MOTHER_NATURE);
        this.steps = steps;
    }

    public int getSteps() {
        return this.steps;
    }
}
