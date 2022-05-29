package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.model.AssistantCard;

import java.io.PrintStream;
import java.util.List;

public class PrinterAssistantCards {

    public void print(List<PlayerInfo> players) {
        PrintStream out = System.out;
        out.println();
        space(15);
        for (int i = 0; i < players.size(); i++) {
            out.print(" _____ ");
            space();
        }
        out.println();
        space(15);
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getDiscardPileHead()!=null)
                out.print("|" + players.get(i).getDiscardPileHead().value() + "   " + players.get(i).getDiscardPileHead().motherNatureMovement() + "|");
            else out.print("|     |");
            space();
        }
        out.println();
        space(15);
        for (int i = 0; i < players.size(); i++) {
            out.print("|     |");
            space();
        }
        out.println();
        space(15);
        for (int i = 0; i < players.size(); i++) {
            out.print("|_____|");
            space();
        }
    }

    private void space() {
        int space = 45;
        for (int i = 0; i < space; i++) System.out.print(" ");
    }

    private void space(int space) {
        for (int i = 0; i < space; i++) System.out.print(" ");
    }

}
