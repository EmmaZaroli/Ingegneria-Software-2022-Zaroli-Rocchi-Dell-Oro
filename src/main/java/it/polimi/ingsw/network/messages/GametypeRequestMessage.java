package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message used by the view containing the number of players and the gameMode chose
 */
public class GametypeRequestMessage extends Message {
    private final GameMode gameMode;
    private final PlayersNumber playersNumber;

    public GametypeRequestMessage(String nickname, GameMode gameMode, PlayersNumber playersNumber) {
        super(nickname, MessageType.GAME_TYPE_REQUEST);
        this.gameMode = gameMode;
        this.playersNumber = playersNumber;
    }

    /**
     *
     * @return the Game Mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     *
     * @return the number of players
     */
    public PlayersNumber getPlayersNumber() {
        return playersNumber;
    }
}
