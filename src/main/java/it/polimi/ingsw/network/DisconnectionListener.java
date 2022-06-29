package it.polimi.ingsw.network;

/**
 * Disconnection Listener
 * Used by the endPoint to notify a disconnection
 */
public interface DisconnectionListener {
    void onDisconnect(Object disconnected);
}
