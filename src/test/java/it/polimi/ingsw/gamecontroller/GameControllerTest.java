package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizard;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.AssistantPlayedMessage;
import it.polimi.ingsw.network.messages.CloudMessage;
import it.polimi.ingsw.network.messages.MoveMotherNatureMessage;
import it.polimi.ingsw.network.messages.MoveStudentMessage;
import it.polimi.ingsw.servercontroller.User;
import it.polimi.ingsw.view.VirtualView;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

class GameControllerTest extends TestCase {

    Player player1 = new Player("player1", Wizard.BLUE, Tower.BLACK, 2);
    Player player2 = new Player("player2", Wizard.GREEN, Tower.WHITE, 2);
    Player[] players = {player1, player2};
    Game game = new Game(players, new Table(PlayersNumber.TWO), new GameParameters(PlayersNumber.TWO, GameMode.NORMAL_MODE));
    TableController tableController = new TableController(game.getTable(), game.getParameters());
    VirtualView[] virtualViews = {new VirtualView(new User("player1"), game), new VirtualView(new User("player2"), game)};

    GameController gameController = new GameController(game, tableController, virtualViews);

    @BeforeEach
    void init(){
        gameController.init();
    }

    @Test
    void planningWithOnlyEqualsAssistants(){
        Assertions.assertEquals(7, player1.getBoard().countStudentsInEntrance());
        Assertions.assertEquals(7, player2.getBoard().countStudentsInEntrance());

        //1.

        AssistantCard cardPlayed1 = player1.getAssistant(0);
        AssistantPlayedMessage message1 = new AssistantPlayedMessage("player1", MessageType.ACTION_PLAY_ASSISTANT, cardPlayed1);
        gameController.onMessageReceived(message1);
        Assertions.assertEquals(9, player1.getAssistantDeck().size());
        Assertions.assertEquals(GamePhase.PLANNING, game.getGamePhase());
        Assertions.assertEquals(1, game.getCurrentPlayer());

        //2.

        //remove all character card except first two
        player2.removeAssistant(player2.getAssistant(9));
        player2.removeAssistant(player2.getAssistant(8));
        player2.removeAssistant(player2.getAssistant(7));
        player2.removeAssistant(player2.getAssistant(6));
        player2.removeAssistant(player2.getAssistant(5));
        player2.removeAssistant(player2.getAssistant(4));
        player2.removeAssistant(player2.getAssistant(3));
        player2.removeAssistant(player2.getAssistant(2));
        Assertions.assertEquals(2, player2.getAssistantDeck().size());
        AssistantPlayedMessage sameCardMessage = new AssistantPlayedMessage("player2", MessageType.ACTION_PLAY_ASSISTANT, cardPlayed1);
        gameController.onMessageReceived(sameCardMessage);
        Assertions.assertEquals(2, player2.getAssistantDeck().size());
        Assertions.assertEquals(1, game.getCurrentPlayer());
        Assertions.assertEquals(GamePhase.PLANNING, game.getGamePhase());

        //remove even the second card, player2 remain only with first card
        player2.removeAssistant(player2.getAssistant(1));
        Assertions.assertEquals(1, player2.getAssistantDeck().size());
        gameController.onMessageReceived(sameCardMessage);
        Assertions.assertEquals(0, player2.getAssistantDeck().size());
        Assertions.assertEquals(cardPlayed1, player2.getDiscardPileHead());
        Assertions.assertEquals(0, game.getCurrentPlayer());
        Assertions.assertEquals(GamePhase.ACTION_MOVE_STUDENTS, game.getGamePhase());

        //player1 action
        MoveStudentMessage messageMoveStudent = new MoveStudentMessage(player1.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player1.getBoard().getEntrance().get(0));
        gameController.onMessageReceived(messageMoveStudent);
        messageMoveStudent = new MoveStudentMessage(player1.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player1.getBoard().getEntrance().get(0));
        gameController.onMessageReceived(messageMoveStudent);
        messageMoveStudent = new MoveStudentMessage(player1.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player1.getBoard().getEntrance().get(0));
        gameController.onMessageReceived(messageMoveStudent);
        MoveMotherNatureMessage messageMoveMN = new MoveMotherNatureMessage(player1.getNickname(), 1);
        gameController.onMessageReceived(messageMoveMN);
        CloudMessage messageChooseCloud = new CloudMessage(player1.getNickname(), MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
        gameController.onMessageReceived(messageChooseCloud);
        Assertions.assertEquals(1, game.getCurrentPlayer());
        Assertions.assertEquals(GamePhase.ACTION_MOVE_STUDENTS, game.getGamePhase());
    }

    @Nested
    class NormalPlanningTestNest{
        @BeforeEach
        public void planning() {
            Assertions.assertEquals(7, player1.getBoard().countStudentsInEntrance());
            Assertions.assertEquals(7, player2.getBoard().countStudentsInEntrance());

            //1.

            AssistantCard cardPlayed1 = player1.getAssistant(0);
            //Wrong Messages (wrong player)
            AssistantPlayedMessage WrongPlayer = new AssistantPlayedMessage("player2", MessageType.ACTION_PLAY_ASSISTANT, cardPlayed1);
            gameController.onMessageReceived(WrongPlayer);
            Assertions.assertEquals(10, player2.getAssistantDeck().size());

            //wrong message (wrong action)
            MoveStudentMessage wrongAction = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, PawnColor.RED);
            gameController.onMessageReceived(wrongAction);

            //Correct Message
            AssistantPlayedMessage message1 = new AssistantPlayedMessage("player1", MessageType.ACTION_PLAY_ASSISTANT, cardPlayed1);
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(9, player1.getAssistantDeck().size());
            Assertions.assertEquals(GamePhase.PLANNING, game.getGamePhase());
            Assertions.assertEquals(1, game.getCurrentPlayer());

            //2.

            AssistantPlayedMessage SameCard = new AssistantPlayedMessage("player2", MessageType.ACTION_PLAY_ASSISTANT, cardPlayed1);
            gameController.onMessageReceived(SameCard);
            Assertions.assertEquals(10, player2.getAssistantDeck().size());
            //Correct Message
            AssistantCard cardPlayed2 = player2.getAssistant(2);
            AssistantPlayedMessage message2 = new AssistantPlayedMessage("player2", MessageType.ACTION_PLAY_ASSISTANT, cardPlayed2);
            gameController.onMessageReceived(message2);
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
            //Wrong Messages (wrong action)
            int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
            MoveMotherNatureMessage WrongMessage = new MoveMotherNatureMessage("player1", 3);
            gameController.onMessageReceived(WrongMessage);
            Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

            //wrong message (wrong island uuid)
            MoveStudentMessage message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
            message1.setIslandCard(new IslandCard(UUID.randomUUID(), 1));
            gameController.onMessageReceived(message1);

            //Correct Message
            IslandCard island = game.getTable().getIslands().get(0);
            message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
            message1.setIslandCard(island);
            Assertions.assertEquals(7, game.getPlayers()[0].getBoard().getStudentsInEntrance());
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(6, game.getPlayers()[0].getBoard().getStudentsInEntrance());
            PawnColor student = pawnColorInEntrance();
            message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(5, game.getPlayers()[0].getBoard().getStudentsInEntrance());
            Assertions.assertTrue(game.getCurrentPlayerBoard().isThereProfessor(student));
            PawnColor student2 = pawnColorInEntrance();
            message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student2);
            gameController.onMessageReceived(message1);
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
            gameController.onMessageReceived(message2);
            message2 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
            gameController.onMessageReceived(message2);
            Assertions.assertTrue(game.getCurrentPlayerBoard().isThereProfessor(student));
            Assertions.assertFalse(game.getPlayers()[1].getBoard().isThereProfessor(student));
        }

