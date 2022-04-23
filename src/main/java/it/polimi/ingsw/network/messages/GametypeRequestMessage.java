package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;

public class GametypeRequestMessage extends Message {
    private final GameMode gameMode;
    private final PlayersNumber playersNumber;

    public GametypeRequestMessage(String nickname, MessageType messageType, GameMode gameMode, PlayersNumber playersNumber) {
        super(nickname, messageType);
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
