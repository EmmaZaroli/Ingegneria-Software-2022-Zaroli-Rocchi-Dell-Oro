package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

public class ExpertGameParameters extends GameParameters {
    private boolean takeProfessorEvenIfSameStudents;
    private int motherNatureExtraMovements;
    private boolean towersCountInInfluence;
    private int extraInfluence;
    private PawnColor colorWithNoInfluence;

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
