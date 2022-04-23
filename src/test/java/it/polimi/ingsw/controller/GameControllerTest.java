package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TableController;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.network.messages.AssistantPlayedMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameControllerTest extends TestCase {

    Player player1 = new Player("player1", Wizzard.BLUE, Tower.BLACK, 2);
    Player player2 = new Player("player2", Wizzard.GREEN, Tower.WHITE, 2);
    Player[] players = {player1, player2};
    Game game = new Game(players);
    TableController tableController = new TableController(game.getTable());
    GameController gameController = new GameController(game, tableController);

    @Test
    public void Planning() {
        gameController.run();
        Assertions.assertEquals(7, player1.getBoard().countStudentsInEntrance());
        Assertions.assertEquals(7, player2.getBoard().countStudentsInEntrance());

        //1.

        AssistantCard cardPlayed1 = player1.getAssistant(0);
        //Wrong Messages
        AssistantPlayedMessage WrongPlayer = new AssistantPlayedMessage("player2", MessageType.ASSISTANT_CARD, cardPlayed1);
        gameController.update(WrongPlayer);
        Assertions.assertEquals(10, player2.getAssistantDeck().size());
        AssistantPlayedMessage WrongMessage = new AssistantPlayedMessage("player1", MessageType.CLOUD, cardPlayed1);
        gameController.update(WrongMessage);
        //Correct Message
        AssistantPlayedMessage message1 = new AssistantPlayedMessage("player1", MessageType.ASSISTANT_CARD, cardPlayed1);
        gameController.update(message1);
        Assertions.assertEquals(9, player1.getAssistantDeck().size());
        Assertions.assertEquals(GamePhase.PLANNING, game.getGamePhase());
        Assertions.assertEquals(1, game.getCurrentPlayer());

        //2.

        AssistantPlayedMessage SameCard = new AssistantPlayedMessage("player2", MessageType.ASSISTANT_CARD, cardPlayed1);
        gameController.update(SameCard);
        Assertions.assertEquals(10, player2.getAssistantDeck().size());
        //Correct Message
        AssistantCard cardPlayed2 = player2.getAssistant(1);
        AssistantPlayedMessage message2 = new AssistantPlayedMessage("player1", MessageType.ASSISTANT_CARD, cardPlayed2);
        gameController.update(message2);
        Assertions.assertEquals(9, player2.getAssistantDeck().size());
        Assertions.assertEquals(GamePhase.ACTION_MOVE_STUDENTS, game.getGamePhase());

    }

    @Test
    public void Action_Move_Student() {
        
    }

}
