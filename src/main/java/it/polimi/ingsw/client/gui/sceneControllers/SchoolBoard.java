package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SchoolBoard extends Pane {
    private PlayerInfo opponent;

    public void setOpponent(PlayerInfo opponent) {
        this.opponent = opponent;
    }

    public String getName() {
        return this.opponent != null ? this.opponent.getNickname() : "";
    }

    public int getCoins() {
        return this.opponent != null ? this.opponent.getCoins() : 0;
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
