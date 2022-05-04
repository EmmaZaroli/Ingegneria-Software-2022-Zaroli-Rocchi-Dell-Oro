package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.ExpertGameParameters;

/**
 * An effect actionable by a player
 */
public interface StandardEffect extends Effect {
    /**
     * Activates the effect
     *
     * @param parameters The current game parameters
     * @param args       Additional parameters to set
     */
    void activateEffect(ExpertGameParameters parameters, Object... args);

    /**
     * Deactivates the effect
     *
     * @param parameters The current game parameters
     * @param args       Additional parameters to set
     */
    void reverseEffect(ExpertGameParameters parameters, Object... args);
}
