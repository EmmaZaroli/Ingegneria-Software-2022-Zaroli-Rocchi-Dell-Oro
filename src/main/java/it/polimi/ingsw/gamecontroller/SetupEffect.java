package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.Effect;

public interface SetupEffect extends Effect {
    public void setupEffect(TableController table, CharacterCardWithSetUpAction character);
}