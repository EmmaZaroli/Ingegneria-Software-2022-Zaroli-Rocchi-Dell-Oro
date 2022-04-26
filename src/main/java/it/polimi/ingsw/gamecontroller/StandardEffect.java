package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.ExpertGameParameters;

public interface StandardEffect extends Effect {
    void activateEffect(ExpertGameParameters parameters, Object... args);

    void reverseEffect(ExpertGameParameters parameters, Object... args);
}
