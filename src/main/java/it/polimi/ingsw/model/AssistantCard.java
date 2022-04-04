package it.polimi.ingsw.model;

/**
 * Assistant card.
 */
public record AssistantCard(int value, int motherNatureMovement) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return value == that.value && motherNatureMovement == that.motherNatureMovement;
    }
}
