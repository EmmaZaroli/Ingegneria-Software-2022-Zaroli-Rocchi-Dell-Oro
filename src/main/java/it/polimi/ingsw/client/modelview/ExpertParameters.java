package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.dtos.ExpertParametersDto;
import it.polimi.ingsw.model.enums.PawnColor;

/**
 * Expert Parameters
 */
public class ExpertParameters {
    private boolean takeProfessorEvenIfSameStudents;
    private int motherNatureExtraMovements;
    private boolean towersCountInInfluence;
    private int extraInfluence;
    private PawnColor colorWithNoInfluence;
    private boolean alreadyActivateCharacterCard;

    public ExpertParameters(){
        this.takeProfessorEvenIfSameStudents = false;
        this.motherNatureExtraMovements = 0;
        this.towersCountInInfluence = true;
        this.extraInfluence = 0;
        this.colorWithNoInfluence = PawnColor.NONE;
        this.alreadyActivateCharacterCard = false;
    }


    public ExpertParameters(ExpertParametersDto origin) {
        this.takeProfessorEvenIfSameStudents = origin.isTakeProfessorEvenIfSameStudents();
        this.motherNatureExtraMovements = origin.getMotherNatureExtraMovements();
        this.towersCountInInfluence = origin.isTowersCountInInfluence();
        this.extraInfluence = origin.getExtraInfluence();
        this.colorWithNoInfluence = origin.getColorWithNoInfluence();
        this.alreadyActivateCharacterCard = origin.hasAlreadyActivateCharacterCard();
    }

    public ExpertParameters(boolean takeProfessorEvenIfSameStudents, int motherNatureExtraMovements, boolean towersCountInInfluence, int extraInfluence, PawnColor colorWithNoInfluence, boolean alreadyActivateCharacterCard) {
        this.takeProfessorEvenIfSameStudents = takeProfessorEvenIfSameStudents;
        this.motherNatureExtraMovements = motherNatureExtraMovements;
        this.towersCountInInfluence = towersCountInInfluence;
        this.extraInfluence = extraInfluence;
        this.colorWithNoInfluence = colorWithNoInfluence;
        this.alreadyActivateCharacterCard = alreadyActivateCharacterCard;
    }



    /**
     *
     * @return the number of mother nature extra steps
     */
    public int getMotherNatureExtraMovements() {
        return motherNatureExtraMovements;
    }

    /**
     *
     * @return true if there's already an active card
     */
    public boolean isAlreadyActivateCharacterCard() {
        return alreadyActivateCharacterCard;
    }

    /**
     *
     * @return a clone of this instance
     */
    private ExpertParameters deepClone(){
        return new ExpertParameters(this.takeProfessorEvenIfSameStudents, this.motherNatureExtraMovements, this.towersCountInInfluence, this.extraInfluence, this.colorWithNoInfluence, this.alreadyActivateCharacterCard);
    }

    /**
     *
     * @param takeProfessorEvenIfSameStudents true if the player can take control of any number of professors,even if another player has the same number of students as the player who currently controls them
     * @return the updated clone of the ExpertParameters
     */
    public ExpertParameters withIsTakeProfessorEvenIfSameStudents(boolean takeProfessorEvenIfSameStudents) {
        ExpertParameters retVal = this.deepClone();
        retVal.takeProfessorEvenIfSameStudents = takeProfessorEvenIfSameStudents;
        return retVal;
    }

    /**
     *
     * @param motherNatureExtraMovements number of mother nature's extra steps
     * @return the updated clone of the ExpertParameters
     */
    public ExpertParameters withMotherNatureExtraMovements(int motherNatureExtraMovements) {
        ExpertParameters retVal = this.deepClone();
        retVal.motherNatureExtraMovements = motherNatureExtraMovements;
        return retVal;
    }

    /**
     *
     * @param towersCountInInfluence true if the towers count in influence, false otherwise
     * @return the updated clone of the ExpertParameters
     */
    public ExpertParameters withIsTowersCountInInfluence(boolean towersCountInInfluence) {
        ExpertParameters retVal = this.deepClone();
        retVal.towersCountInInfluence = towersCountInInfluence;
        return retVal;
    }

    /**
     *
     * @param extraInfluence the extra influence
     * @return the updated clone of the ExpertParameters
     */
    public ExpertParameters withExtraInfluence(int extraInfluence) {
        ExpertParameters retVal = this.deepClone();
        retVal.extraInfluence = extraInfluence;
        return retVal;
    }

    /**
     *
     * @param colorWithNoInfluence the PawnColor with no influence
     * @return the updated clone of the ExpertParameters
     */
    public ExpertParameters withColorWithNoInfluence(PawnColor colorWithNoInfluence) {
        ExpertParameters retVal = this.deepClone();
        retVal.colorWithNoInfluence = colorWithNoInfluence;
        return retVal;
    }

    /**
     *
     * @param alreadyActivateCharacterCard true if a CharacterCard is active
     * @return the updated clone of the ExpertParameters
     */
    public ExpertParameters withHasAlreadyActivateCharacterCard(boolean alreadyActivateCharacterCard) {
        ExpertParameters retVal = this.deepClone();
        retVal.alreadyActivateCharacterCard = alreadyActivateCharacterCard;
        return retVal;
    }

}
