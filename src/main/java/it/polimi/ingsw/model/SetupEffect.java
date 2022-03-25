package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

import java.util.Map;

public interface SetupEffect {
    public void activateEffect(Bag bag, Map<PawnColor, Integer> students);
}