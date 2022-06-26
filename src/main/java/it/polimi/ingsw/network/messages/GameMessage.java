package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.GameDto;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message which contains the game
 */
public class GameMessage extends Message {

    private final GameDto game;

    public GameMessage(String nickname, MessageType messageType, Game game) {
        super(nickname, messageType);
        this.game = new GameDto(game, nickname);
    }

    /**
     *
     * @return the Game
     */
    public GameDto getGame() {
        return game;
    }


}
