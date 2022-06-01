package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.PrintStream;
import java.util.List;

public class PrinterSchoolBoard {
    public static final String ANSI_BLACK = "\u001B[30m";
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
    public static final String PROFESSOR_EMPTY = "⬡";
    public static final String PROFESSOR_FULL = "⬢";
    public static final String TOWER_WHITE = "█";
    public static final String TOWER_BLACK = ANSI_BLACK + TOWER_WHITE + ANSI_RESET;
    public static final String TOWER_GREY = ANSI_WHITE + TOWER_WHITE + ANSI_RESET;


    public void printBoard(List<SchoolBoardDto> boards) {
        PrintStream out = System.out;

        //print board
        int[] actualEntranceSize = {0, 0, 0};
        int[] countTowers = {0, 0, 0};
        for (int i = 0; i < boards.size(); i++) {
            actualEntranceSize[i] = boards.get(i).getEntrance().size();
            countTowers[i] = 0;
        }

        for (int i = 0; i < boards.size(); i++) {
            out.print(" _______________________________________");
            out.print("            "); //12 spaces
        }
        out.println();

        //first line
        for (int i = 0; i < boards.size(); i++) {
            String student = 0 >= actualEntranceSize[i] ? EMPTY_CIRCLE : FULL_CIRCLE;
            if (student.equals(FULL_CIRCLE)) student = assignColor(boards.get(i).getEntrance().get(0), student);
            out.print("|   " + student + " ");
            studentsTable(0, boards.get(i));
            System.out.print("        |");
            out.print("          "); //10 spaces
        }
        out.println();

        //other rows
        int studentPosition = 1;
        for (int rows = 1; rows < 5; rows++) {
            for (int j = 0; j < boards.size(); j++) {
                String student1 = studentPosition >= actualEntranceSize[j] ? EMPTY_CIRCLE : FULL_CIRCLE;
                if (student1.equals(FULL_CIRCLE))
                    student1 = assignColor(boards.get(j).getEntrance().get(studentPosition), student1);
                String student2 = studentPosition + 1 >= actualEntranceSize[j] ? EMPTY_CIRCLE : FULL_CIRCLE;
                if (student2.equals(FULL_CIRCLE))
                    student2 = assignColor(boards.get(j).getEntrance().get(studentPosition + 1), student2);
                out.print("| " + student1 + " " + student2 + " ");
                studentsTable(rows, boards.get(j));
                countTowers[j] = Towers(boards.get(j).getTowersCount(), boards.get(j).getTowerColor(), countTowers[j]);
                System.out.print("  |");
                out.print("          "); //10 spaces
            }
            studentPosition = studentPosition + 2;
            out.println();
        }
        out.print("|________________________________________|");
        out.print("          ");
        out.print("|________________________________________|");
        if (boards.size() == 3) {
            out.print("         ");
            out.print("|________________________________________|");
        }
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

    private void studentsTable(int rows, SchoolBoardDto board) {
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
        int occupiedCells = board.getDiningRoom().getStudentsInDiningRoom(color);
        for (i = 0; i < occupiedCells; i++) {
            System.out.print(" " + colorRow + FULL_CIRCLE + ANSI_RESET);
        }
        for (int j = i; j < 10; j++) {
            if ((j + 1) % 3 == 0) {
                System.out.print(" " + colorRow + COIN + ANSI_RESET);
            } else
                System.out.print(" " + colorRow + CIRCLE + ANSI_RESET);
        }

        for (i = 0; i < occupiedCells / 3; i++) System.out.print(" "); //space corrector
        if (board.isThereProfessor(color)) {
            System.out.print("  " + colorRow + PROFESSOR_FULL + ANSI_RESET);
        } else
            System.out.print("  " + colorRow + PROFESSOR_EMPTY + ANSI_RESET);

    }

    private int Towers(int towerOnBoard, Tower color, int countTowers) {
        String towerColor;
        switch (color) {
            case BLACK -> towerColor = TOWER_BLACK;
            case WHITE -> towerColor = TOWER_WHITE;
            case GREY -> towerColor = TOWER_GREY;
            default -> towerColor = ANSI_RED;
        }
        int spaceCount = 0;
        for (int i = 0; i < 2; i++) {
            if (countTowers < towerOnBoard) {
                countTowers++;
                System.out.print("  " + towerColor);
            } else {
                System.out.print("   ");
                spaceCount++;
            }
        }
        if (spaceCount == 2) System.out.print(" ");
        return countTowers;
    }


}
