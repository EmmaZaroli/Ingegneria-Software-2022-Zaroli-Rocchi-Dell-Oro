package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.gamecontroller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.GameStartingMessage;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;
import it.polimi.ingsw.view.VirtualView;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GameHandler implements GameEndingListener {
    GameController gameController;
    Game gameModel; //this is superfluous
    VirtualView[] virtualViews;
    List<User> users;

    private final List<GameEndingListener> gameEndingListeners = new LinkedList<>();

    public GameHandler(List<User> users, GameController gameController, Game gameModel, VirtualView[] virtualViews) {
        this.users = users;
        this.gameController = gameController;
        this.gameModel = gameModel;
        this.virtualViews = virtualViews;
        this.gameModel.addGameEndingListener(this);
    }

    public void start() {
        for (User user : users) {
            user.sendMessage(new GameStartingMessage(user.getNickname(), MessageType.GAME_STARTING, this.gameModel));
        }
    }

    public NicknameStatus checkNicknameStatus(String nickname) {
        NicknameStatus status = NicknameStatus.FREE;
        for (User user : users) {
            if (user.getNickname().equals(nickname)) {
                if (user.isOnline())
                    status = NicknameStatus.FROM_CONNECTED_PLAYER;
                else
                    status = NicknameStatus.FROM_DISCONNECTED_PLAYER;
            }
        }
        return status;
    }

    public boolean containsUser(String nickname) {
        for (User user : users) {
            if (user.getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    public synchronized void reconnectPlayer(String nickname, Endpoint endpoint) {
        for (User user : users) {
            if (user.getNickname().equals(nickname)) {
                if(user.getEndpoint().isPresent())
                    user.getEndpoint().get().removeDisconnectionListener(gameController);
                endpoint.addDisconnectionListener(gameController);
                user.setEndpoint(endpoint);
                user.sendMessage(new GameStartingMessage(user.getNickname(), MessageType.GAME_STARTING, this.gameModel));
            }
        }
        gameController.onReconnect();
    }

    @Override
    public void onGameEnding(UUID uuid) {
        gameModel.removeGameEndingListener(this);
        notifyGameEnding();
    }

    public void addGameEndingListener(GameEndingListener l) {
        this.gameEndingListeners.add(l);
    }

    public void removeGameEndingListener(GameEndingListener l) {
        this.gameEndingListeners.remove(l);
    }

    private void notifyGameEnding() {
        for(GameEndingListener l : gameEndingListeners)
            l.onGameEnding(getGameId());
    }

    public UUID getGameId() {
        return gameModel.getGameId();
    }

    public List<User> getUsers() {
        return users;
    }
}
