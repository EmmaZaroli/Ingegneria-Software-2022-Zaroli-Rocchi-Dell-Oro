package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.List;

public class PrinterClouds {
    public static final String FULL_CIRCLE = "●";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";

    //TODO instead of 2 put parameter based on the number of player
    public void printClouds(List clouds) {
        int i;
        for (i = 0; i < 2; i++) {

            System.out.print("   _____   ");
            System.out.print("       ");
        }
        System.out.println();
        for (i = 0; i < 2; i++) {
            if (((CloudTile) clouds.get(i)).getStudentsFromCloud().isEmpty())
                System.out.print("  (     )  ");
            else {
                PawnColor student1 = ((CloudTile) clouds.get(i)).getStudentsFromCloud().get(0);
                PawnColor student2 = ((CloudTile) clouds.get(i)).getStudentsFromCloud().get(1);
                PawnColor student3 = ((CloudTile) clouds.get(i)).getStudentsFromCloud().get(2);
                System.out.print("  ( " + assignColor(student1, FULL_CIRCLE) + assignColor(student2, FULL_CIRCLE) + assignColor(student3, FULL_CIRCLE) + " )  ");
            }
            System.out.print("       ");
        }
        System.out.println();
        for (i = 0; i < 2; i++) {
            System.out.print("(____)____)");
            System.out.print("       ");
        }
        System.out.println();
    }


    private static String assignColor(PawnColor studentColor, String student) {
        String assignedColor;
        switch (studentColor) {
            case RED -> assignedColor = ANSI_RED;
            case BLUE -> assignedColor = ANSI_BLUE;
            case GREEN -> assignedColor = ANSI_GREEN;
            case YELLOW -> assignedColor = ANSI_YELLOW;
            case PINK -> assignedColor = ANSI_PURPLE;
            default -> assignedColor = ANSI_WHITE;
        }
        return assignedColor + student + ANSI_RESET;
    }
}
