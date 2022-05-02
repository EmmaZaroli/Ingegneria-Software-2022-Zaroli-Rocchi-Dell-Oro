package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.ExpertGame;

/**
 * An effect actionable during the set-up phase
 */
public interface SetupEffect extends Effect {
    /**
     * Activates the effect
     *
     * @param game      The current game
     * @param table     The current table controller
     * @param character The character related to this effect
     */
    void setupEffect(ExpertGame game, TableController table, CharacterCardWithSetUpAction character);
}