package it.polimi.ingsw.persistency;

import it.polimi.ingsw.controller.GameController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton class to write the state of a game onto persistent memory
 */
public class DataDumper {
    private static final DataDumper instance = new DataDumper();

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String SERIALIZED_FILE_FORMAT = ".gc10";

    /**
     * Returns the instance for this context
     * @return The Data dumper
     */
    public static DataDumper getInstance() {
        return instance;
    }

    /**
     * Serializes the Game controller and writes it on a file
     * @param game The game to serialize
     */
    public void serializeAndWrite(GameController game) {
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(game.getGameId() + SERIALIZED_FILE_FORMAT)) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(game);
                objectOutputStream.close();
            }
        } catch(IOException e) {
            //In case of error there's not much to do...
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Reads and deserializes a Game controller from a file with the specified name
     * @param sourceFile the name of the file containing the serialized game
     * @return The deserialized game
     */
    public GameController readAndDeserialize(String sourceFile) {
        ObjectInputStream objectInputStream;
        GameController gameController = null;
        try {
            try (FileInputStream fileInputStream = new FileInputStream(sourceFile)) {
                objectInputStream = new ObjectInputStream(fileInputStream);
                gameController = (GameController) objectInputStream.readObject();
                objectInputStream.close();
            }
        }  catch(IOException | ClassNotFoundException e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return gameController;
    }

    /**
     * Removes a game from the list of saved games
     * @param gameId The ID of the game to remove
     */
    public void removeGameFromMemory(UUID gameId) {
        Path filePath = Paths.get(gameId + SERIALIZED_FILE_FORMAT);
        try {
            Files.delete(filePath);
        }  catch(IOException e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
