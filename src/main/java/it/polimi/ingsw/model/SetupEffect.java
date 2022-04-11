package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.TableController;

public interface SetupEffect extends Effect {
    public void setupEffect(TableController table, CharacterCardWithSetUpAction character);

}