package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.PlayerInfo;

import java.io.PrintStream;
import java.util.List;

/**
 * Class for printing Assistant Cards
 */
public class PrinterAssistantCards {


    /**
     * Prints the assistant card chose by the players
     * @param players the players
     */
    public void print(List<PlayerInfo> players) {
        PrintStream out = System.out;
        out.println();
        centre();
        for (int i = 0; i < players.size(); i++) {
            out.print(" _____ ");
            space();
        }
        out.println();
        centre();
        for (PlayerInfo player : players) {
            if (player.getDiscardPileHead().isPresent()) {
                int value = player.getDiscardPileHead().get().value();
                if (value != 10)
                    out.print("|" + value + "   " + player.getDiscardPileHead().get().motherNatureMovement() + "|");
                else out.print("|" + value + "  " + player.getDiscardPileHead().get().motherNatureMovement() + "|");
            } else out.print("|     |");
            space();
        }
        out.println();
        centre();
        for (int i = 0; i < players.size(); i++) {
            out.print("|     |");
            space();
        }
        out.println();
        centre();
        for (int i = 0; i < players.size(); i++) {
            out.print("|_____|");
            space();
        }
    }

    /**
     * space between cards
     */
    private void space() {
        int space = 45;
        for (int i = 0; i < space; i++) System.out.print(" ");
    }

    /**
     * left space for centering the card
     */
    private void centre() {
        int space = 15;
        for (int i = 0; i < space; i++) System.out.print(" ");
    }

}
