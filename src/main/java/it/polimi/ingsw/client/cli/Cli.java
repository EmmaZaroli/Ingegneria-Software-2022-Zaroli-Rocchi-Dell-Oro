package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.InputParsen;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Cli extends View {
    private final PrintStream out;
    private final Scanner in;
    private final InputParsen inputParsen;
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private PrinterSchoolBoard boardPrinter = new PrinterSchoolBoard();
    private PrinterClouds cloudPrinter = new PrinterClouds();
    private PrinterIslands islandsPrinter = new PrinterIslands();

    public Cli() {
        out = System.out;
        in = new Scanner(System.in);
        inputParsen = new InputParsen();
        //listeners.addPropertyChangeListener("action", network);
    }

    @Override
    protected void printWelcomeMessage() {
        out.println("      :::::::::: :::::::::  :::::::::::     :::     ::::    ::: ::::::::::: :::   :::  :::::::: \n" +
                "     :+:        :+:    :+:     :+:       :+: :+:   :+:+:   :+:     :+:     :+:   :+: :+:    :+: \n" +
                "    +:+        +:+    +:+     +:+      +:+   +:+  :+:+:+  +:+     +:+      +:+ +:+  +:+         \n" +
                "   +#++:++#   +#++:++#:      +#+     +#++:++#++: +#+ +:+ +#+     +#+       +#++:   +#++:++#++   \n" +
                "  +#+        +#+    +#+     +#+     +#+     +#+ +#+  +#+#+#     +#+        +#+           +#+    \n" +
                " #+#        #+#    #+#     #+#     #+#     #+# #+#   #+#+#     #+#        #+#    #+#    #+#     \n" +
                "########## ###    ### ########### ###     ### ###    ####     ###        ###     ########       ");

        out.println("Welcome to Eriantys Board Game!");

        try {

        } catch (Exception e) {
            out.println(e);
        }
    }

    @Override
    protected void askServerInfo() {
        //TODO
    }

    /**
     * Reads a line from the standard input.
     *
     * @return the string read from the input.
     */
    private String readLine() {
        String input = null;
        input = in.nextLine();
        return input;
    }

    protected void askPlayerNickname() {
        boolean valid = false;
        String nickname;
        do {
            out.println("Enter your nickname: ");
            nickname = readLine();
            valid = inputParsen.checkUsername(nickname);
        } while (!valid);
        //TODO
        //setNickname(nickname);
        listeners.firePropertyChange("nickname", true, nickname);
    }

    protected void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected) {

        //clearCli();
        if (playerReconnected) {
            out.println("You're being reconnected to your previous game");
        }
        if (nicknameAccepted && !playerReconnected) {
            out.println("Welcome, " + getMe().getNickname() + ", get ready to play!");
        }
        if (!nicknameAccepted && !playerReconnected) {
            out.println("Sorry, your nickname is already taken, please choose another one");
            askPlayerNickname();
        }

    }

    protected void askGameSettings() {
        int playersNumber;
        String gameMode;
        out.println("Please enter the Game mode: [normal/expert]");
        gameMode = readLine();
        //TODO check if is valid
        out.println("How many players are you going to play with? [2/3] ");
        playersNumber = Integer.parseInt(readLine());
        //TODO check if the number is 2 or 3
        listeners.firePropertyChange("gameSettings", gameMode, playersNumber);
    }

    protected void genericMessage(String message) {
        out.println(message);
    }

    protected void changePhase(GamePhase phase) {
        out.println("Phase: " + phase);
    }

    /**
     * print the Assistant Card of the player and asks him which one to play
     *
     * @param deck
     */
    protected void askAssistantCard(ArrayList<AssistantCard> deck) {

        out.println("chose the assistant card to play: ");
        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("     " + i + ".      ");
        }
        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("   _____     ");
        }
        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("  |" + deck.get(i).value() + "   " + deck.get(i).motherNatureMovement() + "|    ");
        }
        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("  |     |    ");
        }
        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("  |_____|    ");
        }
        out.println();
        int card;
        card = Integer.parseInt(readLine());
        //TODO check if the number is valid
        //TODO
        //setCardThrown(deck.get(card));
        listeners.firePropertyChange("assistantCard", true, deck.get(card));
    }

    protected void updateAssistantCardPlayed(AssistantCard card, String player) {
        if (!player.equals(getMe().getNickname())) {
            out.println(player + " has played ");
        } else out.println("you have played ");
        out.println("   _____     ");
        out.println("  |" + card.value() + "   " + card.motherNatureMovement() + "|    ");
        out.println("  |     |    ");
        out.println("  |_____|    ");
    }

    protected void askMotherNatureSteps() {
        out.println("How many steps do you want to move mother nature? ");
        int steps = Integer.parseInt(readLine());
        //TODO check if number is <0 or >cardplayed.steps
    }

    protected void updateCurrentPlayersTurn(String otherPlayer) {
        out.println("it's " + otherPlayer + "turn");
    }

    protected void updateCloud(CloudTile cloud) {
        if (getClouds().size() < 2) {
            getClouds().add(cloud);
        } else {
            for (int i = 0; i < 2; i++) {
                if (getClouds().get(i).getUuid().equals(cloud.getUuid())) {
                    getClouds().remove(i);
                    getClouds().add(i, cloud);
                }
            }
        }
        if (getClouds().size() == 2) cloudPrinter.printClouds(getClouds());

    }

    protected void updateIslands(IslandCard island) {
        islandsPrinter.printIslands();
    }

    protected void updateSchoolBoard(String player, SchoolBoard schoolBoard) {
        out.println(player + " board:");
        boardPrinter.printBoard(schoolBoard);
    }

    protected void win() {
        out.println("Well done, you won the game!");
    }

    protected void lose() {
        out.println("Game ended, you lost!");
    }

    //TODO maybe send who tied
    protected void draw() {
        out.println("The game ended in a tie! ");
    }

    /**
     * this method is called when there's a fatal error on the server and the game needs to be closed
     */
    protected void errorAndExit(String error) {
        out.println("\nERROR: " + error);
        out.println("EXIT.");
        System.exit(1);
    }

    protected void error(String error) {
        out.println("");
    }

    @Override
    protected void print() {

    }
}
