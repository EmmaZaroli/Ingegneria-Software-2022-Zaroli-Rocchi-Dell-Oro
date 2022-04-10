package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.message.ChangedPhaseMessage;
import it.polimi.ingsw.network.message.*;
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
    private final ClientHandler clientHandler;
    private final Game game;

    public VirtualView(ClientHandler clientHandler, String nickname, Game game) {
        this.nickname = nickname;
        this.clientHandler = clientHandler;
        this.game = game;
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
     * Receives a notification from the model through a message that something has change
     * The message is sent over the network to the client's View.
     */
    @Override
    public void update(Message message) {
        //update board message
        clientHandler.sendMessage(message);
    }

    /**
     * Receives a notification from the model
     * create a message depending on the game phase
     */
    @Override
    public void update() {
        if (game.getPlayers()[game.getCurrentPlayer()].getNickname().equals(this.nickname)) {
            switch (game.getGamePhase()) {
                case PLANNING -> clientHandler.sendMessage(new GetDeckMessage(this.nickname, MessageType.PLANNING, game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()));
                case ACTION_MOVE_STUDENTS -> clientHandler.sendMessage(new ChangedPhaseMessage(this.nickname, MessageType.ACTION_MOVE_STUDENTS, "move" + game.getParameters().getStudentsToMove() + "students"));
                case ACTION_MOVE_MOTHER_NATURE -> clientHandler.sendMessage(new ChangedPhaseMessage(this.nickname, MessageType.ACTION_MOVE_MOTHER_NATURE, ""));
                case ACTION_CHOOSE_CLOUD -> clientHandler.sendMessage(new ChangedPhaseMessage(this.nickname, MessageType.ACTION_CHOOSE_CLOUD, ""));
            }
        }
    }
}
