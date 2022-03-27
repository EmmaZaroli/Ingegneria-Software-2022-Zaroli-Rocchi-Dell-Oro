package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

import java.util.List;

public interface SetupEffect {
    public void activateEffect(Bag bag, List<PawnColor> students);
}