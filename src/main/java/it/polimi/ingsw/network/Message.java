package it.polimi.ingsw.network;

import java.io.Serializable;

public abstract class Message implements Serializable {
    MessageType message;
    //TODO
    //HashMap<MessageBodyType, Serializable> body;
    //LinkedList<Serializable> body;
}