        @Test
        void actionMoveMotherNature() {
            actionMoveStudent();

            //Wrong messages (wrong action)
            int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
            IslandCard island = game.getTable().getIslands().get(0);
            MoveStudentMessage wrongMessage1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
            wrongMessage1.setIslandCard(island);
            gameController.onMessageReceived(wrongMessage1);
            Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

            //Wrong messages (wrong player)
            originalMnPosition = tableController.getTable().getIslandWithMotherNature();
            MoveMotherNatureMessage wrongMessage2 = new MoveMotherNatureMessage("player2", 3);
            gameController.onMessageReceived(wrongMessage2);
            Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

            //Wrong messages (illegal number of steps)
            originalMnPosition = tableController.getTable().getIslandWithMotherNature();
            wrongMessage2 = new MoveMotherNatureMessage("player1", 20);
            gameController.onMessageReceived(wrongMessage2);
            Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

            //Correct message
            originalMnPosition = tableController.getTable().getIslandWithMotherNature();
            MoveMotherNatureMessage correctMessage = new MoveMotherNatureMessage("player1", 1);
            gameController.onMessageReceived(correctMessage);
            Assertions.assertEquals((originalMnPosition + 1) % 12, tableController.getTable().getIslandWithMotherNature());
        }

        @Test
        void actionChooseCloud() {
            actionMoveMotherNature();

            //Wrong messages (wrong action)
            int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
            IslandCard island = game.getTable().getIslands().get(0);
            MoveStudentMessage wrongMessage1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
            wrongMessage1.setIslandCard(island);
            gameController.onMessageReceived(wrongMessage1);
            Assertions.assertEquals(originalMnPosition, tableController.getTable().getIslandWithMotherNature());

            //Wrong messages (wrong player)
            CloudMessage correctMessage = new CloudMessage("player2", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
            Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
            gameController.onMessageReceived(correctMessage);
            Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
            Assertions.assertEquals(4, game.getPlayers()[0].getBoard().countStudentsInEntrance());

            //Correct message
            correctMessage = new CloudMessage("player1", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
            Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
            gameController.onMessageReceived(correctMessage);
            Assertions.assertEquals(0, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
            Assertions.assertEquals(7, game.getPlayers()[0].getBoard().countStudentsInEntrance());
        }

        @Test
        void actionPlayer2Turn() {
            actionChooseCloud();

            //player2 move students
            IslandCard island = game.getTable().getIslands().get(0);
            MoveStudentMessage message1 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, pawnColorInEntrance());
            message1.setIslandCard(island);
            Assertions.assertEquals(7, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(6, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
            PawnColor student = pawnColorInEntrance();
            message1 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(5, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
            PawnColor student2 = pawnColorInEntrance();
            message1 = new MoveStudentMessage("player2", MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student2);
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(4, game.getCurrentPlayerSchoolBoard().getStudentsInEntrance());
            Assertions.assertEquals(1, game.getCurrentPlayer());

            //player2 move mother nature
            int originalMnPosition = tableController.getTable().getIslandWithMotherNature();
            int originalIslandsNumber = tableController.getTable().getIslands().size();
            MoveMotherNatureMessage message2 = new MoveMotherNatureMessage("player2", 1);
            gameController.onMessageReceived(message2);
            if(tableController.getTable().getIslands().size() == originalIslandsNumber)
                Assertions.assertEquals(Math.floorMod(originalMnPosition + 1, 12), tableController.getTable().getIslandWithMotherNature());
            else
                Assertions.assertEquals(Math.floorMod(originalMnPosition, 12), tableController.getTable().getIslandWithMotherNature());
            //player2 choose cloud
            //wrong message (cloud empty)
            CloudMessage message3 = new CloudMessage("player2", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
            Assertions.assertEquals(0, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
            gameController.onMessageReceived(message3);
            Assertions.assertEquals(0, tableController.getTable().getCloudTiles().get(0).getStudentsNumber());
            Assertions.assertEquals(4, game.getCurrentPlayerSchoolBoard().countStudentsInEntrance());
            //correct message
            message3 = new CloudMessage("player2", MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(1));
            Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(1).getStudentsNumber());
            gameController.onMessageReceived(message3);
            Assertions.assertEquals(3, tableController.getTable().getCloudTiles().get(1).getStudentsNumber());
            Assertions.assertEquals(7, game.getCurrentPlayerSchoolBoard().countStudentsInEntrance());
        }

        @Test
        void player1BuildLastTower(){
            //move 3 students on island 0
            IslandCard island = game.getTable().getIslands().get(0);
            MoveStudentMessage message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, player1.getBoard().getEntrance().get(0));
            message1.setIslandCard(island);
            gameController.onMessageReceived(message1);
            gameController.onMessageReceived(message1);
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(GamePhase.ACTION_MOVE_MOTHER_NATURE, game.getGamePhase());

            Table table = tableController.getTable();

            //set first 7 islands of player1's tower color
            table.setTower(table.getIsland(0), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(1), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(2), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(3), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(4), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(5), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(6), player1.getBoard().getTowerColor());

            //unify first 9 islands
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);

            //move 5 red students on player1 diningroom and add red professor
            List<PawnColor> list = new LinkedList<>();
            for(int i = 0; i < 5; i++)
                list.add(PawnColor.RED);
            player1.getBoard().addStudentsToEntrance(list);
            for(int i = 0; i < 5; i++)
                player1.getBoard().moveStudentFromEntranceToDiningRoom(PawnColor.RED);
            player1.getBoard().addProfessor(PawnColor.RED);

            //move 20 red students on island 1 to ensure influence
            for(int i = 0; i < 20; i++)
                table.getIsland(1).movePawnOnIsland(PawnColor.RED);

            //place mn on island 0
            if(table.getIslandWithMotherNature() == -1)
                table.getIsland(0).setHasMotherNature(true);
            else
                table.setIslandWithMotherNature(0);

            //remove 7 towers from player1
            for(int i = 0; i < 7; i++)
                player1.getBoard().removeTower();

            Assertions.assertEquals(player1.getBoard().getTowerColor(), table.getIsland(0).getTower());
            Assertions.assertEquals(7, table.getIsland(0).getSize());
            Assertions.assertEquals(6, table.getIslands().size());
            Assertions.assertTrue(table.getIsland(1).getStudentsNumber(PawnColor.RED) >= 20);
            Assertions.assertEquals(5, player1.getBoard().getStudentsInDiningRoom(PawnColor.RED));
            Assertions.assertTrue(player1.getBoard().getProfessors().contains(PawnColor.RED));
            Assertions.assertEquals(0, table.getIslandWithMotherNature());
            Assertions.assertEquals(1, player1.getBoard().getTowersCount());

            gameController.onMessageReceived(new MoveMotherNatureMessage(player1.getNickname(), 1));

            Assertions.assertTrue(game.isGameOver());
            Assertions.assertEquals(1, game.getWinners().size());
            Assertions.assertEquals(player1.getNickname(), game.getWinners().get(0));

            Assertions.assertEquals(8, table.getIsland(0).getSize());
            Assertions.assertEquals(5, table.getIslands().size());
            Assertions.assertEquals(0, table.getIslandWithMotherNature());
        }

        @Test
        void only3islandsRemain(){
            //move 3 students on island 0
            IslandCard island = game.getTable().getIslands().get(0);
            MoveStudentMessage message1 = new MoveStudentMessage("player1", MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, game.getPlayer(0).getBoard().getEntrance().get(0));
            message1.setIslandCard(island);
            gameController.onMessageReceived(message1);
            gameController.onMessageReceived(message1);
            gameController.onMessageReceived(message1);
            Assertions.assertEquals(GamePhase.ACTION_MOVE_MOTHER_NATURE, game.getGamePhase());

            Table table = tableController.getTable();

            //island 0 to 4 unifyed and controlled by player1
            //island 5 not controlled
            //island 6 to 10 unifyed and controlled by player2
            //island 11 not controlled
            //total of 4 islands controlled by (in order) player1, free, player2, free

            table.setTower(table.getIsland(0), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(1), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(2), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(3), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(4), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(5), Tower.NONE);
            table.setTower(table.getIsland(6), player2.getBoard().getTowerColor());
            table.setTower(table.getIsland(7), player2.getBoard().getTowerColor());
            table.setTower(table.getIsland(8), player2.getBoard().getTowerColor());
            table.setTower(table.getIsland(9), player2.getBoard().getTowerColor());
            table.setTower(table.getIsland(10), player2.getBoard().getTowerColor());
            table.setTower(table.getIsland(11), Tower.NONE);

            //unify islands 0-4
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);
            table.unifyIslands(0, 1);
            //unify islands 6-10
            table.unifyIslands(2, 3);
            table.unifyIslands(2, 3);
            table.unifyIslands(2, 3);
            table.unifyIslands(2, 3);

            Assertions.assertEquals(4, table.getIslands().size());
            Assertions.assertEquals(player1.getBoard().getTowerColor(), table.getIsland(0).getTower());
            Assertions.assertEquals(Tower.NONE, table.getIsland(1).getTower());
            Assertions.assertEquals(player2.getBoard().getTowerColor(), table.getIsland(2).getTower());
            Assertions.assertEquals(Tower.NONE, table.getIsland(3).getTower());

            //remove 5 towers from player1
            for(int i = 0; i < 5; i++)
                player1.getBoard().removeTower();

            Assertions.assertEquals(3, player1.getBoard().getTowersCount());

            //remove 5 towers from player2
            for(int i = 0; i < 5; i++)
                player2.getBoard().removeTower();

            Assertions.assertEquals(3, player2.getBoard().getTowersCount());

            //place mn on island 0
            if(table.getIslandWithMotherNature() == -1)
                table.getIsland(0).setHasMotherNature(true);
            else
                table.setIslandWithMotherNature(0);

            Assertions.assertEquals(0, table.getIslandWithMotherNature());

            //move 5 red students on player1 diningroom and add red professor
            List<PawnColor> list = new LinkedList<>();
            for(int i = 0; i < 5; i++)
                list.add(PawnColor.RED);
            player1.getBoard().addStudentsToEntrance(list);
            for(int i = 0; i < 5; i++)
                player1.getBoard().moveStudentFromEntranceToDiningRoom(PawnColor.RED);
            player1.getBoard().addProfessor(PawnColor.RED);

            Assertions.assertTrue(player1.getBoard().getStudentsInDiningRoom(PawnColor.RED) >= 5);
            Assertions.assertTrue(player1.getBoard().getProfessors().contains(PawnColor.RED));

            //move 20 red students on island 1 to ensure influence
            for(int i = 0; i < 20; i++)
                table.getIsland(1).movePawnOnIsland(PawnColor.RED);

            Assertions.assertTrue(table.getIsland(1).getStudentsNumber(PawnColor.RED) >= 20);

            //move mn from island 0 to island 1
            //should unify island 0 with isalnd 1 making onli 3 islands ramain
            gameController.onMessageReceived(new MoveMotherNatureMessage(player1.getNickname(), 1));

            Assertions.assertEquals(0, table.getIslandWithMotherNature());
            Assertions.assertEquals(6, table.getIsland(0).getSize());
            Assertions.assertEquals(3, table.getIslands().size());
            Assertions.assertTrue(game.isGameOver());
            Assertions.assertEquals(1, game.getWinners().size());
            Assertions.assertEquals(player1.getNickname(), game.getWinners().get(0));

        }

        @Test
        void lastStudentDrawnFomBag(){
            Table table = tableController.getTable();

            //place mn on island 0
            if(table.getIslandWithMotherNature() == -1)
                table.getIsland(0).setHasMotherNature(true);
            else
                table.setIslandWithMotherNature(0);

            Assertions.assertEquals(0, table.getIslandWithMotherNature());

            //player1 action
            MoveStudentMessage messageMoveStudent = new MoveStudentMessage(player1.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player1.getBoard().getEntrance().get(0));
            gameController.onMessageReceived(messageMoveStudent);
            messageMoveStudent = new MoveStudentMessage(player1.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player1.getBoard().getEntrance().get(0));
            gameController.onMessageReceived(messageMoveStudent);
            messageMoveStudent = new MoveStudentMessage(player1.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player1.getBoard().getEntrance().get(0));
            gameController.onMessageReceived(messageMoveStudent);
            MoveMotherNatureMessage messageMoveMN = new MoveMotherNatureMessage(player1.getNickname(), 1);
            gameController.onMessageReceived(messageMoveMN);
            CloudMessage messageChooseCloud = new CloudMessage(player1.getNickname(), MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(0));
            gameController.onMessageReceived(messageChooseCloud);
            Assertions.assertEquals(1, game.getCurrentPlayer());
            Assertions.assertEquals(GamePhase.ACTION_MOVE_STUDENTS, game.getGamePhase());

            //player2 move students
            messageMoveStudent = new MoveStudentMessage(player2.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player2.getBoard().getEntrance().get(0));
            gameController.onMessageReceived(messageMoveStudent);
            messageMoveStudent = new MoveStudentMessage(player2.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player2.getBoard().getEntrance().get(0));
            gameController.onMessageReceived(messageMoveStudent);
            messageMoveStudent = new MoveStudentMessage(player2.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, player2.getBoard().getEntrance().get(0));
            gameController.onMessageReceived(messageMoveStudent);

            //player2 move MN
            messageMoveMN = new MoveMotherNatureMessage(player2.getNickname(), 2);
            gameController.onMessageReceived(messageMoveMN);

            Assertions.assertEquals(3, tableController.getTable().getIslandWithMotherNature());

            //make player 1 controlling island 0,1
            table.setTower(table.getIsland(0), player1.getBoard().getTowerColor());
            table.setTower(table.getIsland(1), player1.getBoard().getTowerColor());
            while (player1.getBoard().getTowersCount() > 6)
                player1.getBoard().removeTower();

            Assertions.assertEquals(player1.getBoard().getTowerColor(), table.getIsland(0).getTower());
            Assertions.assertEquals(player1.getBoard().getTowerColor(), table.getIsland(1).getTower());
            Assertions.assertEquals(6, player1.getBoard().getTowersCount() );

            //make player 2 controlling island 2,3
            table.setTower(table.getIsland(2), player2.getBoard().getTowerColor());
            table.setTower(table.getIsland(3), player2.getBoard().getTowerColor());
            while (player2.getBoard().getTowersCount() > 6)
                player2.getBoard().removeTower();

            Assertions.assertEquals(player2.getBoard().getTowerColor(), table.getIsland(2).getTower());
            Assertions.assertEquals(player2.getBoard().getTowerColor(), table.getIsland(3).getTower());
            Assertions.assertEquals(6, player2.getBoard().getTowersCount() );

            for(int i = 4; i < 12; i++)
                Assertions.assertEquals(Tower.NONE, table.getIsland(i).getTower());

            //make player1 controlling only red professor
            for(PawnColor color : PawnColor.getValidValues()){
                if(color == PawnColor.RED)
                    player1.getBoard().addProfessor(color);
                else
                    player1.getBoard().removeProfessor(color);
            }

            for(PawnColor color : PawnColor.getValidValues()){
                if(color == PawnColor.RED)
                    Assertions.assertTrue(player1.getBoard().getProfessors().contains(color));
                else
                    Assertions.assertFalse(player1.getBoard().getProfessors().contains(color));
            }

            //make player2 controlling only blue professor
            for(PawnColor color : PawnColor.getValidValues()){
                if(color == PawnColor.BLUE)
                    player2.getBoard().addProfessor(color);
                else
                    player2.getBoard().removeProfessor(color);
            }

            for(PawnColor color : PawnColor.getValidValues()){
                if(color == PawnColor.BLUE)
                    Assertions.assertTrue(player2.getBoard().getProfessors().contains(color));
                else
                    Assertions.assertFalse(player2.getBoard().getProfessors().contains(color));
            }

            //left 0 students in bag
            tableController.drawStudents(tableController.getBag().getStudentsLeft());

            Assertions.assertEquals(0, table.getBag().getStudentsLeft());

            //player2 choose cloud
            messageChooseCloud = new CloudMessage(player2.getNickname(), MessageType.ACTION_CHOOSE_CLOUD, tableController.getTable().getCloudTiles().get(1));
            gameController.onMessageReceived(messageChooseCloud);

            Assertions.assertTrue(game.isGameOver());
            Assertions.assertEquals(0, table.getBag().getStudentsLeft());
            //player1 and player2 have the same number of towers and professors, so this should be a tie
            Assertions.assertEquals(2, game.getWinners().size());
            Assertions.assertTrue(game.getWinners().contains(player1.getNickname()));
            Assertions.assertTrue(game.getWinners().contains(player2.getNickname()));
        }
    }

    @Nested
    class DisconnectionTestNest{
        @Test
        void allPlayersGoOffline(){
            game.setEnoughPlayersOnline(true);
            Assertions.assertTrue(game.areEnoughPlayersOnline());
            Assertions.assertFalse(virtualViews[0].isOnline());
            Assertions.assertFalse(virtualViews[1].isOnline());
            Assertions.assertTrue(player1.isOnline());
            Assertions.assertTrue(player2.isOnline());
            gameController.onDisconnect(this); //TODO fix this
            Assertions.assertFalse(player1.isOnline());
            Assertions.assertFalse(player2.isOnline());
            Assertions.assertFalse(game.areEnoughPlayersOnline());
            /*
            try {
                wait(10000); //TODO this should be 4-5 seconds more than the disconnection timer duration
                Assertions.assertTrue(game.isGameOver());
                Assertions.assertEquals(0, game.getWinners().size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }
    }


}
