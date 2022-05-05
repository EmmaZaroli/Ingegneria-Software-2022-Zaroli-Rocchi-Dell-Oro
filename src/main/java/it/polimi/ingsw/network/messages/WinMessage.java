package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class WinMessage extends Message {

    private final boolean winner;

    public WinMessage(MessageType messageType, boolean win) {
        super("server", messageType);
        this.winner = win;
    }

    public boolean isWinner() {
        return this.winner;
    }
}
