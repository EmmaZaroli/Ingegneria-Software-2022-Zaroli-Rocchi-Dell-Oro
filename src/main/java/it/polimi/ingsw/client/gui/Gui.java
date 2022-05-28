package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        new GuiManager(stage);
        stage.setTitle("Eriantys");
        stage.setMaximized(true);
        stage.show();
    }
}
