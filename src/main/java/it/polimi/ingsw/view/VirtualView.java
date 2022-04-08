package it.polimi.ingsw.view;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observer;

public class VirtualView implements Observer {

    ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void askNickname() {

    }

    public void askNumberOfPlayer() {

    }

    public void askGameMode() {

    }

    public void askAssistantCard() {

    }

    public void askMovePawn() {

    }

    public void askStepMotherNature() {

    }

    public void askCloudTiles() {

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
