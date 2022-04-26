package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.model.Effect;
import it.polimi.ingsw.model.ExpertGameParameters;

public interface StandardEffect extends Effect {
    public void activateEffect(ExpertGameParameters parameters, Object... args);

    public void reverseEffect(ExpertGameParameters parameters, Object... args);
}
