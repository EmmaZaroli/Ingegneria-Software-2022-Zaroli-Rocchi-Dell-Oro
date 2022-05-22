package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.GameDto;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

import java.util.List;

public class GameStartingMessage extends Message {

    private GameDto game;
    private String firstPlayer;
    private List<AssistantCard> deckfirstPlayer;

    public GameStartingMessage(String nickname, MessageType messageType, Game game) {
        super(nickname, messageType);
        this.game = new GameDto(game, nickname);
        firstPlayer = game.getPlayers()[game.getCurrentPlayer()].getNickname();
        deckfirstPlayer = game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck();
    }

    public GameDto getGame() {
        return game;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public List<AssistantCard> getDeckfirstPlayer() {
        return deckfirstPlayer;
    }
}
