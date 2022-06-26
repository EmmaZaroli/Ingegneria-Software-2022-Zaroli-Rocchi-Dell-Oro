package it.polimi.ingsw.model;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;

import java.io.Serial;
import java.io.Serializable;

/**
 * Game parameters.
 */
public class GameParameters implements Serializable {
    @Serial
    private static final long serialVersionUID = 7L;

    private final PlayersNumber playersNumber;
    private final GameMode gameMode;

    private int initialTowersCount;
    private int initialStudentsCount;
    private int studentsToDraw;
    private int studentsToMove;

    /**
     * instantiates a new GameParameters
     * @param playersNumber the number of players
     * @param gameMode the gameMode
     */
    public GameParameters(PlayersNumber playersNumber, GameMode gameMode) {
        this.playersNumber = playersNumber;
        this.gameMode = gameMode;
    }

    /**
     * @return the initial towers number on the school board
     */
    public int getInitialTowersCount() {
        return initialTowersCount;
    }

    /**
     * @return the initial students number
     */
    public int getInitialStudentsCount() {
        return initialStudentsCount;
    }

    /**
     * @return the students to draw from the bag
     */
    public int getStudentsToDraw() {
        return studentsToDraw;
    }

    /**
     * @return the students to move
     */
    public int getStudentsToMove() {
        return studentsToMove;
    }

    /**
     *
     * @return the number of players
     */
    public PlayersNumber getPlayersNumber() {
        return playersNumber;
    }

    /**
     *
     * @return the GameMode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * @param initialTowersCount the initial towers count
     */
    public void setInitialTowersCount(int initialTowersCount) {
        this.initialTowersCount = initialTowersCount;
    }

    /**
     * @param initialStudentsCount the initial students count
     */
    public void setInitialStudentsCount(int initialStudentsCount) {
        this.initialStudentsCount = initialStudentsCount;
    }

    /**
     * @param studentsToDraw the students to draw from the bag
     */
    public void setStudentsToDraw(int studentsToDraw) {
        this.studentsToDraw = studentsToDraw;
    }

    /**
     * @param studentsToMove the students to move
     */
    public void setStudentsToMove(int studentsToMove) {
        this.studentsToMove = studentsToMove;
    }
}
