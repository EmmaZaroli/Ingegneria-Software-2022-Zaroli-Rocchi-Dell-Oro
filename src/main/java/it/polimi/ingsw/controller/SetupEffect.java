package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.List;

public interface SetupEffect extends Effect{
    public void setupEffect(TableController table, CharacterCardWithSetUpAction character);

}