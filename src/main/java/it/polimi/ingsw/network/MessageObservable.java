package it.polimi.ingsw.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Message Observable
 */
public class MessageObservable {

    private final List<MessageListener> listeners = new ArrayList<>();

    /**
     * Adds a listener.
     *
     * @param listener the listener to be added.
     */
    public void addListener(MessageListener listener) {
        synchronized (listeners){
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener.
     *
     * @param listener the listener to be removed.
     */
    public void removeListener(MessageListener listener) {
        synchronized (listeners){
            listeners.remove(listener);
        }
    }

    public void notifyMessage(Message message){
        synchronized (listeners){
            List<MessageListener> temporaryCopy = new ArrayList<>(listeners);
            for(MessageListener listener : temporaryCopy){
                listener.onMessageReceived(message);
            }
        }
    }

}
