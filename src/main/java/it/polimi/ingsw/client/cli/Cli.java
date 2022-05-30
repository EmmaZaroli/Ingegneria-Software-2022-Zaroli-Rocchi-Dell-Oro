package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.InputParser;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.PrintStream;
import java.util.*;

public class Cli extends View {
    private final PrintStream out;
    private final Scanner in;
    private final InputParser inputParser;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String COIN = "¢";
    public static final String ANSI_YELLOW = "\u001B[33m";
    private final PrinterSchoolBoard boardPrinter = new PrinterSchoolBoard();
    private final PrinterClouds cloudPrinter = new PrinterClouds();
    private final PrinterIslands islandsPrinter = new PrinterIslands();
    private final PrinterCharacterCards PrinterCharacterCard = new PrinterCharacterCards();
    private final PrinterAssistantCards printerAssistantCards = new PrinterAssistantCards();

    public Cli() {
        out = System.out;
        in = new Scanner(System.in);
        inputParser = new InputParser();
    }

    @Override
    public void printWelcomeMessage() {
        out.println("                       ▄████████    ▄████████  ▄█     ▄████████ ███▄▄▄▄       ███     ▄██   ▄      ▄████████ \n" +
                "                      ███    ███   ███    ███ ███    ███    ███ ███▀▀▀██▄ ▀█████████▄ ███   ██▄   ███    ███ \n" +
                "                      ███    █▀    ███    ███ ███▌   ███    ███ ███   ███    ▀███▀▀██ ███▄▄▄███   ███    █▀  \n" +
                "                     ▄███▄▄▄      ▄███▄▄▄▄██▀ ███▌   ███    ███ ███   ███     ███   ▀ ▀▀▀▀▀▀███   ███        \n" +
                "                    ▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ███▌ ▀███████████ ███   ███     ███     ▄██   ███ ▀███████████ \n" +
                "                      ███    █▄  ▀███████████ ███    ███    ███ ███   ███     ███     ███   ███          ███ \n" +
                "                      ███    ███   ███    ███ ███    ███    ███ ███   ███     ███     ███   ███    ▄█    ███ \n" +
                "                      ██████████   ███    ███ █▀     ███    █▀   ▀█   █▀     ▄████▀    ▀█████▀   ▄████████▀  \n" +
                "                                   ███    ███                                                                ");

        out.println("");
        space(45);
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

        /*
        out.print("Please, insert the Server's ip address: ");
        ip = readLine();
        out.print("Select the Server's port number: ");
        port = Integer.parseInt(readLine());

         */
        ip = "127.0.0.1";
        port = 24000;

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
            out.println("You've been reconnected to your previous game");
        }
        if (nicknameAccepted && !playerReconnected) {
            out.println("Welcome, " + getMe().getNickname() + ", get ready to play!");
        }
        if (!nicknameAccepted && !playerReconnected) {
            out.println("Sorry, your nickname is already taken, please choose another one ");
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
        this.sendGameSettings(playersNumber == 2 ? PlayersNumber.TWO : PlayersNumber.THREE,
                gameMode.equals("expert") ? GameMode.EXPERT_MODE : GameMode.NORMAL_MODE);
    }

    public void genericMessage(String message) {
        out.println(message);
    }

    public void changePhase(GamePhase phase) {
        space(57);
        out.println(ANSI_YELLOW + "Phase: " + phase + ANSI_RESET);
    }

    /**
     * print the Assistant Card of the player and asks him which one to play
     *
     * @param deck
     */
    public void askAssistantCard(List<AssistantCard> deck) {

        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("     " + (i+1) + ".      ");
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
        boolean valid = false;
        while (!valid) {
            out.print("chose the assistant card to play");
            if (isExpertGame()) out.print(" or choose character card to activate");
            out.print(": ");
            card = Integer.parseInt(readLine());
            if (this.sendAssistantCard(card-1)) {
                valid = true;
            } else error("Error, the card you selected is not valid!");
        }
    }

    public void askMotherNatureSteps() {
        boolean valid = false;
        while(!valid) {
            out.print("Mother Nature steps");
            if (isExpertGame()) out.print(" or choose character card to activate");
            out.print(": ");
            int steps = Integer.parseInt(readLine());
            if(this.sendMotherNatureSteps(steps)) valid = true;
            else error("invalid number of steps");
        }
    }

    public void askCloud() {
        out.print("Choose Cloud");
        if (isExpertGame()) out.print(" or choose character card to activate");
        out.print(": ");
        int indexCloud = Integer.parseInt(readLine());
        this.sendCloudChoice(indexCloud);
    }

