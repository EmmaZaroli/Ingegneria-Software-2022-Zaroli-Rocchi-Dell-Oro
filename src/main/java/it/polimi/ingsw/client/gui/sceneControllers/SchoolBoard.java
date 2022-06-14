package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SchoolBoard extends Pane {
    private PlayerInfo player;

    @FXML
    private Label name;
    @FXML
    private Label coins;

    public void setPlayer(PlayerInfo opponent) {
        this.name.setText(opponent.getNickname());
        this.coins.setText("                                                                                              coins: " + opponent.getCoins());
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
