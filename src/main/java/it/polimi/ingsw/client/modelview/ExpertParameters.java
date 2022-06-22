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

    public boolean isAlreadyActivateCharacterCard() {
        return alreadyActivateCharacterCard;
    }

    private ExpertParameters deepClone(){
        return new ExpertParameters(this.takeProfessorEvenIfSameStudents, this.motherNatureExtraMovements, this.towersCountInInfluence, this.extraInfluence, this.colorWithNoInfluence, this.alreadyActivateCharacterCard);
    }

    public ExpertParameters withIsTakeProfessorEvenIfSameStudents(boolean takeProfessorEvenIfSameStudents) {
        ExpertParameters retVal = this.deepClone();
        retVal.takeProfessorEvenIfSameStudents = takeProfessorEvenIfSameStudents;
        return retVal;
    }

    public ExpertParameters withMotherNatureExtraMovements(int motherNatureExtraMovements) {
        ExpertParameters retVal = this.deepClone();
        retVal.motherNatureExtraMovements = motherNatureExtraMovements;
        return retVal;
    }

    public ExpertParameters withIsTowersCountInInfluence(boolean towersCountInInfluence) {
        ExpertParameters retVal = this.deepClone();
        retVal.towersCountInInfluence = towersCountInInfluence;
        return retVal;
    }

    public ExpertParameters withExtraInfluence(int extraInfluence) {
        ExpertParameters retVal = this.deepClone();
        retVal.extraInfluence = extraInfluence;
        return retVal;
    }

    public ExpertParameters withColorWithNoInfluence(PawnColor colorWithNoInfluence) {
        ExpertParameters retVal = this.deepClone();
        retVal.colorWithNoInfluence = colorWithNoInfluence;
        return retVal;
    }

    public ExpertParameters withHasAlreadyActivateCharacterCard(boolean alreadyActivateCharacterCard) {
        ExpertParameters retVal = this.deepClone();
        retVal.alreadyActivateCharacterCard = alreadyActivateCharacterCard;
        return retVal;
    }

}
