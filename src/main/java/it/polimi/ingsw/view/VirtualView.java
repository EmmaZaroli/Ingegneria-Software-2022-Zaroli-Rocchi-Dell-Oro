package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.ModelObserver;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.servercontroller.User;

public class VirtualView extends Observable implements ModelObserver, MessageListener {
    private final User user;
    private final Game game;

    public VirtualView(User user, Game game) {
        this.user = user;
        this.game = game;
        user.getEndpoint().addMessageListener(this);
    }

    public Endpoint getClientHandler() {
        return user.getEndpoint();
    }

    public String getPlayerNickname() {
        return user.getNickname();
    }

    public boolean isOnline() {
        return user.isOnline();
    }

    /**
     * Receives a message coming from the client.
     *
     * @param message a message coming from the client
     */
    public void onMessageReceived(Message message) {
        //TODO before sending the message to the controller, it should add the nickname of the player ?
        notify(message);
    }

    private String getCurrentPlayer() {
        return game.getPlayers()[game.getCurrentPlayer()].getNickname();
    }

    /**
     * Notifies the observers about a change on the given CharacterCard
     *
     * @param message The changed CharacterCard
     */
    @Override
    public void update(CharacterCard message, Object[] parameters) {
        user.sendMessage(new CharacterCardMessage(getCurrentPlayer(), MessageType.UPDATE_CHARACTER_CARD, message, parameters));
    }

    @Override
    public void update(IslandCard message) {
        user.sendMessage(new IslandMessage(MessageType.UPDATE_ISLAND, message));
    }

    @Override
    public void update(GamePhase message) {
        switch (game.getGamePhase()) {
            case PLANNING ->
                    //TODO why this?
                    user.sendMessage(new GetDeckMessage(getCurrentPlayer(), MessageType.PLANNING, game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()));
            case ACTION_MOVE_STUDENTS ->
                    user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));
            case ACTION_MOVE_MOTHER_NATURE ->
                    user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));
            case ACTION_CHOOSE_CLOUD ->
                    user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));

        }
    }

    @Override
    public void update(Player message) {
        user.sendMessage(new ChangedPlayerMessage(getCurrentPlayer()));
    }

    @Override
    public void update(CloudTile message) {
        user.sendMessage(new CloudMessage("server", MessageType.UPDATE_CLOUD, message));
    }

    @Override
    public void update(AssistantCard message) {
        user.sendMessage(new AssistantPlayedMessage(getCurrentPlayer(), MessageType.UPDATE_ASSISTANT_CARD, message));
    }

    @Override
    public void update(SchoolBoard message) {
        user.sendMessage(new SchoolBoardMessage(getCurrentPlayer(), MessageType.UPDATE_BOARD, message));
    }

    @Override
    public void update(Exception message) {
        user.sendMessage(new ErrorMessage(getCurrentPlayer(), game.getLastError().getMessage()));
    }

    /**
     * Receives a notification from the model
     * create a message and sends it to the Socket
     */
    //TODO this should be eliminated, right?
    @Override
    public void update(Object message) {

        if (message.getClass().equals(String.class)) {
            //game over, sent to every player
            if (game.isGameOver()) {
                if (((String) message).equals("draw")) {
                    //user.sendMessage(new GameOverMessage(MessageType.DRAW, false));
                } else {
                    //user.sendMessage(new GameOverMessage(MessageType.GAME_OVER, ((String) message).equals(this.user.getNickname())));
                }
            }
            //error sent only to the current player
            else if (this.user.getNickname().equals(getCurrentPlayer())) {
                user.sendMessage(new ErrorMessage(getCurrentPlayer(), (String) message));
            }
        }

        //player's coins varied
        if (message.getClass().equals(Integer.class) && getCurrentPlayer().equals(this.user.getNickname())) {
            user.sendMessage(new CoinMessage(getCurrentPlayer(), (int) message, false));
        }

    }


}