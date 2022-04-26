package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

/**
 * Expert game parameters,set when a character card is activated
 */
public class ExpertGameParameters extends GameParameters {
    private boolean takeProfessorEvenIfSameStudents;
    private int motherNatureExtraMovements;
    private boolean towersCountInInfluence;
    private int extraInfluence;
    private PawnColor colorWithNoInfluence;
    private boolean alreadyActivateCharacterCard;

    /**
     * @return true if the player can take control of the professors even
     * if he has the same number of students in his dining room as the player
     * who currently controls them, false otherwise
     */
    public boolean isTakeProfessorEvenIfSameStudents() {
        return takeProfessorEvenIfSameStudents;
    }

    /**
     * @return mother nature extra movements
     */
    public int getMotherNatureExtraMovements() {
        return motherNatureExtraMovements;
    }

    /**
     * @return true if, when counting the influence on an island, the towers
     * present are not calculated
     */
    public boolean isTowersCountInInfluence() {
        return towersCountInInfluence;
    }

    /**
     * @return the extra influence of a player on the island
     */
    public int getExtraInfluence() {
        return extraInfluence;
    }

    /**
     * @return the student's color with no influence
     */
    public PawnColor getColorWithNoInfluence() {
        return colorWithNoInfluence;
    }

    public boolean hasAlreadyActivateCharacterCard() {
        return alreadyActivateCharacterCard;
    }

    /**
     * Sets take professor even if same students.
     *
     * @param takeProfessorEvenIfSameStudents The value to set
     */
    public void setTakeProfessorEvenIfSameStudents(boolean takeProfessorEvenIfSameStudents) {
        this.takeProfessorEvenIfSameStudents = takeProfessorEvenIfSameStudents;
    }

    /**
     * Sets mother nature extra movements.
     *
     * @param motherNatureExtraMovements the mother nature extra movements
     */
    public void setMotherNatureExtraMovements(int motherNatureExtraMovements) {
        this.motherNatureExtraMovements = motherNatureExtraMovements;
    }

    /**
     * Sets the variable towers count in influence.
     *
     * @param towersCountInInfluence If true, towers are treated ad students while counting influence
     */
    public void setTowersCountInInfluence(boolean towersCountInInfluence) {
        this.towersCountInInfluence = towersCountInInfluence;
    }

    /**
     * Sets extra influence on an island
     *
     * @param extraInfluence the extra influence
     */
    public void setExtraInfluence(int extraInfluence) {
        this.extraInfluence = extraInfluence;
    }

    /**
     * Sets color with no influence.
     *
     * @param colorWithNoInfluence the color with no influence
     */
    public void setColorWithNoInfluence(PawnColor colorWithNoInfluence) {
        this.colorWithNoInfluence = colorWithNoInfluence;
    }

    public void setAlreadyActivateCharacterCard(boolean alreadyActivateCharacterCard) {
        this.alreadyActivateCharacterCard = alreadyActivateCharacterCard;
    }
}
