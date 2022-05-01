package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.ExpertGame;

public interface SetupEffect extends Effect {
    void setupEffect(ExpertGame game, TableController table, CharacterCardWithSetUpAction character);
}