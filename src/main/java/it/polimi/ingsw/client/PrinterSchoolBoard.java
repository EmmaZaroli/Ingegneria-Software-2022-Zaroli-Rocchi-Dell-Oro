package it.polimi.ingsw.client;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.PrintStream;

public class PrinterSchoolBoard {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String FULL_CIRCLE = "●";
    public static final String EMPTY_CIRCLE = ANSI_WHITE + FULL_CIRCLE + ANSI_RESET;
    public static final String CIRCLE = "○";
    public static final String COIN = "⦿";

    public void printBoard(SchoolBoard board) {
        PrintStream out = System.out;

        //print board
        int actualEntranceSize = board.getEntrance().size();
        out.println(" _____________________________");

        //first line
        String student = 0 >= actualEntranceSize ? EMPTY_CIRCLE : FULL_CIRCLE;
        if (student.equals(FULL_CIRCLE)) student = assignColor(board.getEntrance().get(0), student);
        out.print("|   " + student + " ");
        studentsTable(0, board);

        int rows = 1;
        for (int i = 1; i < 9; i++) {
            String student1 = i >= actualEntranceSize ? EMPTY_CIRCLE : FULL_CIRCLE;
            if (student1.equals(FULL_CIRCLE)) student1 = assignColor(board.getEntrance().get(i), student1);
            i++;
            String student2 = i >= actualEntranceSize ? EMPTY_CIRCLE : FULL_CIRCLE;
            if (student2.equals(FULL_CIRCLE)) student2 = assignColor(board.getEntrance().get(i), student2);
            out.print("| " + student1 + " " + student2 + " ");
            studentsTable(rows, board);
            rows++;
        }
        out.println("|_____________________________|");

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

    private void studentsTable(int rows, SchoolBoard board) {
        int i = 0;
        String colorRow;
        PawnColor color;
        switch (rows) {
            case 0:
                colorRow = ANSI_GREEN;
                color = PawnColor.GREEN;
                break;
            case 1:
                colorRow = ANSI_RED;
                color = PawnColor.RED;
                break;
            case 2:
                colorRow = ANSI_YELLOW;
                color = PawnColor.YELLOW;
                break;
            case 3:
                colorRow = ANSI_PURPLE;
                color = PawnColor.PINK;
                break;
            case 4:
                colorRow = ANSI_BLUE;
                color = PawnColor.BLUE;
                break;
            default:
                colorRow = ANSI_WHITE;
                color = PawnColor.NONE;
                break;
        }
        int occupiedCells = board.getStudentsInDiningRoom(color);
        for (i = 0; i < occupiedCells; i++) {
            if ((i + 1) % 3 == 0) {
                System.out.print(" " + colorRow + COIN + ANSI_RESET);
            } else
                System.out.print(" " + colorRow + FULL_CIRCLE + ANSI_RESET);
        }
        for (int j = i; j < 10; j++) {
            if ((j + 1) % 3 == 0) {
                System.out.print(" " + colorRow + COIN + ANSI_RESET);
            } else
                System.out.print(" " + colorRow + CIRCLE + ANSI_RESET);
        }
        System.out.println(" |");
    }

}
