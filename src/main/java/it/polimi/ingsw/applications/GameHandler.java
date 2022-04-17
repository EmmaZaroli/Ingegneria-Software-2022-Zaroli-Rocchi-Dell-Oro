package it.polimi.ingsw.applications;

import it.polimi.ingsw.applications.enums.NicknameStatus;
import it.polimi.ingsw.controller.GameController;

public class GameHandler extends Thread{
    GameController gameController;
    User[] users;

    public GameHandler(GameController gameController) {
        this.gameController = gameController;
        //TODO get users
        //TODO get view
    }

    @Override
    public void run() {

    }

    public NicknameStatus checkNicknameStatus(String nickname){
        NicknameStatus status = NicknameStatus.FREE;
        for(User user: users){
            if(user.getNickname().equals(nickname)) {
                if (user.isOnline())
                    status = NicknameStatus.FROM_CONNECTED_PLAYER;
                else
                    status = NicknameStatus.FROM_DISCONNECTED_PLAYER;
            }
        }
        return status;
    }
}
