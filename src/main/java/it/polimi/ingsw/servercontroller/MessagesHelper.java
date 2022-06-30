package it.polimi.ingsw.servercontroller;

/**
 * Message Helper
 */
public class MessagesHelper {
    private MessagesHelper() {
    }

    public static final String NO_PORT = "Failed to start server: Specify port number with --port <port> command";
    public static final String PORT_NOT_AVAILABLE = "The selected port is already in use. Try again with a different port";
    public static final String SERVER_STARTED = "Server is ready";
    public static final String ERROR_STARTING_SERVER = "Error starting server:";
    public static final String ERROR_CLOSING_ENDPOINT = "Error closing endpoint";
    public static final String ERROR_CREATING_GAME = "Game creation error";
    public static final String ERROR_SAVE_NOT_FOUND = "Previous saved game to restore game state not found";

}
