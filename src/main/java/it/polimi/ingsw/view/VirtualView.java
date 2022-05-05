package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.ModelObserver;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.servercontroller.User;

public class VirtualView extends Observable implements ModelObserver {
    private final User user;
    private final Game game;

    public VirtualView(User user, Game game) {
        this.user = user;
        this.game = game;
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
    public void notifyGame(Message message) {
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
    public void update(CharacterCard message) {
        user.sendMessage(new CharacterCardMessage(getCurrentPlayer(), MessageType.CHARACTER_CARD, message));
    }

    @Override
    public void update(IslandCard message) {
        user.sendMessage(new IslandMessage(MessageType.ISLAND, message));
    }

    @Override
    public void update(GamePhase message) {
        switch (game.getGamePhase()) {
            case PLANNING ->
                    user.sendMessage(new GetDeckMessage(getCurrentPlayer(), MessageType.PLANNING, game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()));
            case ACTION_MOVE_STUDENTS ->
                    user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), MessageType.ACTION_MOVE_STUDENTS, "move" + game.getParameters().getStudentsToMove() + "students"));
            case ACTION_MOVE_MOTHER_NATURE ->
                    user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), MessageType.ACTION_MOVE_MOTHER_NATURE, ""));
            case ACTION_CHOOSE_CLOUD ->
                    user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), MessageType.ACTION_CHOOSE_CLOUD, ""));
        }
    }

    @Override
    public void update(Player message) {
        user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), MessageType.CHANGE_PLAYER, ""));
    }

    @Override
    public void update(CloudTile message) {
        user.sendMessage(new CloudMessage("server", MessageType.CLOUD, message));
    }

    @Override
    public void update(AssistantCard message) {
        user.sendMessage(new AssistantPlayedMessage(getCurrentPlayer(), MessageType.ASSISTANT_CARD, message));
    }

    @Override
    public void update(SchoolBoard message) {
        user.sendMessage(new SchoolBoardMessage(getCurrentPlayer(), MessageType.BOARD, message));
    }

    @Override
    public void update(Exception message) {
        user.sendMessage(new ErrorMessage(getCurrentPlayer(), game.getLastError().getMessage()));
    }

    /**
     * Receives a notification from the model
     * create a message and sends it to the Socket
     */
    @Override
    public void update(Object message) {
        //TODO do we need to send every message to every player?

        if (message.getClass().equals(String.class)) {
            //game over, sent to every player
            if (game.isGameOver()) {
                if (((String) message).equals("draw")) {
                    user.sendMessage(new WinMessage(MessageType.DRAW, false));
                } else {
                    user.sendMessage(new WinMessage(MessageType.GAME_OVER, ((String) message).equals(this.user.getNickname())));
                }
            }
            //error sent only to the current player
            else if (this.user.getNickname().equals(getCurrentPlayer())) {
                user.sendMessage(new ErrorMessage(getCurrentPlayer(), (String) message));
            }
        }

        //player's coins varied
        if (message.getClass().equals(Integer.class) && getCurrentPlayer().equals(this.user.getNickname())) {
            user.sendMessage(new CoinMessage(getCurrentPlayer(), (int) message));
        }

    }
}