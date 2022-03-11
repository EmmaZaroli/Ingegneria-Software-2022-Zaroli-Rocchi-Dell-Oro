package it.polimi.ingsw.model;

public class AssistantCard {
    private int value;
    private int motherNatureMovement;

    public AssistantCard(int value, int motherNatureMovement) {
        this.value = value;
        this.motherNatureMovement = motherNatureMovement;
    }

    public int getValue() {
        return value;
    }

    public int getMotherNatureMovement() {
        return motherNatureMovement;
    }
}
