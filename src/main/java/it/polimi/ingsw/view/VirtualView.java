package it.polimi.ingsw.view;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public class VirtualView extends Observable implements Observer {
    /**
     * The user owning the view.
     */
    private final String nickname;

    /**
     * The connection to the client.
     */
    ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler, String nickname) {
        this.nickname = nickname;
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public String getPlayerNickname() {
        return nickname;
    }

    /**
     * Receives a message coming from the client.
     *
     * @param message a message coming from the client
     */
    public void notifyGame(Message message) {
        //before sending the message to the controller, it should add the nickname of the player ?
        notify(message);
    }

    /**
     * Receives a notification from the model that something has change
     * The message is sent over the network to the client's View.
     */
    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }
}