    public void askStudents(){
        CliParsen parsenStudents = new CliParsen();
        boolean validObject = false;
        boolean validDestination;
        int destination = 0;
        while (!validObject) {
            out.print("Choose student to move");
            if (isExpertGame()) out.print(" or choose character card to activate");
            out.print(": ");
            String input = readLine();
            PawnColor student = parsenStudents.checkIfStudent(input);
            if (student != PawnColor.NONE && getMe().getBoard().getEntrance().contains(student)) {
                validDestination = false;
                while (!validDestination) {
                    out.print("Choose location (island's index/schoolboard) : ");

                    destination = parsenStudents.isIslandOrSchoolBoard(readLine(), getNumberOfIslandOnTable());

                    if (destination != 13) {
                        validObject=true;
                        validDestination = true;
                    } else {
                        this.error("the destination selected is invalid, please retry");
                    }
                }
                if(destination == 12) sendStudentMoveOnBoard(student);
                else sendStudentMoveOnIsland(student,destination);
            }
            // the input was not a color, checking if it's a character card
            else if (isExpertGame()) {
                //TODO
                if (isACard(input)) ;
            } else error("invalid student selected");
        }
    }


    private boolean isACard(String input) {
        //TODO
        return false;
    }

    public void printGameStarting() {
        out.println("The game is starting!");
    }

    public void updateCurrentPlayersTurn(String otherPlayer) {
        out.println("It's " + otherPlayer.toUpperCase() + "'s turn");
    }

    @Override
    public void print() {
        clearCli();
        changePhase(getCurrentPhase());
        printCloud();
        printIslands();
        if (isExpertGame()) printCharacterCards();
        printSchoolBoard();
        updateCurrentPlayersTurn(getCurrentPlayer());
    }

    public void printCoins() {
        //me
        for (int i = 0; i < getMe().getCoins(); i++) {
            out.print(ANSI_YELLOW + COIN + ANSI_RESET);
        }
        space(51 - getMe().getCoins());
        //opponent n°1
        for (int i = 0; i < getOpponents().get(0).getCoins(); i++) {
            out.print(ANSI_YELLOW + COIN + ANSI_RESET);
        }
        space(51 - getOpponents().get(0).getCoins());
        //opponent n°2
        if (getOpponents().size() == 2) {
            for (int i = 0; i < getOpponents().get(1).getCoins(); i++) {
                out.print(ANSI_YELLOW + COIN + ANSI_RESET);
            }
        }
        out.println();
    }

    private void printCharacterCards() {
        PrinterCharacterCard.print(getCharacterCards());
    }

    private void printCloud() {
        cloudPrinter.printClouds(getClouds());
    }

    private void printIslands() {
        islandsPrinter.printIslands(getIslands());
    }

    public void printSchoolBoard() {
        out.print(getMe().getNickname() + "'s" + " board:");
        space(39);
        for (PlayerInfo opponent : getOpponents()) {
            out.print(opponent.getNickname() + "'s" + " board:");
            space(39);
        }
        out.println();
        if (isExpertGame()) printCoins();
        List<SchoolBoardDto> boards = new ArrayList<>();
        boards.add(getMe().getBoard());
        for (PlayerInfo opponent : getOpponents()) boards.add(opponent.getBoard());
        boardPrinter.printBoard(boards);
        printAssistantCardPlayed();
    }


    private void printAssistantCardPlayed() {
        List<PlayerInfo> players = new ArrayList();
        players.add(getMe());
        for(PlayerInfo opponent : getOpponents())
            players.add(opponent);
        printerAssistantCards.print(players);
        out.println();
    }

    public void win() {
        out.println("Well done, you won the game!");
    }

    public void lose(List<String> winners) {
        out.println("Game ended, you lost!");
    }

    //TODO maybe send who tied
    public void draw(String otherWinner) {
        out.println("The game ended in a tie! ");
    }

    /**
     * this method is called when there's a fatal error on the server and the game needs to be closed
     */
    public void errorAndExit(String error) {
        error(error);
        out.println("EXIT.");
        System.exit(1);
    }

    public void error(String error) {
        out.println("\nERROR: " + ANSI_RED + error + ANSI_RESET);
    }

    private void space(int space) {
        for (int i = 0; i < space; i++) out.print(" ");
    }

    private void clearCli() {
        for (int i = 0; i < 20; i++) out.println();
    }

    @Override
    public void gameOverFromDisconnection() {

    }

    public void notEnoughPlayer() {

    }
}
