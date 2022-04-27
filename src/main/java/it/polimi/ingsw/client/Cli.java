package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Cli extends View {
    private final PrintStream out;
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

    public Cli(Network network) {
        out = System.out;
        listeners.addPropertyChangeListener("action", network);
    }


    //TODO do we need to ask for the port?


    public void init() {
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


    /**
     * Reads a line from the standard input.
     *
     * @return the string read from the input.
     */
    public String readLine() {
        //TODO read from input
        return "";
    }

    //TODO maybe its better if we open a scan and set a boolean is your turn
    public void askPlayerNickname() {
        String nickname;
        out.println("Enter your nickname: ");
        //TODO exception if nickname = null
        nickname = readLine();
        setNickname(nickname);
        listeners.firePropertyChange("nickname", true, nickname);
    }

    public void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected) {

        //clearCli();
        if (playerReconnected) {
            out.println("You're being reconnected to your previous game");
        }
        if (nicknameAccepted && !playerReconnected) {
            out.println("Welcome, " + getNickname() + ", get ready to play!");
        }
        if (!nicknameAccepted && !playerReconnected) {
            out.println("Sorry, your nickname is already taken, please choose another one");
            askPlayerNickname();
        }

    }

    public void askGameSettings() {
        int playersNumber;
        String gameMode;
        out.println("Please enter the Game mode: [normal/expert]");
        gameMode = readLine();
        //TODO check if is valid
        out.println("How many players are you going to play with? [2/3] ");
        playersNumber = Integer.parseInt(readLine());
        //TODO check if the number is 2 or 3
        setNumberOfPlayer(playersNumber);
        listeners.firePropertyChange("gameSettings", gameMode, playersNumber);
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
        listeners.firePropertyChange("assistantCard", true, deck.get(card));
    }

    public void updateAssistantCardPlayed(AssistantCard card, String player) {
        if (!player.equals(getNickname())) {
            out.println(player + " has played ");
        } else out.println("you have played ");
        out.println("   _____     ");
        out.println("  |" + card.value() + "   " + card.motherNatureMovement() + "|    ");
        out.println("  |     |    ");
        out.println("  |_____|    ");


    }

    public void askMotherNatureSteps() {

    }

    public void updateCurrentPlayer(String otherPlayer) {
        out.println("it's " + otherPlayer + "turn");
    }

    public void updateCloud(CloudTile cloud) {

    }

    public void updateIslands(IslandCard island) {

    }

    public void updateSchoolBoard(String player, SchoolBoard schoolBoard) {
        out.println(player + " board:");
        boardPrinter.printBoard(schoolBoard);
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

    public void error() {
        out.println("");
    }


}
