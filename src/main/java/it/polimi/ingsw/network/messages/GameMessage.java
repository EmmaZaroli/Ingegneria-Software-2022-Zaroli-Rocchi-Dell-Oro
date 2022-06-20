package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.GameDto;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;


public class GameMessage extends Message {

    private GameDto game;


    public GameMessage(String nickname, MessageType messageType, Game game) {
        super(nickname, messageType);
        this.game = new GameDto(game, nickname);
    }

    public GameDto getGame() {
        return game;
    }


}
