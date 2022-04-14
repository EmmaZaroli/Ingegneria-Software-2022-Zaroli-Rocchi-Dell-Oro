package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.message.Message;

/**
 * generic interface, supports the update method
 */
public interface Observer<T> {

    public void update(T message);

}
