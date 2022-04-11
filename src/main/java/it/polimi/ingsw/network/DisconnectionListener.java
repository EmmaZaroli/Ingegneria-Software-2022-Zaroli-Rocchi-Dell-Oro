package it.polimi.ingsw.network;

public interface DisconnectionListener {
    //TODO maybe we need to store a reference to the specific client
    void onDisconnect();
}
