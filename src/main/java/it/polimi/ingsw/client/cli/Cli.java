package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.InputParser;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Cli extends View {
    private final PrintStream out;
    private final Scanner in;
    private final InputParser inputParser;
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private final PrinterSchoolBoard boardPrinter = new PrinterSchoolBoard();
    private final PrinterClouds cloudPrinter = new PrinterClouds();
    private final PrinterIslands islandsPrinter = new PrinterIslands();

    public Cli() {
        out = System.out;
        in = new Scanner(System.in);
        inputParser = new InputParser();
        //listeners.addPropertyChangeListener("action", network);
    }

    @Override
    public void printWelcomeMessage() {
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
    public void printEnqueuedMessage() {
        System.out.println("You've been added to the players queue.\nPlease, wait for a game to start");
    }

    @Override
    public void printGameStartingMessage() {
        System.out.println("Game starting!");
    }

    @Override
    public void askServerInfo() {
        String ip;
        int port;

        out.print("Please, insert the Server's ip address ");
        ip = readLine();
        out.print("Select the Server's port number ");
        port = Integer.parseInt(readLine());

        this.startConnection(ip, port);
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

    //TODO move validation logic to shared layer
    public void askPlayerNickname() {
        boolean valid = false;
        String nickname;
        do {
            out.print("Enter your nickname: ");
            nickname = readLine();
            valid = inputParser.checkUsername(nickname);
        } while (!valid);
        this.sendPlayerNickname(nickname);
    }

    public void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected) {
        //clearCli();
        if (playerReconnected) {
            out.println("You've being reconnected to your previous game");
        }
        if (nicknameAccepted && !playerReconnected) {
            out.println("Welcome, " + getMe().getNickname() + ", get ready to play!");
        }
        if (!nicknameAccepted && !playerReconnected) {
            out.println("Sorry, your nickname is already taken, please choose another one");
        }
    }

    public void askGameSettings() {
        int playersNumber;
        String gameMode;
        out.print("Please enter the Game mode: [normal/expert] ");
        gameMode = readLine();
        //TODO check if is valid
        out.print("How many players are you going to play with? [2/3] ");
        playersNumber = Integer.parseInt(readLine());
        //TODO check if the number is 2 or 3
        this.sendGameSettings(playersNumber, gameMode.equals("expert"));
    }

    public void genericMessage(String message) {
        out.println(message);
    }

    public void changePhase(GamePhase phase) {
        out.println("Phase: " + phase);
    }

    /**
     * print the Assistant Card of the player and asks him which one to play
     *
     * @param deck
     */
    public void askAssistantCard(ArrayList<AssistantCard> deck) {

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

    public void askMotherNatureSteps() {
        out.println("How many steps do you want to move mother nature? ");
        int steps = Integer.parseInt(readLine());
        //TODO check if number is <0 or >cardplayed.steps
    }

    public void printGameStarting() {
        out.println("The game is starting");
    }

    public void updateCurrentPlayersTurn(String otherPlayer) {
        out.println("it's " + otherPlayer + "turn");
    }

    @Override
    public void print() {
        printCloud();
        printIslands();
        for (int i = 0; i < getOpponents().size(); i++) {
            if (getOpponents().get(i).getNickname().equals(getCurrentPlayer())) {
                printSchoolBoard(getMe());
                if (i + 1 < getOpponents().size()) {
                    printSchoolBoard(getOpponents().get(i + 1));
                }
            }
            printSchoolBoard(getOpponents().get(i));
        }
    }

    private void printCoins() {
        //TODO
    }

    private void printCharacterCards() {
        //TODO
    }

    private void printCloud() {
        cloudPrinter.printClouds(getClouds());
    }

    private void printIslands() {
        islandsPrinter.printIslands(getIslands());
    }

    private void printSchoolBoard(PlayerInfo player) {
        out.println(player.getNickname() + " board:");
        //TODO player dto
        boardPrinter.printBoard(player.getBoard());
        printAssistantCardPlayed(player.getDiscardPileHead());
    }

    private void printAssistantCardPlayed(AssistantCard card) {
        out.println("   _____     ");
        out.println("  |" + card.value() + "   " + card.motherNatureMovement() + "|    ");
        out.println("  |     |    ");
        out.println("  |_____|    ");
    }

    public void win() {
        out.println("Well done, you won the game!");
    }

    public void lose() {
        out.println("Game ended, you lost!");
    }

    //TODO maybe send who tied
    public void draw() {
        out.println("The game ended in a tie! ");
    }

    /**
     * this method is called when there's a fatal error on the server and the game needs to be closed
     */
    public void errorAndExit(String error) {
        out.println("\nERROR: " + error);
        out.println("EXIT.");
        System.exit(1);
    }

    public void error(String error) {
        out.println("");
    }
}
