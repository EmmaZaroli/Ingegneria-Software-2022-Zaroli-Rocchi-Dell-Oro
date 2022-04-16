package it.polimi.ingsw.applications;

import it.polimi.ingsw.controller.GameController;

public class GameHandler extends Thread{
    GameController gameController;

    public GameHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void run() {

    }
}
