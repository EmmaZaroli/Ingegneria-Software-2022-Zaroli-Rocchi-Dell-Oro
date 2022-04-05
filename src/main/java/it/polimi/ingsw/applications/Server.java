package it.polimi.ingsw.applications;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.persistency.DataDumper;

import java.util.List;

public class Server {
    public static void main(String[] args) {
        //TODO start thread
        new Server();
    }

    //TODO we need a runnable wrapper around GameController
    private List<GameController> loadSavedGames() {
        DataDumper dd = DataDumper.getInstance();
        return dd.getAllGames();
    }
}
