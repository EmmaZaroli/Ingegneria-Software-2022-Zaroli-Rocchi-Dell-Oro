package it.polimi.ingsw.network;

/**
 * Message Listener
 * Notify a new incoming message
 */
public interface MessageListener {
    void onMessageReceived(Message message);
}
