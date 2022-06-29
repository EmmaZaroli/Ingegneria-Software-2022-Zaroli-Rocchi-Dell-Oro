package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.ExpertGameParameters;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serial;
import java.io.Serializable;

/**
 * ExpertParameters Dto
 */
public class ExpertParametersDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 108L;

    private final boolean takeProfessorEvenIfSameStudents;
    private final int motherNatureExtraMovements;
    private final boolean towersCountInInfluence;
    private final int extraInfluence;
    private final PawnColor colorWithNoInfluence;
    private final boolean alreadyActivateCharacterCard;

    /**
     * Construct an expertParametersDto from an expertGameParameters
     * @param origin the expertGameParameters
     */
    public ExpertParametersDto(ExpertGameParameters origin) {
        this.takeProfessorEvenIfSameStudents = origin.isTakeProfessorEvenIfSameStudents();
        this.motherNatureExtraMovements = origin.getMotherNatureExtraMovements();
        this.towersCountInInfluence = origin.isTowersCountInInfluence();
        this.extraInfluence = origin.getExtraInfluence();
        this.colorWithNoInfluence = origin.getColorWithNoInfluence();
        this.alreadyActivateCharacterCard = origin.hasAlreadyActivateCharacterCard();
    }

    /**
     *
     * @return true if the player can take control of any number of professors,even if another player has the same number of students as the player who currently controls them
     */
    public boolean isTakeProfessorEvenIfSameStudents() {
        return takeProfessorEvenIfSameStudents;
    }

    /**
     *
     * @return the number of mother nature's extra steps
     */
    public int getMotherNatureExtraMovements() {
        return motherNatureExtraMovements;
    }

    /**
     *
     * @return true if the towers count in influence, false otherwise
     */
    public boolean isTowersCountInInfluence() {
        return towersCountInInfluence;
    }

    /**
     *
     * @return the extra influence
     */
    public int getExtraInfluence() {
        return extraInfluence;
    }

    /**
     *
     * @return the pawnColor with no influence
     */
    public PawnColor getColorWithNoInfluence() {
        return colorWithNoInfluence;
    }

    /**
     *
     * @return true if a characterCard is already active
     */
    public boolean hasAlreadyActivateCharacterCard() {
        return alreadyActivateCharacterCard;
    }
}