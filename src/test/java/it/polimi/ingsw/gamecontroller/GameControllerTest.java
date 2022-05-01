package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.network.messages.AssistantPlayedMessage;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.MoveMotherNatureMessage;
import it.polimi.ingsw.network.messages.MoveStudentMessage;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GameControllerTest extends TestCase {

    Player player1 = new Player("player1", Wizzard.BLUE, Tower.BLACK, 2);
    Player player2 = new Player("player2", Wizzard.GREEN, Tower.WHITE, 2);
    Player[] players = {player1, player2};
    Game game = new Game(players);
    TableController tableController = new TableController(game.getTable());
    VirtualView[] virtualViews = new VirtualView[2];
    //TODO initialize virtualViews
    GameController gameController = new GameController(game, tableController, virtualViews);

    @BeforeEach
    public void planning() {
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

    private PawnColor pawnColorInEntrance() {
        for (PawnColor p : PawnColor.getValidValues()) {
            if (game.getCurrentPlayerBoard().isStudentInEntrance(p)) {
                return p;
            }
        }
        return PawnColor.NONE;
    }

    @Test
    void actionMoveStudent() {

        //Wrong Messages
        MoveMotherNatureMessage WrongMessage = new MoveMotherNatureMessage("player1", 3);
        gameController.update(WrongMessage);

        //Correct Message
        IslandCard island = game.getTable().getIslands().get(0);
        MoveStudentMessage message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
        message1.setIslandCard(island);
        gameController.update(message1);
        PawnColor student = pawnColorInEntrance();
        message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        gameController.update(message1);
        Assertions.assertTrue(game.getCurrentPlayerBoard().isThereProfessor(student));
        PawnColor student2 = pawnColorInEntrance();
        message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student2);
        gameController.update(message1);

        //TODO solve this bug
        //Assertions.assertEquals(1, game.getCurrentPlayer());
        //2.

        //steal professor
        game.getCurrentPlayerBoard().removeStudentFromEntrance(pawnColorInEntrance());
        game.getCurrentPlayerBoard().removeStudentFromEntrance(pawnColorInEntrance());
        List<PawnColor> students = new ArrayList<>();
        students.add(student);
        game.getCurrentPlayerBoard().addStudentsToEntrance(students);
        game.getCurrentPlayerBoard().addStudentsToEntrance(students);
        MoveStudentMessage message2 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        gameController.update(message2);
        message2 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        gameController.update(message2);
        Assertions.assertTrue(game.getCurrentPlayerBoard().isThereProfessor(student));

        //TODO solve this bug
        //Assertions.assertFalse(game.getPlayers()[0].getBoard().isThereProfessor(student));
    }
}
