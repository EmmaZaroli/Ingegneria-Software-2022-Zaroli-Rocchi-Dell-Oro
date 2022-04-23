package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlayerTest extends TestCase {
    Player player = new Player("nickname", Wizzard.BLUE, Tower.BLACK, 2);
    ExpertPlayer expertPlayer = new ExpertPlayer("expert", Wizzard.BLUE, Tower.WHITE, 3);

    @Test
    void createStandardPlayer() {
        Assertions.assertEquals("nickname", player.getNickname());
        Assertions.assertEquals(Wizzard.BLUE, player.getWizzard());
        player.togglePlayerTurn();
        Assertions.assertTrue(player.isPlayerTurn());
        player.setPlayerTurn(false);
        Assertions.assertFalse(player.isPlayerTurn());
        player.getBoard();
    }

    @Test
    void createAssistantCards() {
        for (int i = 0; i < 10; i++) {
            System.out.println(player.getAssistant(i).value() + " " + player.getAssistant(i).motherNatureMovement());
        }
        AssistantCard a = player.getAssistant(9);
        player.playAssistant(a);
        Assertions.assertEquals(a, player.getDiscardPileHead());
        Assertions.assertFalse(player.getAssistantDeck().contains(a));
        for (int i = 0; player.isDeckEmpty(); i++) {
            player.playAssistant(player.getAssistant(i));
        }
    }

    @Test
    void createExpertPlayer() {
        Assertions.assertEquals(1, expertPlayer.getCoins());
        expertPlayer.addCoin();
        Assertions.assertEquals(2, expertPlayer.getCoins());
    }
}
