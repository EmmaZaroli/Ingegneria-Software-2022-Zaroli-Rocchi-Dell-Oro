package it.polimi.ingsw.network;

import it.polimi.ingsw.network.message.Message;

public interface MessageListener {
    void onMessageReceived(Message message);
}
