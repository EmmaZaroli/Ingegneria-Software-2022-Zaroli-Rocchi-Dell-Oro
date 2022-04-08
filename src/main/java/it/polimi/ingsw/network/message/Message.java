package it.polimi.ingsw.network.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    MessageType message;
    //TODO
    //HashMap<MessageBodyType, Serializable> body;
    //LinkedList<Serializable> body;
}
