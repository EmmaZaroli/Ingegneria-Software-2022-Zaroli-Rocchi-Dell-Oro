package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.InputParser;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CLI
 */
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
    private final PrinterCharacterCards characterCardPrinter = new PrinterCharacterCards();
    private final PrinterAssistantCards assistantCardsPrinter = new PrinterAssistantCards();
    private final CliParser cliParser = new CliParser();

    /**
     * Instantiates a new cli
     */
    public Cli() {
        out = System.out;
        in = new Scanner(System.in);
        inputParser = new InputParser();
    }

    /**
     * prints game title
     */
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

        out.println(" ");
        space(45);
        out.println("Welcome to Eriantys Board Game!");
    }

    @Override
    public void printEnqueuedMessage() {
        System.out.println("You've been added to the players queue.\nPlease, wait for a game to start");
    }

    //@Override
    public void printGameStartingMessage() {
        System.out.println("Game starting!");
    }

    /**
     * asks the ip and the port of the server
     */
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
        ip = "10.169.157.155";
        port = 24000;

        this.startConnection(ip, port);
    }

    /**
     * Reads a line from the standard input.
     *
     * @return the string read from the input.
     */
    private String readLine() {
        String input = "";
        boolean helpRequest = false;
        while (!helpRequest) {
            input = in.nextLine();
            if (input.equals("-h") && isExpertGame()) {
                helpMessage();
            } else helpRequest = true;
        }
        return input;
    }


    /**
     * Asks the player a nickname
     */
    public void askPlayerNickname() {
        boolean valid;
        String nickname;
        do {
            out.print("Enter your nickname: ");
            nickname = readLine();
            valid = inputParser.checkUsername(nickname);
        } while (!valid);
        this.sendPlayerNickname(nickname);
    }

    /**
     * @param nicknameAccepted  is true if the server accepted the nickname
     * @param playerReconnected if the nickname was of a player previously disconnected
     */
    public void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected) {
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

    /**
     * Asks the player the gameMode and the number of players to play with
     */
    public void askGameSettings() {
        int playersNumber = 0;
        String gameMode = null;
        boolean valid = false;
        while (!valid) {
            out.print("Please enter the Game mode: [normal/expert] ");
            gameMode = readLine();
            valid = (gameMode.equals("normal") || gameMode.equals("expert"));
        }
        valid = false;
        while (!valid) {
            out.print("How many players are you going to play with? [2/3] ");
            try {
                playersNumber = Integer.parseInt(readLine());
                valid = (playersNumber == 2 || playersNumber == 3);
            } catch (NumberFormatException e) {
                error("not a number");
            }
        }
        this.sendGameSettings(playersNumber == 2 ? PlayersNumber.TWO : PlayersNumber.THREE,
                gameMode.equals("expert") ? GameMode.EXPERT_MODE : GameMode.NORMAL_MODE);
    }

    public void genericMessage(String message) {
        out.println(message);
    }

    /**
     * Used in the expert game mode, prints the effects of each character card
     */
    public void helpMessage() {
        out.println("character_1: Take 1 student from this card and place it on an island of your choice. ");
        out.println("character_2: During this turn, you take control of any number of professors,even if you have the same number of students as the player who currently controls them.");
        out.println("character_4: You may move Mother Nature up to 2 additional steps.");
        out.println("character_6: When resolving a Conquering on an Island, towers don't count towards influence.");
        out.println("character_7: You may take up to 3 students from this card and replace them with the same number of students from you entrance.");
        out.println("character_8: During the influence calculation this turn, you count as having 2 more influence.");
        out.println("character_9: Choose a color of students, during the influence calculation this turn, that color adds no influence.");
        out.println("character_11: Take one student from this card and place it in your dining room.");
        out.print("Make your move:");
    }

    /**
     * Prints the new game phase
     *
     * @param phase the new GamePhase
     */
    public void changePhase(GamePhase phase) {
        space(57);
        out.println(ANSI_YELLOW + "Phase: " + phase + ANSI_RESET);
    }

    /**
     * print the Assistant Card of the player and asks him which one to play
     *
     * @param deck the list of Assistant cards
     */
    public void askAssistantCard(List<AssistantCard> deck) {

        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("     " + (i + 1) + ".      ");
        }
        out.println();
        for (int i = 0; i < deck.size(); i++) {
            out.print("   _____     ");
        }
        out.println();
        for (AssistantCard assistantCard : deck) {
            if (assistantCard.value() != 10)
                out.print("  |" + assistantCard.value() + "   " + assistantCard.motherNatureMovement() + "|    ");
            else out.print("  |" + assistantCard.value() + "  " + assistantCard.motherNatureMovement() + "|    ");
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
            out.print("chose the assistant card to play: ");
            String input = readLine();
            try {
                card = Integer.parseInt(input);
                valid = this.sendAssistantCard(card - 1);
            } catch (NumberFormatException e) {
                valid = false;
            }
            if (!valid) error("Error, the card you selected is not valid!");
        }
    }

    /**
     * Asks the number of mother nature steps
     * In the expert mode is given the chance to choose a character card to play
     */
    public void askMotherNatureSteps() {
        boolean valid = false;
        while (!valid) {
            out.print("Mother Nature steps");
            if (isExpertGame() && !areCharacterActive()) out.print(" or choose character card to activate");
            out.print(": ");

            String input = readLine();
            Optional<Integer> characterCard = cliParser.indexCharacterCard(input, getCharacterCards());
            if (characterCard.isEmpty()) {
                try {
                    int steps = Integer.parseInt(input);
                    valid = this.sendMotherNatureSteps(steps);
                } catch (NumberFormatException e) {
                    valid = false;
                }
            } else {
                if (canActivateCharacter(characterCard.get()))
                    valid = askCharacterCardParameters(characterCard.get());
            }
            if (!valid && isExpertGame()) error("invalid number of steps or invalid character card");
            else if (!valid) error("invalid number of steps");
        }
    }

    /**
     * Asks the index of the cloud
     * In the expert mode is given the chance to choose a character card to play
     */
    public void askCloud() {
        boolean valid = false;
        while (!valid) {
            out.print("Choose Cloud");
            if (isExpertGame() && !areCharacterActive()) out.print(" or choose character card to activate");
            out.print(": ");
            String input = readLine();
            Optional<Integer> characterCard = cliParser.indexCharacterCard(input, getCharacterCards());
            if (characterCard.isEmpty()) {
                try {
                    int indexCloud = Integer.parseInt(input);
                    valid = this.sendCloudChoice(indexCloud);
                } catch (NumberFormatException e) {
                    valid = false;
                }
            } else {
                if (canActivateCharacter(characterCard.get()))
                    valid = askCharacterCardParameters(characterCard.get());
            }
            if (!valid && isExpertGame()) error("invalid cloud or invalid character card");
            else if (!valid) error("invalid cloud");
        }

    }


    /**
     * Asks the student to move and the destination
     * In the expert mode is given the chance to choose a character card to play
     */
    public void askStudents() {

        boolean validObject = false;
        boolean validDestination;
        int destination = 0;
        while (!validObject) {
            out.print("Choose student to move");
            if (isExpertGame() && !areCharacterActive()) out.print(" or choose character card to activate");
            out.print(": ");
            String input = readLine();
            PawnColor student = cliParser.checkIfStudent(input);
            if (student != PawnColor.NONE && getMe().getBoard().getEntrance().contains(student)) {
                validDestination = false;
                while (!validDestination) {
                    out.print("Choose location (island's index/schoolboard) : ");
                    destination = CliParser.isIslandOrSchoolBoard(readLine(), getNumberOfIslandOnTable());
                    if (destination != 13) {
                        validObject = true;
                        validDestination = true;
                    } else {
                        this.error("the destination selected is invalid, please retry");
                    }
                }
                if (destination == 12) validObject = sendStudentMoveOnBoard(student);
                else validObject = sendStudentMoveOnIsland(student, destination);
            }
            // the input was not a color, checking if it's a character card
            else {
                Optional<Integer> characterCard = cliParser.indexCharacterCard(input, getCharacterCards());
                if (characterCard.isPresent() && canActivateCharacter(characterCard.get())) {
                    validObject = askCharacterCardParameters(characterCard.get());
                }
            }
            if (!validObject && isExpertGame()) error("invalid student or invalid character card");
            else if (!validObject && !isExpertGame()) error("invalid student or move");
        }
    }


    /**
     * Called at the beginning of the game
     */
    public void printGameStarting() {
        out.println("The game is starting!");
    }

    /**
     * @param otherPlayer the new current player
     */
    public void updateCurrentPlayersTurn(String otherPlayer) {
        out.println("It's " + otherPlayer.toUpperCase() + "'s turn");
    }

    /**
     * Prints the table
     */
    @Override
    public void print() {
        clearCli();
        changePhase(getCurrentPhase());
        printCloud();
        printIslands();
        if (isExpertGame()) {
            printTableCoins();
            printCharacterCards();
        }
        printSchoolBoard();
        updateCurrentPlayersTurn(getCurrentPlayer());
    }

    /**
     * Prints the players' coins
     */
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

    /**
     * Prints the character cards on the table
     */
    private void printCharacterCards() {
        characterCardPrinter.print(getCharacterCards());
    }

    /**
     * Prints the coins on the table
     */
    private void printTableCoins() {
        space(56);
        out.println("COINS ON TABLE: " + getCoins() + " " + COIN);
    }

    /**
     * Prints the clouds
     */
    private void printCloud() {
        cloudPrinter.printClouds(getClouds());
    }

    /**
     * Prints the islands
     */
    private void printIslands() {
        islandsPrinter.printIslands(getIslands());
    }

    /**
     * Prints the players' schoolBoards
     */
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


    /**
     * Prints the players' assistant cards
     */
    private void printAssistantCardPlayed() {
        List<PlayerInfo> players = new ArrayList<>();
        players.add(getMe());
        players.addAll(getOpponents());
        assistantCardsPrinter.print(players);
        out.println();
    }


    /**
     * @param index the index of the chosen character card
     * @return true if all the parameters are correct
     */
    private boolean askCharacterCardParameters(int index) {
        Character character = getCharacterCards().get(index).getCharacter();
        switch (character) {
            case CHARACTER_ONE -> {
                return askCharacterOneParameters(index);
            }
            case CHARACTER_SEVEN -> {
                return askCharacterSevenParameters(index);
            }
            case CHARACTER_NINE -> {
                return askCharacterNineParameters(index);
            }
            case CHARACTER_ELEVEN -> {
                return askCharacterElevenParameters(index);
            }
            default -> {
                return sendCharacterCard(index, null);
            }
        }
    }

    /**
     * Asks the parameters for the character one
     *
     * @param index the index of the chosen character card
     * @return true if all the parameters are correct
     */
    private boolean askCharacterOneParameters(int index) {
        boolean validSend = false;
        Object[] parameters = new Object[2];
        while (!validSend) {
            boolean valid = false;
            while (!valid) {
                System.out.println("Choose student from card 1 to move: ");
                PawnColor student = cliParser.checkIfStudent(readLine());
                if (student != PawnColor.NONE) {
                    parameters[0] = student;
                    valid = true;
                } else error("invalid student selected!");
            }
            valid = false;
            while (!valid) {
                System.out.println("Choose Island index: ");
                int islandIndex = CliParser.isIslandOrSchoolBoard(readLine(), getNumberOfIslandOnTable());
                if (islandIndex != 13 && islandIndex != 12) {
                    parameters[1] = getIslands().get((islandIndex - 1) % 12).getIsland().getUuid();
                    valid = true;
                }
                if (!valid) error("invalid Island index selected!");
            }
            validSend = sendCharacterCard(index, parameters);
            if (!validSend) error("Some parameters are incorrect!");
        }
        return true;
    }

    /**
     * Asks the parameters for the character seven
     *
     * @param index the index of the chosen character card
     * @return true if all the parameters are correct
     */
    private boolean askCharacterSevenParameters(int index) {
        boolean valid = false;
        Object[] parameters = new Object[2];
        List<PawnColor> studentsFromCard = new ArrayList<>();
        List<PawnColor> studentsFromEntrance;
        do {
            while (!valid) {
                System.out.println("Choose student from card 7 to move [max 3, separated by commas]: ");
                studentsFromCard = Arrays.stream(readLine().split(",")).map(cliParser::checkIfStudent).collect(Collectors.toList());
                int size = (int) studentsFromCard.stream().filter(s -> s != PawnColor.NONE).count();
                if (studentsFromCard.size() == size) {
                    parameters[0] = studentsFromCard;
                    valid = true;
                } else error("invalid students selected!");
            }
            valid = false;
            while (!valid) {
                System.out.println("Choose student from entrance [max 3, separated by commas]: ");
                studentsFromEntrance = Arrays.stream(readLine().split(",")).map(cliParser::checkIfStudent).collect(Collectors.toList());
                int size = (int) studentsFromEntrance.stream().filter(s -> s != PawnColor.NONE).count();
                if (studentsFromEntrance.size() == size && studentsFromEntrance.size() == studentsFromCard.size()) {
                    parameters[1] = studentsFromEntrance;
                    valid = true;
                } else error("invalid students selected!");
            }
        } while (!sendCharacterCard(index, parameters));
        return true;
    }

    /**
     * Asks the parameters for the character nine
     *
     * @param index the index of the chosen character card
     * @return true if all the parameters are correct
     */
    private boolean askCharacterNineParameters(int index) {
        Object[] parameters = new Object[1];
        boolean valid = false;
        while (!valid) {
            System.out.println("Choose student's color: ");
            PawnColor student = cliParser.checkIfStudent(readLine());
            if (student != PawnColor.NONE) {
                parameters[0] = student;
                valid = sendCharacterCard(index, parameters);
            }
            if (!valid) error("invalid student selected!");
        }
        return true;
    }

    /**
     * Asks the parameters for the character eleven
     *
     * @param index the index of the chosen character card
     * @return true if all the parameters are correct
     */
    private boolean askCharacterElevenParameters(int index) {
        boolean valid = false;
        Object[] parameters = new Object[1];
        while (!valid) {
            System.out.println("Choose student from card 11 to move in your schoolboard: ");
            PawnColor student = cliParser.checkIfStudent(readLine());
            if (student != PawnColor.NONE) {
                parameters[0] = student;
                valid = sendCharacterCard(index, parameters);
            }
            if (!valid) error("invalid student selected!");
        }
        return true;
    }


    /**
     * Called when the game is over and the player is the winner
     */
    public void win() {
        out.println("Well done, you won the game!");
        pressAnyKeyToContinue();
    }

    /**
     * Called when the game is over and the player is the loser
     */
    public void lose(List<String> winners) {
        out.println("Game ended, you lost!");
        pressAnyKeyToContinue();
    }

    /**
     * Called when the game is over and had ended in a tie
     *
     * @param otherWinner the nickname of the other winner
     */
    public void draw(String otherWinner) {
        out.println("The game ended in a tie! ");
        out.println("The winners are you and " + otherWinner);
        pressAnyKeyToContinue();
    }

    /**
     * this method is called when there's a fatal error on the server and the game needs to be closed
     */
    public void errorAndExit(String error) {
        error(error);
        exit();
    }

    /**
     * Close the game
     */
    public void exit() {
        out.println("Press ANY KEY for EXIT.");
        readLine();
        System.exit(1);
    }

    /**
     * @param error the error
     */
    public void error(String error) {
        out.println("\nERROR: " + ANSI_RED + error.toUpperCase() + ANSI_RESET);
    }

    /**
     * @param space the number of spaces to leave on the left
     */
    private void space(int space) {
        for (int i = 0; i < space; i++) out.print(" ");
    }

    /**
     * Clears the cli
     */
    private void clearCli() {
        for (int i = 0; i < 20; i++) out.println();
    }

    /**
     * Game over from disconnection
     */
    @Override
    public void printGameOverFromDisconnection() {
        out.println("TIMER OUT");
        out.println("it seems that you're the only player left...");
        out.println("congratulation, you won!");
        pressAnyKeyToContinue();
    }

    /**
     * There aren't enough player online to continue playing
     */
    public void notEnoughPlayer() {
        out.println("your opponent has disconnected, please wait...");
    }

    /**
     * Press any key to continue
     */
    private void pressAnyKeyToContinue() {
        out.println("Press any key to continue");
        readLine();
    }
}
