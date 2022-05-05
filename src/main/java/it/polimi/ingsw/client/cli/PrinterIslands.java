package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.model.IslandCard;

import java.awt.*;
import java.util.List;

public class PrinterIslands {

    public static final Color brown = new Color(139, 69, 19);
    private final String MOTHER_NATURE = "▲";
    public static final String TOP = "_______";
    public static final String SIDE = "|       |";
    public static final String CONNECTED_SIDE_SX = "|        ";
    public static final String CONNECTED_SIDE_DX = "        |";
    public static final String CONNECTED_SIDE_MIDDLE = "         ";
    public static final String CONNECTED_BOTTOM_SX = "|________";
    public static final String BOTTOM = "|_______|";
    public static final String CONNECTED_BOTTOM_DX = "________|";
    public static final String CONNECTED_BOTTOM_MIDDLE = "_________";
    private boolean isnext = false;

    public void printIslands(List<LinkedIslands> islands) {

        //TODO fix if i=5 case
        //first fow
        System.out.print(" ");
        for (int i = 0; i < 5; i++) {
            if (islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland()))
                top(true);
            else top(false);
        }

        System.out.println();

        for (int i = 0; i < 5; i++) {
            if (islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland())) {
                if (isnext) {
                    side(true, true);
                } else side(true, false);
                isnext = true;
            } else {
                side(false, false);
                if (isnext) isnext = false;
            }
        }

        isnext = false;
        System.out.println();

        for (int i = 0; i < 5; i++) {
            if (islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland())) {
                if (isnext) {
                    bottom(true, true);
                } else bottom(true, false);
                isnext = true;
            } else {
                bottom(false, false);
                if (isnext) isnext = false;
            }
        }
        System.out.println();


        //second row
        top(false);
        System.out.print("                                 ");
        top(false);
        System.out.println();
        side(false, false);
        System.out.print("                                 ");
        side(false, false);
        System.out.println();
        bottom(false, false);
        System.out.print("                                 ");
        bottom(false, false);
        System.out.println();

        //third row
        for (int i = 0; i < 5; i++) {
            top(false);
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            side(false, false);
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            bottom(false, false);
        }
        System.out.println();
    }

    private void top(boolean connected) {

        if (connected) {
            System.out.print(TOP);
            System.out.print("____");
        } else {
            System.out.print(TOP);
            System.out.print("    ");
        }
    }

    private void side(boolean connected, boolean middle) {
        if (!connected && !isnext) {
            System.out.print(SIDE);
        } else {
            if (isnext) {
                if (middle) System.out.print(CONNECTED_SIDE_MIDDLE);
                else System.out.print(CONNECTED_SIDE_DX);
            } else System.out.print(CONNECTED_SIDE_SX);
        }
        System.out.print("  ");
    }

    private void bottom(boolean connected, boolean middle) {
        if (!connected && !isnext) {
            System.out.print(BOTTOM);
            System.out.print("  ");
        } else {
            if (isnext) {
                if (middle) {
                    System.out.print(CONNECTED_BOTTOM_MIDDLE);
                    System.out.print("__");
                } else {
                    System.out.print(CONNECTED_BOTTOM_DX);
                    System.out.print("  ");
                }

            } else {
                System.out.print(CONNECTED_BOTTOM_SX);
                System.out.print("__");
            }

        }
    }

}
