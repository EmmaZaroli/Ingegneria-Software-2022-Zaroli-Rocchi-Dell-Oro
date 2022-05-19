package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/start-connection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        stage.setTitle("Eriantys");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
