package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.IslandCard;

import java.awt.*;
import java.util.List;

public class PrinterIslands {

    public static final Color brown = new Color(139, 69, 19);
    private final String MOTHER_NATURE = "▲";
    public static final String TOP = " _______ ";
    public static final String SIDE = "|       |";
    public static final String BOTTOM = "|_______|";

    public void printIslands() {
        //first fow 
        for (int i = 0; i < 5; i++) {
            top();
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            side();
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            bottom();
        }
        System.out.println();

        //second row
        top();
        System.out.print("                                 ");
        top();
        System.out.println();
        side();
        System.out.print("                                 ");
        side();
        System.out.println();
        bottom();
        System.out.print("                                 ");
        bottom();
        System.out.println();

        //third row
        for (int i = 0; i < 5; i++) {
            top();
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            side();
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            bottom();
        }
        System.out.println();
    }

    private void top() {
        boolean connected = false;
        System.out.print(TOP);
        if (connected) {
            System.out.print("__");
        } else
            System.out.print("  ");
    }

    private void side() {
        boolean connected = false;
        System.out.print(SIDE);
        if (connected) {

        } else
            System.out.print("  ");
    }

    private void bottom() {
        boolean connected = false;
        System.out.print(BOTTOM);
        if (connected) {

        } else
            System.out.print("  ");
    }
}
