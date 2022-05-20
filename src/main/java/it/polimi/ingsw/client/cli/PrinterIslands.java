package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;

public class PrinterIslands {

    //TODO extend top third row and all the bottom

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
    public static final String TOWER_WHITE = "\uD83D\uDEE2";
    public static final String TOWER_BLACK = ANSI_BLACK + TOWER_WHITE + ANSI_RESET;
    public static final String TOWER_GREY = ANSI_WHITE + TOWER_WHITE + ANSI_RESET;
    private boolean isnext;
    private int[] size = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] studentsAlreadyPrinted = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private boolean upConnected;
    private boolean up0;
    private boolean up4;

    public void printIslands(List<LinkedIslands> islands) {
        //TODO initialized the arrays
        isnext = false;
        upConnected = false;
        up0 = false;
        up4 = false;

        //first fow
        firstRow(islands);

        System.out.println();
        isnext = false;

        //second row
        if (!up0) System.out.print(" ");
        int intermediate = (islands.get(11).getMainIsland().getStudentsFromIsland().size() - 11) / 2;
        size[11] = Math.max(intermediate, 0);
        if (islands.get(11).isMainConnected()) topWithConnection(up0, size[11]);
        else topWithConnection(up0);
        System.out.print("                                 ");
        if (up0) System.out.print("  ");
        intermediate = (islands.get(5).getMainIsland().getStudentsFromIsland().size() - 11) / 2;
        size[5] = Math.max(intermediate, 0);
        if (!up4) System.out.print(" ");
        if (islands.get(5).isMainConnected()) topWithConnection(up4, size[5]);
        else topWithConnection(up4);
        System.out.println();

        if (islands.get(11).isMainConnected()) side(false, false, islands.get(11).getMainIsland(), 11, 5);
        else side(false, false, islands.get(11).getMainIsland().getTower());
        System.out.print("                                 ");
        if (islands.get(5).isMainConnected()) side(false, false, islands.get(5).getMainIsland(), 5, 5);
        else side(false, false, islands.get(5).getMainIsland().getTower());
        System.out.println();
        if (islands.get(11).isMainConnected()) side(false, false, islands.get(11).getMainIsland(), 11, 7);
        else side(false, false, Tower.NONE);
        System.out.print("                                 ");
        if (islands.get(5).isMainConnected()) side(false, false, islands.get(5).getMainIsland(), 5, 7);
        else side(false, false, Tower.NONE);
        System.out.println();

        up0 = islands.get(10).getLinkedislands().contains(islands.get(11).getMainIsland());
        up4 = islands.get(5).getLinkedislands().contains(islands.get(6).getMainIsland());
        if (up0) {
            System.out.print(SIDE + "  ");
        } else bottom(false, false);
        System.out.print("                                 ");
        if (up4) System.out.print(SIDE);
        else bottom(false, false);
        System.out.println();

        thirdRow(islands);

        System.out.println();
    }

    private void firstRow(List<LinkedIslands> islands) {
        //top
        firstRowTop(islands);

        System.out.println();

        //sides

        firstRowSideone(islands);

        isnext = false;
        System.out.println();

        //bottom
        firstRowBottom(islands);

    }

    private void firstRowTop(List<LinkedIslands> islands) {
        //top
        System.out.print(" ");
        for (int i = 0; i < 5; i++) {
            int intermediate = (islands.get(i).getMainIsland().getStudentsFromIsland().size() - 11) / 2;
            size[i] = Math.max(intermediate, 0);
            if (islands.get(i).isMainConnected())
                top(islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland()) && i != 4, size[i]);
            else top(islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland()) && i != 4);
        }
    }

    private void firstRowSideone(List<LinkedIslands> islands) {
        for (int i = 0; i < 5; i++) {
            if (islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland()) && i != 4) {
                if (islands.get(i).isMainConnected()) side(true, isnext, islands.get(i).getMainIsland(), i, 5);
                else side(true, isnext, islands.get(i).getMainIsland().getTower());
                isnext = true;
            } else {
                if (islands.get(i).isMainConnected()) side(false, false, islands.get(i).getMainIsland(), i, 5);
                else side(false, false, islands.get(i).getMainIsland().getTower());
                if (isnext) isnext = false;
            }
        }

        isnext = false;
        System.out.println();
        firstRowSidetwo(islands);

    }

    private void firstRowSidetwo(List<LinkedIslands> islands) {
        for (int i = 0; i < 5; i++) {
            if (islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland()) && i != 4) {
                if (islands.get(i).isMainConnected()) side(true, isnext, islands.get(i).getMainIsland(), i, 7);
                else side(true, isnext, Tower.NONE);
                isnext = true;
            } else {
                if (islands.get(i).isMainConnected()) side(false, false, islands.get(i).getMainIsland(), i, 7);
                else side(false, false, Tower.NONE);
                if (isnext) isnext = false;
            }
        }
    }

    private void firstRowBottom(List<LinkedIslands> islands) {
        for (int i = 0; i < 4; i++) {
            upConnected = false;
            if (i == 0 && islands.get(11).getLinkedislands().contains(islands.get(i).getMainIsland())) {
                upConnected = true;
                up0 = true;
            }
            if (islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland())) {
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

        if (islands.get(4).getLinkedislands().contains(islands.get(5).getMainIsland())) {
            up4 = true;
            upConnected(isnext, true);
        } else {
            if (isnext) bottomdx();
            else bottom(false, false);
        }
    }

    private void thirdRow(List<LinkedIslands> islands) {
        int intermediate = (islands.get(10).getMainIsland().getStudentsFromIsland().size() - 11) / 2;
        size[10] = Math.max(intermediate, 0);
        upConnected = islands.get(10).getLinkedislands().contains(islands.get(11).getMainIsland());
        if (islands.get(9).getLinkedislands().contains(islands.get(10).getMainIsland())) {
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

        //sides
        thirdRowSides(islands);

        isnext = false;
        System.out.println();

        //bottoms
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).getLinkedislands().contains(islands.get(i).getMainIsland()) && i != 6) {
                bottom(true, isnext);
                isnext = true;
            } else {
                bottom(false, false);
                if (isnext) isnext = false;
            }
        }
    }

    private void thirdRowOtherTops(List<LinkedIslands> islands) {
        int intermediate;
        for (int i = 9; i > 6; i--) {
            intermediate = (islands.get(i).getMainIsland().getStudentsFromIsland().size() - 11) / 2;
            size[i] = Math.max(intermediate, 0);
            top(islands.get(i - 1).getLinkedislands().contains(islands.get(i).getMainIsland()));
        }
        //third row last island
        intermediate = (islands.get(5).getMainIsland().getStudentsFromIsland().size() - 11) / 2;
        size[5] = Math.max(intermediate, 0);
        upConnected = islands.get(5).getLinkedislands().contains(islands.get(6).getMainIsland());
        if (islands.get(6).getLinkedislands().contains(islands.get(7).getMainIsland())) {
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

    private void thirdRowSides(List<LinkedIslands> islands) {
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).getLinkedislands().contains(islands.get(i).getMainIsland()) && i != 6) {
                if (islands.get(i).isMainConnected()) side(true, isnext, islands.get(i).getMainIsland(), i, 5);
                else side(true, isnext, islands.get(i).getMainIsland().getTower());
                isnext = true;
            } else {
                if (islands.get(i).isMainConnected()) side(false, false, islands.get(i).getMainIsland(), i, 5);
                else side(false, false, islands.get(i).getMainIsland().getTower());
                if (isnext) isnext = false;
            }
        }
        System.out.println();
        isnext = false;
        thirdRowSideTwo(islands);

    }

    private void thirdRowSideTwo(List<LinkedIslands> islands) {
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).getLinkedislands().contains(islands.get(i).getMainIsland()) && i != 6) {
                if (islands.get(i).isMainConnected()) side(true, isnext, islands.get(i).getMainIsland(), i, 7);
                else side(true, isnext, Tower.NONE);
                isnext = true;
            } else {
                if (islands.get(i).isMainConnected()) side(false, false, islands.get(i).getMainIsland(), i, 7);
                else side(false, false, Tower.NONE);
                if (isnext) isnext = false;
            }
        }
    }


    private void topWithConnection(boolean connected) {
        if (connected) System.out.print(SIDE);
        else top(false);
    }

    //for print
    private void topWithConnection(boolean connected, int extension) {
        if (connected) {
            System.out.print("|       ");
            for (int i = 0; i < extension; i++) {
                System.out.print(" ");
            }
            System.out.print("|");
        } else top(false, extension);
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

    //for print
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

    private void side(boolean connected, boolean middle, IslandCard island, int indexIsland, int maxSize) {
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

    private void printIslandContent(IslandCard island, int indexIsland, int maxSize) {
        if (maxSize == 5) {
            if (island.isHasMotherNature()) System.out.print(MOTHER_NATURE);
            else System.out.print(" ");
            System.out.print(towersColor(island.getTower()));
            int i;
            for (i = 0; i < maxSize + size[indexIsland] && i < island.getStudentsFromIsland().size(); i++) {
                System.out.print(assignColor(island.getStudentsFromIsland().get(i)));
                studentsAlreadyPrinted[indexIsland]++;
            }
            for (; i < maxSize + size[indexIsland]; i++) {
                System.out.print(" ");
            }
        } else {
            int i;
            for (i = studentsAlreadyPrinted[indexIsland]; i < studentsAlreadyPrinted[indexIsland] + maxSize + size[indexIsland] && i < island.getStudentsFromIsland().size(); i++) {
                System.out.print(assignColor(island.getStudentsFromIsland().get(i)));
            }
            for (i = i - studentsAlreadyPrinted[indexIsland]; i < maxSize + size[indexIsland]; i++) {
                System.out.print(" ");
            }
        }
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

}
