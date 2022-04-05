package it.polimi.ingsw.model;

public interface StandardEffect extends Effect {
    public void activateEffect(ExpertGameParameters parameters, Object ... args);
    public void reverseEffect(ExpertGameParameters parameters, Object ... args);
}
