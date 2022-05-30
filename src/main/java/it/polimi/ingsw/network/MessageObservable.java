package it.polimi.ingsw.network;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.observer.ModelObserver;

import java.util.ArrayList;
import java.util.List;

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
