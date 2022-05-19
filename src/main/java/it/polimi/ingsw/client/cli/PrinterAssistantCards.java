package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.AssistantCard;

import java.io.PrintStream;
import java.util.List;

public class PrinterAssistantCards {

    public void print(List<AssistantCard> deck) {
        PrintStream out = System.out;
        out.println();
        space(15);
        for (int i = 0; i < deck.size(); i++) {
            out.print(" _____ ");
            space();
        }
        out.println();
        space(15);
        for (int i = 0; i < deck.size(); i++) {
            out.print("|" + deck.get(i).value() + "   " + deck.get(i).motherNatureMovement() + "|");
            space();
        }
        out.println();
        space(15);
        for (int i = 0; i < deck.size(); i++) {
            out.print("|     |");
            space();
        }
        out.println();
        space(15);
        for (int i = 0; i < deck.size(); i++) {
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
