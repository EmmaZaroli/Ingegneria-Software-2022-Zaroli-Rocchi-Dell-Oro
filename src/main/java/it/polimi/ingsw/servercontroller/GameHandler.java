package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.gamecontroller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.GameStartingMessage;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;
import it.polimi.ingsw.view.VirtualView;

public class GameHandler {
    GameController gameController;
    Game gameModel; //this is superfluous
    VirtualView[] virtualViews;
    User[] users;

    public GameHandler(User[] users, GameController gameController, Game gameModel, VirtualView[] virtualViews) {
        this.users = users;
        this.gameController = gameController;
        this.gameModel = gameModel;
        this.virtualViews = virtualViews;
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

    public void reconnectPlayer(String nickname, Endpoint endpoint) {
        for (User user : users) {
            if (user.getNickname().equals(nickname)) {
                if(user.getEndpoint().isPresent())
                    user.getEndpoint().get().removeDisconnectionListener(gameController);
                endpoint.addDisconnectionListener(gameController);
                user.setEndpoint(endpoint);
                user.sendMessage(new GameStartingMessage(user.getNickname(), MessageType.GAME_STARTING, this.gameModel));
            }
        }
        gameController.onReconnect();//TODO change to listener
    }
}
