package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

import java.util.List;

public class GameOverMessage extends Message {

    private final List<String> winners;

    public GameOverMessage(MessageType messageType, List<String> winners) {
        super("server", messageType);
        this.winners = winners;
    }

    public List<String> getWinners() {
        return winners;
    }
}
