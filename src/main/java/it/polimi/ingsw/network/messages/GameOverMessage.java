package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

import java.util.List;

/**
 * Message which contains the list of winners at the end of the game
 */
public class GameOverMessage extends Message {

    private final List<String> winners;

    public GameOverMessage(MessageType messageType, List<String> winners) {
        super("server", messageType);
        this.winners = winners;
    }

    /**
     *
     * @return the list of winners
     */
    public List<String> getWinners() {
        return winners;
    }
}
