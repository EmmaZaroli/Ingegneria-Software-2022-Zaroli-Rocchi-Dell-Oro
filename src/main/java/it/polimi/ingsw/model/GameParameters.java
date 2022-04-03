package it.polimi.ingsw.model;

/**
 * Game parameters.
 */
public class GameParameters {
    private int initialTowersCount;
    private int initialStudentsCount;
    private int studentsToDraw;
    private int studentsToMove;

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
