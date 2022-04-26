package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.CharacterCardWithSetUpAction;

public interface SetupEffect extends Effect {
    void setupEffect(TableController table, CharacterCardWithSetUpAction character);
}