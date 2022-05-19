package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.AssistantPlayedMessage;
import it.polimi.ingsw.network.messages.CloudMessage;
import it.polimi.ingsw.network.messages.MoveMotherNatureMessage;
import it.polimi.ingsw.network.messages.MoveStudentMessage;
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
    Game game = new Game(players, new Table(PlayersNumber.TWO), new GameParameters(PlayersNumber.TWO, GameMode.NORMAL_MODE));
    TableController tableController = new TableController(game.getTable(), game.getParameters());
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
        AssistantPlayedMessage message2 = new AssistantPlayedMessage("player2", MessageType.ASSISTANT_CARD, cardPlayed2);
        gameController.update(message2);
        Assertions.assertEquals(9, player2.getAssistantDeck().size());
        Assertions.assertEquals(GamePhase.ACTION_MOVE_STUDENTS, game.getGamePhase());

        Assertions.assertEquals(7, game.getPlayers()[0].getBoard().getStudentsInEntrance());
        Assertions.assertEquals(7, game.getPlayers()[0].getBoard().getStudentsInEntrance());
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
        int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
        MoveMotherNatureMessage WrongMessage = new MoveMotherNatureMessage("player1", 3);
        gameController.update(WrongMessage);
        Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

        //Correct Message
        IslandCard island = game.getTable().getIslands().get(0);
        MoveStudentMessage message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
        message1.setIslandCard(island);
        Assertions.assertEquals(7, game.getPlayers()[0].getBoard().getStudentsInEntrance());
        gameController.update(message1);
        Assertions.assertEquals(6, game.getPlayers()[0].getBoard().getStudentsInEntrance());
        PawnColor student = pawnColorInEntrance();
        message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        gameController.update(message1);
        Assertions.assertEquals(5, game.getPlayers()[0].getBoard().getStudentsInEntrance());
        Assertions.assertTrue(game.getCurrentPlayerBoard().isThereProfessor(student));
        PawnColor student2 = pawnColorInEntrance();
        message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student2);
        gameController.update(message1);
        Assertions.assertEquals(4, game.getPlayers()[0].getBoard().getStudentsInEntrance());
        Assertions.assertEquals(0, game.getCurrentPlayer());
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
        Assertions.assertFalse(game.getPlayers()[1].getBoard().isThereProfessor(student));
    }

    @Test
    void actionMoveMotherNature(){
        actionMoveStudent();

        //Wrong messages (wrong action)
        int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
        IslandCard island = game.getTable().getIslands().get(0);
        MoveStudentMessage wrongMessage1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
        wrongMessage1.setIslandCard(island);
        gameController.update(wrongMessage1);
        Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

        //Wrong messages (wrong player)
        originalMnPosition = tableController.getTable().getIslandWithMotherNature();
        MoveMotherNatureMessage wrongMessage2 = new MoveMotherNatureMessage("player2", 3);
        gameController.update(wrongMessage2);
        Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

        //Wrong messages (illegal number of steps)
        originalMnPosition = tableController.getTable().getIslandWithMotherNature();
        wrongMessage2 = new MoveMotherNatureMessage("player1", 20);
        gameController.update(wrongMessage2);
        Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

        //Correct message
        originalMnPosition = tableController.getTable().getIslandWithMotherNature();
        MoveMotherNatureMessage correctMessage = new MoveMotherNatureMessage("player1", 1);
        gameController.update(correctMessage);
        Assertions.assertEquals((originalMnPosition + 1) % 12, tableController.getTable().getIslandWithMotherNature());
    }

    @Test
    void actionChooseCloud(){
        actionMoveMotherNature();

        //Wrong messages (wrong action)
        int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
        IslandCard island = game.getTable().getIslands().get(0);
        MoveStudentMessage wrongMessage1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
        wrongMessage1.setIslandCard(island);
        gameController.update(wrongMessage1);
        Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

        //Wrong messages (wrong player)
        CloudMessage correctMessage = new CloudMessage("player2", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
        Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
        gameController.update(correctMessage);
        Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
        Assertions.assertEquals(4, game.getPlayers()[0].getBoard().countStudentsInEntrance());

        //Correct message
        correctMessage = new CloudMessage("player1", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
        Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
        gameController.update(correctMessage);
        Assertions.assertEquals(0, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
        Assertions.assertEquals(7, game.getPlayers()[0].getBoard().countStudentsInEntrance());
    }

    @Test
    void actionPlayer2Turn(){
        actionChooseCloud();

        //player2 move students
        IslandCard island = game.getTable().getIslands().get(0);
        MoveStudentMessage message1 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
        message1.setIslandCard(island);
        Assertions.assertEquals(7, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
        gameController.update(message1);
        Assertions.assertEquals(6, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
        PawnColor student = pawnColorInEntrance();
        message1 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        gameController.update(message1);
        Assertions.assertEquals(5, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
        PawnColor student2 = pawnColorInEntrance();
        message1 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student2);
        gameController.update(message1);
        Assertions.assertEquals(4, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
        Assertions.assertEquals(1, game.getCurrentPlayer());

        //player2 move mother nature
        int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
        MoveMotherNatureMessage message2 = new MoveMotherNatureMessage("player2", 1);
        gameController.update(message2);
        Assertions.assertEquals((originalMnPosition + 1) % 12, tableController.getTable().getIslandWithMotherNature());

        //player2 choose cloud
        //wrong message (cloud empty)
        CloudMessage message3 = new CloudMessage("player2", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
        Assertions.assertEquals(0, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
        gameController.update(message3);
        Assertions.assertEquals(0, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
        Assertions.assertEquals(4, game.getCurrentPlayerSchoolBoard().countStudentsInEntrance());
        //correct message
        message3 = new CloudMessage("player2", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(1));
        Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(1).getStudentsNumber());
        gameController.update(message3);
        Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(1).getStudentsNumber());
        Assertions.assertEquals(7, game.getCurrentPlayerSchoolBoard().countStudentsInEntrance());
    }
}
