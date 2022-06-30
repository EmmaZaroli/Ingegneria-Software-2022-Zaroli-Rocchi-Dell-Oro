package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the GUI
     *
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        View view = new Gui(stage);
        stage.setTitle("Eriantys");
        stage.setMaximized(true);
        view.start();
    }
}
