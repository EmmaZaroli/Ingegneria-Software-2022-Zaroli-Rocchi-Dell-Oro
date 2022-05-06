package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.LinkedIslands;

import java.awt.*;
import java.util.List;

public class PrinterIslands {

    public static final Color brown = new Color(139, 69, 19);
    private static final String MOTHER_NATURE = "▲";
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
    private boolean upConnected = false;
    private boolean up0 = false;
    private boolean up4 = false;

    public void printIslands(List<LinkedIslands> islands) {

        //first fow
        System.out.print(" ");
        for (int i = 0; i < 5; i++) {
            top(islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland()) && i != 4);
        }

        System.out.println();

        for (int i = 0; i < 5; i++) {
            if (islands.get(i).getLinkedislands().contains(islands.get(i + 1).getMainIsland()) && i != 4) {
                side(true, isnext);
                isnext = true;
            } else {
                side(false, false);
                if (isnext) isnext = false;
            }
        }

        isnext = false;
        System.out.println();

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

        System.out.println();
        isnext = false;

        //second row
        if (!up0) System.out.print(" ");
        topWithConnection(up0);
        System.out.print("                                 ");
        if (up0) System.out.print("  ");
        if (!up4) System.out.print(" ");
        topWithConnection(up4);
        System.out.println();
        side(false, false);
        System.out.print("                                 ");
        side(false, false);
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

        //third row first island
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
        for (int i = 9; i > 6; i--) {
            top(islands.get(i - 1).getLinkedislands().contains(islands.get(i).getMainIsland()));
        }
        //third row first island
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


        System.out.println();
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).getLinkedislands().contains(islands.get(i).getMainIsland()) && i != 6) {
                side(true, isnext);
                isnext = true;
            } else {
                side(false, false);
                if (isnext) isnext = false;
            }
        }
        System.out.println();
        for (int i = 10; i > 5; i--) {
            if (islands.get(i - 1).getLinkedislands().contains(islands.get(i).getMainIsland()) && i != 6) {
                bottom(true, isnext);
                isnext = true;
            } else {
                bottom(false, false);
                if (isnext) isnext = false;
            }
        }
        System.out.println();
    }

    private void topWithConnection(boolean connected) {
        if (connected) System.out.print(SIDE);
        else top(false);
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

}
