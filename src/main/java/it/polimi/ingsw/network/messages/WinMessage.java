package it.polimi.ingsw.network.messages;

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
