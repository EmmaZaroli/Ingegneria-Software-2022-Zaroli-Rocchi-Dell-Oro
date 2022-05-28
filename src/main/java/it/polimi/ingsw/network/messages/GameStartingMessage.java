package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.GameDto;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

import java.util.ArrayList;
import java.util.List;

public class GameStartingMessage extends Message {

    private GameDto game;
    private List<AssistantCard> deckFirstPlayer = new ArrayList<>();

    public GameStartingMessage(String nickname, MessageType messageType, Game game) {
        super(nickname, messageType);
        this.game = new GameDto(game, nickname);
        this.deckFirstPlayer = game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck();

    }

    public GameDto getGame() {
        return game;
    }

    public List<AssistantCard> getDeckFirstPlayer() {
        return deckFirstPlayer;
    }

}
