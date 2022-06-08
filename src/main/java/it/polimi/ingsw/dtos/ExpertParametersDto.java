package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.ExpertGameParameters;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serial;
import java.io.Serializable;

public class ExpertParametersDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 108L;

    private final boolean takeProfessorEvenIfSameStudents;
    private final int motherNatureExtraMovements;
    private final boolean towersCountInInfluence;
    private final int extraInfluence;
    private final PawnColor colorWithNoInfluence;
    private final boolean alreadyActivateCharacterCard;

    public ExpertParametersDto(ExpertGameParameters origin) {
        this.takeProfessorEvenIfSameStudents = origin.isTakeProfessorEvenIfSameStudents();
        this.motherNatureExtraMovements = origin.getMotherNatureExtraMovements();
        this.towersCountInInfluence = origin.isTowersCountInInfluence();
        this.extraInfluence = origin.getExtraInfluence();
        this.colorWithNoInfluence = origin.getColorWithNoInfluence();
        this.alreadyActivateCharacterCard = origin.hasAlreadyActivateCharacterCard();
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

    public boolean hasAlreadyActivateCharacterCard() {
        return alreadyActivateCharacterCard;
    }
}