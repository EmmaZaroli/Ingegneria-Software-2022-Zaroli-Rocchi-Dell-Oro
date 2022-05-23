package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class GametypeRequestMessage extends Message {
    private final GameMode gameMode;
    private final PlayersNumber playersNumber;

    public GametypeRequestMessage(String nickname, GameMode gameMode, PlayersNumber playersNumber) {
        super(nickname, MessageType.GAME_TYPE_REQUEST);
        this.gameMode = gameMode;
        this.playersNumber = playersNumber;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public PlayersNumber getPlayersNumber() {
        return playersNumber;
    }
}
