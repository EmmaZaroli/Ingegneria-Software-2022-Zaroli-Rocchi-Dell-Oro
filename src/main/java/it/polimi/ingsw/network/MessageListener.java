package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;

public interface MessageListener {
    void onMessageReceived(Message message);
}
