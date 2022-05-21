package it.polimi.ingsw.client.gui.sceneControllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Card extends Pane {
    public Card() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
