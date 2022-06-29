package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.List;

/**
 * Defines a generic view to be implemented by each view type (e.g. CLI, GUI in JavaFX, ...).
 */
public interface UserInterface {

    /**
     * Shows the title of the game
     */
    void printWelcomeMessage();

    /**
     * Shows that the player has been successfully added to the queue
     */
    void printEnqueuedMessage();

    /**
     * Asks the user the ip and the port of the server
     */
    void askServerInfo();

    /**
     * Asks the user to choose a Nickname
     */
    void askPlayerNickname();

    /**
     * Shows to the user if the Login succeeded.
     *
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param playerReconnected    indicates if the nickname chosen was that of a player previously disconnected
     */
    void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected);

    /**
     * Asks the user the GameMode (Normal,Expert) and the number of player to play with (2,3)
     */
    void askGameSettings();

    /**
     * Prints the new gamePhase
     * @param phase the new gamePhase
     */
    void changePhase(GamePhase phase);

    /**
     * Asks the user which assistantCard to play
     * @param deck the list of assistantCard to choose from
     */
    void askAssistantCard(List<AssistantCard> deck);

    /**
     * Asks the user how many steps to move mother nature
     */
    void askMotherNatureSteps();

    /**
     * Asks the user the student from the entrance to move and the destination
     */
    void askStudents();

    /**
     * Asks the user to choose a cloudTile
     */
    void askCloud();

    /**
     * Prints the new current player
     * @param otherPlayer the new current player
     */
    void updateCurrentPlayersTurn(String otherPlayer);

    /**
     * Shows a win message.
     */
    void win();

    /**
     * Shows the lost message and the nicknames of the winners
     * @param winners the winners
     */
    void lose(List<String> winners);

    /**
     * Shows the players who tied at the end of the game
     * @param otherWinner the other winner's nickname other than this user's nickname
     */
    void draw(String otherWinner);

    /**
     * Shows the error that occurred and proceeds to close the game
     * @param error the error
     */
    void errorAndExit(String error);

    /**
     * Shows the error that occurred
     * @param error the error
     */
    void error(String error);

    /**
     * Prints the table
     */
    void print();

    /**
     * Prints a game starting message
     */
    void printGameStarting();

    /**
     * Shows the user that there aren't enough player online to continue playing
     */
    void notEnoughPlayer();

    /**
     * Called when the other/others player/players didn't reconnect in time
     * Shows the user that he is the winner
     */
    void printGameOverFromDisconnection();
}
