package it.polimi.ingsw.model;

public class GameParameters {
    private int initialTowersCount;
    private int initialStudentsCount;
    private int studentsToDraw;
    private int studentsToMove;

    public int getInitialTowersCount() {
        return initialTowersCount;
    }

    public int getInitialStudentsCount() {
        return initialStudentsCount;
    }

    public int getStudentsToDraw() {
        return studentsToDraw;
    }

    public int getStudentsToMove() {
        return studentsToMove;
    }

    public void setInitialTowersCount(int initialTowersCount) {
        this.initialTowersCount = initialTowersCount;
    }

    public void setInitialStudentsCount(int initialStudentsCount) {
        this.initialStudentsCount = initialStudentsCount;
    }

    public void setStudentsToDraw(int studentsToDraw) {
        this.studentsToDraw = studentsToDraw;
    }

    public void setStudentsToMove(int studentsToMove) {
        this.studentsToMove = studentsToMove;
    }
}
