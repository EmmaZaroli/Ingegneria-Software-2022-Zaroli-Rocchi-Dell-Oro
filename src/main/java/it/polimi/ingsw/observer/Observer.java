package it.polimi.ingsw.observer;

/**
 * generic interface, supports the update method
 */
public interface Observer<T> {

    public void update(T message);

}
