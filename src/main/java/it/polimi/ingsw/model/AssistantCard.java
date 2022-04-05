package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Assistant card.
 */
public record AssistantCard(int value, int motherNatureMovement) implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return value == that.value && motherNatureMovement == that.motherNatureMovement;
    }
}
