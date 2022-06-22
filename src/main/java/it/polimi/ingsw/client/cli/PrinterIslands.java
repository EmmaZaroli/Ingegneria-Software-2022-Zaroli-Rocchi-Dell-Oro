package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.dtos.IslandCardDto;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;

/**
 * Class for printing LinkedIslands
 * We have chosen to print students only on the main islands
 * Therefore, when an island is joined to another, the printing will be made only on the second one
 * The towers will be printed on all islands regardless of their type
 * the printing is done on the two sides of the islands
 * in the first side, the first two spaces are reserved for the printing of the tower and possibly of mother nature
 */
public class PrinterIslands {


    private static final String MOTHER_NATURE = "M";
    public static final String TOP = "_______";
    public static final String SIDE = "|       |";
    public static final String CONNECTED_SIDE_SX = "|        ";
    public static final String CONNECTED_SIDE_DX = "        |";
    public static final String CONNECTED_BOTTOM_SX = "|________";
    public static final String BOTTOM = "|_______|";
    public static final String CONNECTED_BOTTOM_DX = "________|";
    public static final String CONNECTED_BOTTOM_MIDDLE = "_________";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String FULL_CIRCLE = "●";
    public static final String TOWER_WHITE = "█";
    public static final String TOWER_BLACK = ANSI_BLACK + TOWER_WHITE + ANSI_RESET;
    public static final String TOWER_GREY = ANSI_WHITE + TOWER_WHITE + ANSI_RESET;
    private boolean isnext;
    private int[] size = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] studentsAlreadyPrinted = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private String[] islandsIndex = {"","","","","","","","","","","",""};
    private boolean upConnected;
    private boolean up0;
    private boolean up4;

    /**
     * Prints the LinkedIslands
     * @param islands the linkedIslands on the table
     */
    public void printIslands(List<LinkedIslands> islands) {

        //initializing
        for (int i = 0; i < 12; i++) {
            size[i] = 0;
            studentsAlreadyPrinted[i] = 0;
        }
        isnext = false;
        upConnected = false;
        up0 = false;
        up4 = false;

        //index assignment

        int index = 1;
        for(int i=0;i<islands.size();i++){
            if(islands.get(i).isMainIsland()) {
                islandsIndex[i] = Integer.toString(index);
                index++;
            }
            else islandsIndex[i] = "  ";
        }

        //first row numbers
        firstRowNumbers();
        //first row
        firstRow(islands);

        System.out.println();
        isnext = false;

        //second row
        //top
        centre();
        if (!up0) System.out.print(" ");
        int intermediate = (islands.get(11).getIsland().getStudents().size() - 11) / 2;
        size[11] = Math.max(intermediate, 0);
        if (islands.get(11).isMainIsland()) topWithConnection(up0, size[11]);
        else topWithConnection(up0);
        System.out.print("                                 ");
        if (up0) System.out.print("  ");
        intermediate = (islands.get(5).getIsland().getStudents().size() - 11) / 2;
        size[5] = Math.max(intermediate, 0);
        if (!up4) System.out.print(" ");
        if (islands.get(5).isMainIsland()) topWithConnection(up4, size[5]);
        else topWithConnection(up4);
        System.out.println();

        //side 1
        centre();
        if (islands.get(11).isMainIsland()) side(false, false, islands.get(11).getIsland(), 11, 5);
        else side(false, false, islands.get(11).getIsland().getTower());
        System.out.print("                                 ");
        if (islands.get(5).isMainIsland()) side(false, false, islands.get(5).getIsland(), 5, 5);
        else side(false, false, islands.get(5).getIsland().getTower());
        System.out.println();

        //side 2
        space(37);
        System.out.print(islandsIndex[11]); // 1 space
        space(1);
        if (islands.get(11).isMainIsland()) side(false, false, islands.get(11).getIsland(), 11, 7);
        else side(false, false, Tower.NONE);
        System.out.print("                                 ");
        if (islands.get(5).isMainIsland()) side(false, false, islands.get(5).getIsland(), 5, 7);
        else side(false, false, Tower.NONE);
        System.out.print(islandsIndex[5]);
        System.out.println();

        //bottom
        centre();
        up0 = islands.get(10).isConnectedWithNext();
        up4 = islands.get(5).isConnectedWithNext();
        if (up0) {
            System.out.print(SIDE + "  ");
        } else bottom(false, false);
        System.out.print("                                 ");
        if (up4) System.out.print(SIDE);
        else bottom(false, false);
        System.out.println();
        centre();

        //third row
        thirdRow(islands);
        //third row numbers
        thirdRowNumbers();
        System.out.println();
    }

    /**
     * Prints the indexes for the first row of LinkedIslands
     */
    private void firstRowNumbers(){
        centre();
        space(4);
        for(int i=0;i<5;i++){
            System.out.print(islandsIndex[i]);
            space(10);
        }
        System.out.println();
    }

    /**
     * Prints the indexes for the third row of LinkedIslands
     */
    private void thirdRowNumbers(){
        System.out.println();
        centre();
        space(3); //maybe 4
        for(int i=10;i>5;i--) {
            System.out.print(islandsIndex[i]);
            space(10);
        }
    }

    /**
     * Prints the first row of linkedIslands
     * @param islands the linkedIslands
     */
    private void firstRow(List<LinkedIslands> islands) {
        //top
        centre();
        firstRowTop(islands);

        System.out.println();
        centre();

        //sides

        firstRowSideone(islands);

        isnext = false;
        System.out.println();
        centre();

        //bottom
        firstRowBottom(islands);

    }

    /**
     * Prints the top of the first row of islands
     * @param islands the linkedIslands
     */
    private void firstRowTop(List<LinkedIslands> islands) {
        //top
        System.out.print(" ");
        for (int i = 0; i < 5; i++) {
            int intermediate = (islands.get(i).getIsland().getStudents().size() - 11) / 2;
            size[i] = Math.max(intermediate, 0);
            if (islands.get(i).isMainIsland())
                top(islands.get(i).isConnectedWithNext() && i != 4, size[i]);
            else top(islands.get(i).isConnectedWithNext() && i != 4);
        }
    }

    /**
     * Prints the first side of the first row of islands
     * @param islands the linked islands
     */
    private void firstRowSideone(List<LinkedIslands> islands) {
        for (int i = 0; i < 5; i++) {
            if (islands.get(i).isConnectedWithNext() && i != 4) {
                if (islands.get(i).isMainIsland()) side(true, isnext, islands.get(i).getIsland(), i, 5);
                else side(true, isnext, islands.get(i).getIsland().getTower());
                isnext = true;
            } else {
                if (islands.get(i).isMainIsland()) side(false, false, islands.get(i).getIsland(), i, 5);
                else side(false, false, islands.get(i).getIsland().getTower());
                if (isnext) isnext = false;
            }
        }

        isnext = false;
        System.out.println();
        centre();
        firstRowSidetwo(islands);

    }

    /**
     * Prints the second side of the first row of islands
     * @param islands the linked islands
     */
    private void firstRowSidetwo(List<LinkedIslands> islands) {
        for (int i = 0; i < 5; i++) {
            if (islands.get(i).isConnectedWithNext() && i != 4) {
                if (islands.get(i).isMainIsland()) side(true, isnext, islands.get(i).getIsland(), i, 7);
                else side(true, isnext, Tower.NONE);
                isnext = true;
            } else {
                if (islands.get(i).isMainIsland()) side(false, false, islands.get(i).getIsland(), i, 7);
                else side(false, false, Tower.NONE);
                if (isnext) isnext = false;
            }
        }
    }

    /**
     * Prints the bottoms of the islands of the first row
     * @param islands the linked islands
     */
    private void firstRowBottom(List<LinkedIslands> islands) {
        for (int i = 0; i < 4; i++) {
            upConnected = false;
            if (i == 0 && islands.get(11).isConnectedWithNext()) {
                upConnected = true;
                up0 = true;
            }
            if (islands.get(i).isConnectedWithNext()) {
                if (isnext) bottom(true, true);
                else {
                    if (upConnected) upConnected(true, false);
                    else bottom(true, false);
                }
                isnext = true;
            } else {
                if (upConnected) upConnected(false, false);
                else bottom(false, false);
                if (isnext) isnext = false;
            }
        }

        if (islands.get(4).isConnectedWithNext()) {
            up4 = true;
            upConnected(isnext, true);
        } else {
            if (isnext) bottomdx();
            else bottom(false, false);
        }
    }

    /**
     * Prints the third row
     * @param islands the linked islands
     */
    private void thirdRow(List<LinkedIslands> islands) {
        int intermediate = (islands.get(10).getIsland().getStudents().size() - 11) / 2;
        size[10] = Math.max(intermediate, 0);
        upConnected = islands.get(10).isConnectedWithNext();
        if (islands.get(9).isConnectedWithNext()) {
            if (upConnected) {
                upConnected(true, false);
                System.out.print("_");
            } else {
                System.out.print(" ");
                top(true);
            }
        } else {
            if (upConnected) {
                upConnected(false, false);
                System.out.print(" ");
            } else {
                System.out.print(" ");
                top(false);
            }
        }

        //third row other tops
        thirdRowOtherTops(islands);

        System.out.println();
        centre();

        //sides
        thirdRowSides(islands);

        isnext = false;
        System.out.println();
        centre();

        //bottoms
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).isConnectedWithNext() && i != 6) {
                bottom(true, isnext);
                isnext = true;
            } else {
                bottom(false, false);
                if (isnext) isnext = false;
            }
        }
    }

    /**
     * Prints the tops of the third row of islands
     * @param islands the linked islands
     */
    private void thirdRowOtherTops(List<LinkedIslands> islands) {
        int intermediate;
        for (int i = 9; i > 6; i--) {
            intermediate = (islands.get(i).getIsland().getStudents().size() - 11) / 2;
            size[i] = Math.max(intermediate, 0);
            top(islands.get(i - 1).isConnectedWithNext());
        }
        //third row last island
        intermediate = (islands.get(5).getIsland().getStudents().size() - 11) / 2;
        size[5] = Math.max(intermediate, 0);
        upConnected = islands.get(5).isConnectedWithNext();
        if (islands.get(6).isConnectedWithNext()) {
            if (upConnected) {
                System.out.print("       |");
            } else {
                top(false);
            }
        } else {
            if (upConnected) {
                upConnected(false, false);
            } else {
                top(false);
            }
        }


    }

    /**
     * Prints the first side of the third row of islands
     * @param islands the linked islands
     */
    private void thirdRowSides(List<LinkedIslands> islands) {
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).isConnectedWithNext() && i != 6) {
                if (islands.get(i).isMainIsland()) side(true, isnext, islands.get(i).getIsland(), i, 5);
                else side(true, isnext, islands.get(i).getIsland().getTower());
                isnext = true;
            } else {
                if (islands.get(i).isMainIsland()) side(false, false, islands.get(i).getIsland(), i, 5);
                else side(false, false, islands.get(i).getIsland().getTower());
                if (isnext) isnext = false;
            }
        }
        System.out.println();
        centre();
        isnext = false;
        thirdRowSideTwo(islands);

    }

    /**
     * Prints the second side of the third row of islands
     * @param islands the linked islands
     */
    private void thirdRowSideTwo(List<LinkedIslands> islands) {
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).isConnectedWithNext() && i != 6) {
                if (islands.get(i).isMainIsland()) side(true, isnext, islands.get(i).getIsland(), i, 7);
                else side(true, isnext, Tower.NONE);
                isnext = true;
            } else {
                if (islands.get(i).isMainIsland()) side(false, false, islands.get(i).getIsland(), i, 7);
                else side(false, false, Tower.NONE);
                if (isnext) isnext = false;
            }
        }
    }


    /**
     * Used only for the two centered islands (second row) if they are not considered as main islands
     * @param connected is true if the island is connected with the upper one
     */
    private void topWithConnection(boolean connected) {
        if (connected) System.out.print(SIDE);
        else top(false);
    }

    /**
     * Used only for the two centered islands (second row) if they are considered as main islands
     * @param connected is true if the island is connected with the upper one
     * @param extension additional space in case the space already present for the students' printing is not enough
     */
    private void topWithConnection(boolean connected, int extension) {
        if (connected) {
            System.out.print("|       ");
            for (int i = 0; i < extension; i++) {
                System.out.print(" ");
            }
            System.out.print("|");
        } else top(false, extension);
    }

    /**
     * Prints the top of the island depending on whether the island is connected or not
     * Called only if the island is not a main island
     * @param connected is true if the island is connected to the next one
     */
    private void top(boolean connected) {

        if (connected) {
            System.out.print(TOP);
            System.out.print("____");
        } else {
            System.out.print(TOP);
            System.out.print("    ");
        }
    }

    /**
     * Prints the top of the island depending on whether the island is connected or not
     * Called only if the island is a main island
     * @param connected is true if the island is connected to the next one
     * @param extension additional space in case the space already present for the students' printing is not enough
     */
    private void top(boolean connected, int extension) {

        if (connected) {
            System.out.print(TOP);
            System.out.print("____");
            for (int i = 0; i < extension; i++) {
                System.out.print("_");
            }
        } else {
            System.out.print(TOP);
            for (int i = 0; i < extension; i++) {
                System.out.print("_");
            }
            System.out.print("    ");
        }
    }

    /**
     * Prints the side of the island depending on whether the island is connected or not
     * Called only if the island is not a main island
     * @param connected is true if the island is connected with the next one
     * @param middle is true if the island is connected with the previous one and the next one
     * @param tower the color of the tower
     */
    private void side(boolean connected, boolean middle, Tower tower) {
        if (!connected && !isnext) {
            System.out.print("| " + this.towersColor(tower) + "     |");
        } else {
            if (isnext) {
                if (middle) System.out.print("  " + this.towersColor(tower) + "      ");
                else System.out.print("  " + this.towersColor(tower) + "     |");
            } else System.out.print("| " + this.towersColor(tower) + "      ");
        }
        System.out.print("  ");
    }

    /**
     * Prints the side of the island depending on whether the island is connected or not
     * Called only if the island is a main island
     * @param connected is true if the island is connected with the next one
     * @param middle is true if the island is connected with the previous one and the next one
     * @param island the island to print
     * @param indexIsland the index of the island to print
     * @param maxSize is 5 if it's the first side, 7 if it's the second one. represents the number of students that can be printed on that line
     */
    private void side(boolean connected, boolean middle, IslandCardDto island, int indexIsland, int maxSize) {
        if (!connected && !isnext) {
            System.out.print("|");
            printIslandContent(island, indexIsland, maxSize);
            System.out.print("|");
        } else {
            if (isnext) {
                if (middle) {
                    System.out.print(" ");
                    printIslandContent(island, indexIsland, maxSize);
                    System.out.print(" ");
                } else {
                    System.out.print(" ");
                    printIslandContent(island, indexIsland, maxSize);
                    System.out.print("|");
                }
            } else {
                System.out.print("|");
                printIslandContent(island, indexIsland, maxSize);
                System.out.print(" ");
            }
        }
        System.out.print("  ");
    }

    /**
     * Prints the content of the island
     * If the max size is 5, it prints, in order, mother nature, the tower and max five students
     * If the max size is 7, it prints the remaining students, if any
     * @param island the island to print
     * @param indexIsland the index of the island to print
     * @param maxSize is 5 if it's the first side, 7 if it's the second one.
     * */
    private void printIslandContent(IslandCardDto island, int indexIsland, int maxSize) {
        if (maxSize == 5) {
            if (island.isHasMotherNature()) System.out.print(MOTHER_NATURE);
            else System.out.print(" ");
            System.out.print(towersColor(island.getTower()));
            int i;
            for (i = 0; i < maxSize + size[indexIsland] && i < island.getStudents().size(); i++) {
                System.out.print(assignColor(island.getStudents().get(i)));
                studentsAlreadyPrinted[indexIsland]++;
            }
            for (; i < maxSize + size[indexIsland]; i++) {
                System.out.print(" ");
            }
        } else {
            int i;
            for (i = studentsAlreadyPrinted[indexIsland]; i < studentsAlreadyPrinted[indexIsland] + maxSize + size[indexIsland] && i < island.getStudents().size(); i++) {
                System.out.print(assignColor(island.getStudents().get(i)));
            }
            for (i = i - studentsAlreadyPrinted[indexIsland]; i < maxSize + size[indexIsland]; i++) {
                System.out.print(" ");
            }
        }
    }

    /**
     * Prints the bottom of the island
     * @param connected is true if the island is connected to the next one
     * @param middle is true if the island is connected with the previous one and the next one
     */
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

    private void bottomdx() {
        System.out.print(CONNECTED_BOTTOM_DX);
    }

    private void upConnected(boolean side, boolean dx) {
        if (side && !dx) {
            System.out.print(CONNECTED_SIDE_SX);
            System.out.print("__");
        } else if (side) {
            System.out.print(CONNECTED_SIDE_DX);

        } else {
            System.out.print(SIDE);
            System.out.print("  ");
        }

    }

    /**
     *
     * @param studentColor the pawnColor
     * @return a string corresponding to the color of the pawnColor
     */
    private static String assignColor(PawnColor studentColor) {
        String assignedColor;
        switch (studentColor) {
            case RED -> assignedColor = ANSI_RED;
            case BLUE -> assignedColor = ANSI_BLUE;
            case GREEN -> assignedColor = ANSI_GREEN;
            case YELLOW -> assignedColor = ANSI_YELLOW;
            case PINK -> assignedColor = ANSI_PURPLE;
            default -> assignedColor = ANSI_WHITE;
        }
        return assignedColor + FULL_CIRCLE + ANSI_RESET;
    }

    /**
     *
     * @param color the Tower
     * @return a string corresponding to the color of the Tower
     */
    private String towersColor(Tower color) {
        String towerColor;
        switch (color) {
            case BLACK -> towerColor = TOWER_BLACK;
            case WHITE -> towerColor = TOWER_WHITE;
            case GREY -> towerColor = TOWER_GREY;
            default -> towerColor = " ";
        }
        return towerColor;
    }

    /**
     * Used for centering the islands
     */
    private void centre() {
        for (int i = 0; i < 40; i++) System.out.print(" ");
    }

    /**
     *
     * @param space the number of spaces to leave of the left
     */
    private void space(int space) {
        for(int i=0;i<space;i++) System.out.print(" ");
    }

}
