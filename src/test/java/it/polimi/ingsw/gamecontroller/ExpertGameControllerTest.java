package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.AssistantPlayedMessage;
import it.polimi.ingsw.network.messages.CharacterCardMessage;
import it.polimi.ingsw.network.messages.MoveMotherNatureMessage;
import it.polimi.ingsw.network.messages.MoveStudentMessage;
import it.polimi.ingsw.view.VirtualView;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ExpertGameControllerTest extends TestCase {

    ExpertPlayer player1 = new ExpertPlayer("player1", Wizzard.BLUE, Tower.BLACK, 2);
    ExpertPlayer player2 = new ExpertPlayer("player2", Wizzard.GREEN, Tower.WHITE, 2);
    ExpertPlayer[] players = {player1, player2};
    ExpertGame game = new ExpertGame(players, new ExpertTable(PlayersNumber.TWO), new ExpertGameParameters(PlayersNumber.TWO, GameMode.NORMAL_MODE));
    ExpertTableController tableController = new ExpertTableController(game.getTable());
    VirtualView[] virtualViews = new VirtualView[2];
    //TODO initialize virtualViews
    ExpertGameController gameController = new ExpertGameController(game, tableController, virtualViews);

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
    void actionActivateEffectCharacterOne() {
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_ONE);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_ONE);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_TWO);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_TWO);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_FOUR);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_FOUR);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_ONE, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_TWO, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_FOUR, game.getCharacterCards()[2].getCharacter());

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[1], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertFalse(game.getParameters().isTakeProfessorEvenIfSameStudents());

        //correct message
        IslandCard island = game.getTable().getIslands().get(0);
        int red = island.getStudentsNumber(PawnColor.RED);
        parameters[0] = PawnColor.RED;
        parameters[1] = island.getUuid();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(red + 1, island.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());

        //wrong message, player has already played character card
        int blue = island.getStudentsNumber(PawnColor.BLUE);
        parameters[0] = PawnColor.BLUE;
        parameters[1] = island.getUuid();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(blue, island.getStudentsNumber(PawnColor.BLUE));
    }

    @Test
    void actionActivateEffectCharacterTwo() {
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_ONE);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_ONE);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_TWO);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_TWO);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_FOUR);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_FOUR);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_ONE, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_TWO, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_FOUR, game.getCharacterCards()[2].getCharacter());

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[1], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertFalse(game.getParameters().isTakeProfessorEvenIfSameStudents());

        //correct message
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[1], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertTrue(game.getParameters().isTakeProfessorEvenIfSameStudents());

        //wrong message, player has already played character card
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[2], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertTrue(game.getParameters().isTakeProfessorEvenIfSameStudents());
        Assertions.assertEquals(0, game.getParameters().getMotherNatureExtraMovements());

        gameController.reverseEffect();
        Assertions.assertFalse(game.getParameters().isTakeProfessorEvenIfSameStudents());
    }

    @Test
    void actionActivateEffectCharacterFour() {
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_ONE);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_ONE);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_TWO);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_TWO);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_FOUR);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_FOUR);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_ONE, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_TWO, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_FOUR, game.getCharacterCards()[2].getCharacter());

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[1], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertFalse(game.getParameters().isTakeProfessorEvenIfSameStudents());

        //correct message
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[2], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(2, game.getParameters().getMotherNatureExtraMovements());

        //wrong message, player has already played character card
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[1], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertFalse(game.getParameters().isTakeProfessorEvenIfSameStudents());
        Assertions.assertEquals(2, game.getParameters().getMotherNatureExtraMovements());

        gameController.reverseEffect();
        Assertions.assertEquals(0, game.getParameters().getMotherNatureExtraMovements());
    }

    @Test
    void actionActivateEffectCharacterSix() {
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_SIX);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_SIX);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_SEVEN);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_SEVEN);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_EIGHT);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_EIGHT);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_SIX, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_SEVEN, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_EIGHT, game.getCharacterCards()[2].getCharacter());

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertTrue(game.getParameters().isTowersCountInInfluence());

        //correct message
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertFalse(game.getParameters().isTowersCountInInfluence());

        //wrong message, player has already played character card
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(3, game.getPlayers()[0].getCoins());
        Assertions.assertFalse(game.getParameters().isTowersCountInInfluence());

        gameController.reverseEffect();
        Assertions.assertTrue(game.getParameters().isTowersCountInInfluence());
    }

    @Test
    void actionActivateEffectCharacterSeven() {
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_SIX);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_SIX);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_SEVEN);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_SEVEN);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_EIGHT);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_EIGHT);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_SIX, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_SEVEN, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_EIGHT, game.getCharacterCards()[2].getCharacter());

        gameController.activateSetupEffect();

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertTrue(game.getParameters().isTowersCountInInfluence());

        //correct message
        List<PawnColor> list1 = new LinkedList<>();
        PawnColor colorCard1 = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudents().get(0);
        PawnColor colorCard2 = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudents().get(1);
        list1.add(colorCard1);
        list1.add(colorCard2);
        parameters[0] = list1;
        int colorNumberCard1 = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudentsNumber(colorCard1);
        int colorNumberCard2 = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudentsNumber(colorCard2);
        List<PawnColor> list2 = new LinkedList<>();
        PawnColor colorEntrance1 = game.getPlayers()[0].getBoard().getEntrance().get(0);
        PawnColor colorEntrance2 = game.getPlayers()[0].getBoard().getEntrance().get(2);
        list2.add(colorEntrance1);
        list2.add(colorEntrance2);
        parameters[1] = list2;
        int colorNumberEntrance1 = game.getPlayers()[0].getBoard().getStudentsInEntrance(colorEntrance1);
        int colorNumberEntrance2 = game.getPlayers()[0].getBoard().getStudentsInEntrance(colorEntrance2);
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[1], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        int newColorNumberCard1 = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudentsNumber(colorCard1);
        int newColorNumberCard2 = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudentsNumber(colorCard2);
        int newColorNumberEntrance1 = game.getPlayers()[0].getBoard().getStudentsInEntrance(colorEntrance1);
        int newColorNumberEntrance2 = game.getPlayers()[0].getBoard().getStudentsInEntrance(colorEntrance2);
        if(colorCard1 != colorCard2){
            if(colorCard1 != colorEntrance1 && colorCard1 != colorEntrance2){
                Assertions.assertEquals(colorNumberCard1 - 1, newColorNumberCard1);
            }
            if(colorCard1 == colorEntrance1 && colorCard1 == colorEntrance2){
                Assertions.assertEquals(colorNumberCard1 + 1, newColorNumberCard1);
            }
            if(colorCard1 == colorEntrance1 ^ colorCard1 == colorEntrance2){
                Assertions.assertEquals(colorNumberCard1, newColorNumberCard1);
            }
            if(colorCard2 != colorEntrance1 && colorCard2 != colorEntrance2){
                Assertions.assertEquals(colorNumberCard2 - 1, newColorNumberCard2);
            }
            if(colorCard2 == colorEntrance1 && colorCard2 == colorEntrance2){
                Assertions.assertEquals(colorNumberCard2 + 1, newColorNumberCard2);
            }
            if(colorCard2 == colorEntrance1 ^ colorCard2 == colorEntrance2){
                Assertions.assertEquals(colorNumberCard2, newColorNumberCard2);
            }
        }
        else{
            if(colorCard1 != colorEntrance1 && colorCard1 != colorEntrance2){
                Assertions.assertEquals(colorNumberCard1 - 2, newColorNumberCard1);
            }
            if(colorCard1 == colorEntrance1 && colorCard1 == colorEntrance2){
                Assertions.assertEquals(colorNumberCard1, newColorNumberCard1);
            }
            if(colorCard1 == colorEntrance1 ^ colorCard1 == colorEntrance2){
                Assertions.assertEquals(colorNumberCard1 - 1, newColorNumberCard1);
            }
        }

        //wrong message, player has already played character card
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(3, game.getPlayers()[0].getCoins());
        Assertions.assertTrue(game.getParameters().isTowersCountInInfluence());

        gameController.reverseEffect();
    }

    @Test
    void actionActivateEffectCharacterEight() {
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_SIX);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_SIX);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_SEVEN);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_SEVEN);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_EIGHT);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_EIGHT);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_SIX, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_SEVEN, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_EIGHT, game.getCharacterCards()[2].getCharacter());

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[2], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(0, game.getParameters().getExtraInfluence());

        //correct message
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[2], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(2, game.getParameters().getExtraInfluence());

        //wrong message, player has already played character card
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(3, game.getPlayers()[0].getCoins());
        Assertions.assertTrue(game.getParameters().isTowersCountInInfluence());

        gameController.reverseEffect();
        Assertions.assertEquals(0, game.getParameters().getExtraInfluence());
    }

    @Test
    void actionActivateEffectCharacterNine() {
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_NINE);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_NINE);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_ELEVEN);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_ELEVEN);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_EIGHT);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_EIGHT);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_NINE, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_ELEVEN, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_EIGHT, game.getCharacterCards()[2].getCharacter());

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        parameters[0] = PawnColor.YELLOW;
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(PawnColor.NONE, game.getParameters().getColorWithNoInfluence());

        //correct message
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        parameters[0] = PawnColor.YELLOW;
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(PawnColor.YELLOW, game.getParameters().getColorWithNoInfluence());

        //wrong message, player has already played character card
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[0], parameters);
        gameController.update(message);
        Assertions.assertEquals(3, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(PawnColor.YELLOW, game.getParameters().getColorWithNoInfluence());

        gameController.reverseEffect();
        Assertions.assertEquals(PawnColor.NONE, game.getParameters().getColorWithNoInfluence());
    }

    @Test
    void actionActivateEffectCharacterEleven() {
        //TODO
        CharacterCard[] characterCards = new CharacterCard[3];
        Effect[] effects = new Effect[3];

        characterCards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_NINE);
        effects[0] = EffectFactory.getEffect(Character.CHARACTER_NINE);
        characterCards[1] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_ELEVEN);
        effects[1] = EffectFactory.getEffect(Character.CHARACTER_ELEVEN);
        characterCards[2] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_EIGHT);
        effects[2] = EffectFactory.getEffect(Character.CHARACTER_EIGHT);
        game.setCharacterCards(characterCards);
        gameController.setEffects(effects);

        Assertions.assertEquals(Character.CHARACTER_NINE, game.getCharacterCards()[0].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_ELEVEN, game.getCharacterCards()[1].getCharacter());
        Assertions.assertEquals(Character.CHARACTER_EIGHT, game.getCharacterCards()[2].getCharacter());

        gameController.activateSetupEffect();

        //wrong message, player has not enough coins
        Object[] parameters = new Object[2];
        CharacterCardMessage message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[2], parameters);
        gameController.update(message);
        Assertions.assertEquals(1, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(0, game.getParameters().getExtraInfluence());

        //correct message
        game.getPlayers()[0].addCoin();
        PawnColor color = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudents().get(0);
        parameters[0] = color;
        int colorNumberCard = ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudentsNumber(color);
        int colorNumberDiningroom = game.getPlayers()[0].getBoard().getStudentsInDiningRoom(color);
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[1], parameters);
        gameController.update(message);
        Assertions.assertEquals(0, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(colorNumberCard - 1, ((CharacterCardWithSetUpAction)game.getCharacterCards()[1]).getStudentsNumber(color));
        Assertions.assertEquals(colorNumberDiningroom + 1, game.getPlayers()[0].getBoard().getStudentsInDiningRoom(color));

        //wrong message, player has already played character card
        game.getPlayers()[0].addCoin();
        game.getPlayers()[0].addCoin();
        message = new CharacterCardMessage("player1", MessageType.CHARACTER_CARD, characterCards[2], parameters);
        gameController.update(message);
        Assertions.assertEquals(2, game.getPlayers()[0].getCoins());
        Assertions.assertEquals(0, game.getParameters().getExtraInfluence());

        gameController.reverseEffect();
    }


}

