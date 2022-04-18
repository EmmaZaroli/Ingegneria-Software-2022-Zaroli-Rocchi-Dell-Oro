package it.polimi.ingsw.applications;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.persistency.DataDumper;

import java.util.List;

public class Server {
    public static void main(String[] args) {
        //TODO start thread
        new Server();
    }

    private List<Game> loadSavedGames() {
        DataDumper dd = DataDumper.getInstance();
        return dd.getAllGames();
    }
}
