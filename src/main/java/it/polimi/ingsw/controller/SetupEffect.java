package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.Effect;
import it.polimi.ingsw.controller.TableController;
import it.polimi.ingsw.model.CharacterCardWithSetUpAction;

public interface SetupEffect extends Effect {
    public void setupEffect(TableController table, CharacterCardWithSetUpAction character);

}