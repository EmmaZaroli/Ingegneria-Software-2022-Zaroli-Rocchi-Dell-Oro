package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

public class GameParameters {
    private int initialTowersCount;
    private int initialStudentsCount;
    private int studentsToDraw;
    private int studentsToMove;

    private boolean takeProfessorEvenIfSameStudents;
    private int motherNatureExtraMovements;
    private boolean towersCountInInfluence;
    private int extraInfluence;
    private PawnColor colorWithNoInfluence;

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

    public boolean isTakeProfessorEvenIfSameStudents() {
        return takeProfessorEvenIfSameStudents;
    }

    public int getMotherNatureExtraMovements() {
        return motherNatureExtraMovements;
    }

    public boolean isTowersCountInInfluence() {
        return towersCountInInfluence;
    }

    public int getExtraInfluence() {
        return extraInfluence;
    }

    public PawnColor getColorWithNoInfluence() {
        return colorWithNoInfluence;
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

    public void setTakeProfessorEvenIfSameStudents(boolean takeProfessorEvenIfSameStudents) {
        this.takeProfessorEvenIfSameStudents = takeProfessorEvenIfSameStudents;
    }

    public void setMotherNatureExtraMovements(int motherNatureExtraMovements) {
        this.motherNatureExtraMovements = motherNatureExtraMovements;
    }

    public void setTowersCountInInfluence(boolean towersCountInInfluence) {
        this.towersCountInInfluence = towersCountInInfluence;
    }

    public void setExtraInfluence(int extraInfluence) {
        this.extraInfluence = extraInfluence;
    }

    public void setColorWithNoInfluence(PawnColor colorWithNoInfluence) {
        this.colorWithNoInfluence = colorWithNoInfluence;
    }
}
