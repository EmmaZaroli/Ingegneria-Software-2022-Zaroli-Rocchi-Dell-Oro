package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SchoolBoard extends Pane {
    @FXML
    private PlayerInfo player;

    public void setPlayer(PlayerInfo opponent) {
        this.player = player;
    }

    public int getCoins() {
        return this.player != null ? this.player.getCoins() : 0;
    }

    public String getCoinsString() {
        return "                                                                                              coins: " + this.getCoins();
    }

    public SchoolBoard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/markups/components/schoolboard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
