package it.polimi.ingsw.model;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

class PlayerTest extends TestCase {
    Player player = new Player("nickname", Wizzard.BLUE, Tower.BLACK);
    ExpertPlayer expertPlayer = new ExpertPlayer("expert", Wizzard.BLUE, Tower.WHITE);

    @Test
    void PlayerTest() {
        assertTrue(player.getNickname().equals("nickname"));
        assertTrue(player.getWizzard().equals(Wizzard.BLUE));
        player.togglePlayerTurn();
        assertTrue(player.isPlayerTurn());
        player.setPlayerTurn(false);
        assertTrue(!player.isPlayerTurn());
        player.getBoard();
    }

    @Test
    void AssistantCards() {
        for (int i = 0; i < 10; i++) {
            System.out.println(player.getAssistant(i).value() + " " + player.getAssistant(i).motherNatureMovement());
        }
        AssistantCard a = player.getAssistant(9);
        player.playAssistant(a);
        assertEquals(a, player.getDiscardPileHead());
        assertFalse(player.getAssistantDeck().contains(a));
        for (int i = 0; player.isDeckEmpty(); i++) {
            player.playAssistant(player.getAssistant(i));
        }
    }

    @Test
    void ExpertPlayer() {
        assertEquals(1, expertPlayer.getCoins());
        expertPlayer.addCoin();
        assertEquals(2, expertPlayer.getCoins());
    }
}
