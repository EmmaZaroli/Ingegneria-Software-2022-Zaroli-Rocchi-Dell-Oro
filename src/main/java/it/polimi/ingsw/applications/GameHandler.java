package it.polimi.ingsw.applications;

import it.polimi.ingsw.controller.GameController;

public class GameHandler implements Runnable{
    GameController gameController;

    public GameHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void run() {

    }
}
