package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.message.ChangedPhaseMessage;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public class VirtualView<T> extends Observable<T> implements Observer<T> {
    /**
     * The user owning the view.
     */
    private final String nickname;

    /**
     * The connection to the client.
     */
    private final Endpoint clientHandler;
    private final Game game;

    public VirtualView(Endpoint clientHandler, String nickname, Game game) {
        this.nickname = nickname;
        this.clientHandler = clientHandler;
        this.game = game;
    }

    public Endpoint getClientHandler() {
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
        //TODO before sending the message to the controller, it should add the nickname of the player ?
        notify((T) message);
    }

    /**
     * Receives a notification from the model
     * create a message depending on the game phase
     */

    @Override
    public void update(T message) {
        String currentPlayer = game.getPlayers()[game.getCurrentPlayer()].getNickname();
        //TODO do we need to send every message to every player?

        if (message.getClass().isInstance(game)) {
            if (currentPlayer.equals(this.nickname)) {
                switch (game.getGamePhase()) {
                    case PLANNING -> clientHandler.sendMessage(new GetDeckMessage(this.nickname, MessageType.PLANNING, game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()));
                    case ACTION_MOVE_STUDENTS -> clientHandler.sendMessage(new ChangedPhaseMessage(this.nickname, MessageType.ACTION_MOVE_STUDENTS, "move" + game.getParameters().getStudentsToMove() + "students"));
                    case ACTION_MOVE_MOTHER_NATURE -> clientHandler.sendMessage(new ChangedPhaseMessage(this.nickname, MessageType.ACTION_MOVE_MOTHER_NATURE, ""));
                    case ACTION_CHOOSE_CLOUD -> clientHandler.sendMessage(new ChangedPhaseMessage(this.nickname, MessageType.ACTION_CHOOSE_CLOUD, ""));
                }
            }
        }
        if (message.getClass().equals(CloudTile.class)) {
            clientHandler.sendMessage(new CloudMessage(MessageType.CLOUD, (CloudTile) message));
        }
        if (message.getClass().equals(IslandCard.class)) {

        }

        //maybe it's the only one that we should send only to the current player?
        if (message.getClass().equals(SchoolBoard.class)) {
            clientHandler.sendMessage(new BoardMessage(currentPlayer, MessageType.BOARD, (SchoolBoard) message));
        }
    }
}
