package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.util.ArrayList;

public class Cli extends View {
    private final PrintStream out;
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);

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

    public void askMotherNatureSteps() {

    }

    public void updateCloud(CloudTile cloud) {

    }

    public void updateIslands(IslandCard island) {

    }

    public void updateSchoolBoard(SchoolBoard schoolBoard) {

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
        out.println();
    }


}
