package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.gamecontroller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.GameMessage;
import it.polimi.ingsw.view.VirtualView;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Game Handler
 * This class is used for managing a game
 */
public class GameHandler implements GameEndingListener {
    GameController gameController;
    Game gameModel;
    VirtualView[] virtualViews;
    List<User> users;

    private final List<GameEndingListener> gameEndingListeners = new LinkedList<>();

    /**
     * Default Constructor
     * @param users the players
     * @param gameController the gameController
     * @param gameModel the game
     * @param virtualViews the virtual views
     */
    public GameHandler(List<User> users, GameController gameController, Game gameModel, VirtualView[] virtualViews) {
        this.users = users;
        this.gameController = gameController;
        this.gameModel = gameModel;
        this.virtualViews = virtualViews;
        this.gameModel.addGameEndingListener(this);
    }

    /**
     * Notify the players that the game is starting
     */
    public void start() {
        for (User user : users) {
            user.sendMessage(new GameMessage(user.getNickname(), MessageType.GAME_STARTING, this.gameModel));
        }
    }

    /**
     * @param nickname the nickname
     * @return true if in this gameHandler there's a user with the same nickname as the @param nickname
     */
    public boolean containsUser(String nickname) {
        for (User user : users) {
            if (user.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    /**
     * Reconnects a player
     * @param nickname the player's nickname
     * @param endpoint the player's endpoint
     */
    public synchronized void reconnectPlayer(String nickname, Endpoint endpoint) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getNickname().equals(nickname)) {
                if(users.get(i).getEndpoint().isPresent()) {
                    users.get(i).getEndpoint().get().removeDisconnectionListener(gameController);
                    users.get(i).getEndpoint().get().removeMessageListener(virtualViews[i]);
                }
                endpoint.addDisconnectionListener(gameController);
                endpoint.addMessageListener(virtualViews[i]);
                users.get(i).setEndpoint(endpoint);
                users.get(i).sendMessage(new GameMessage(users.get(i).getNickname(), MessageType.GAME_STARTING, this.gameModel));
            }
        }
        gameController.onReconnect(nickname);
    }

    @Override
    public void onGameEnding(UUID uuid) {
        gameModel.removeGameEndingListener(this);
        notifyGameEnding();
    }

    /**
     * Adds a gameEndingListener
     * @param l the gameEndingListener
     */
    public void addGameEndingListener(GameEndingListener l) {
        this.gameEndingListeners.add(l);
    }

    /**
     * Remove a gameEndingListener
     * @param l the gameEndingListener
     */
    public void removeGameEndingListener(GameEndingListener l) {
        this.gameEndingListeners.remove(l);
    }

    /**
     * Notify that the game has ended
     */
    private void notifyGameEnding() {
        for(GameEndingListener l : gameEndingListeners)
            l.onGameEnding(getGameId());
    }

    /**
     *
     * @return the game's uuid
     */
    public UUID getGameId() {
        return gameModel.getGameId();
    }

    /**
     *
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }
}
