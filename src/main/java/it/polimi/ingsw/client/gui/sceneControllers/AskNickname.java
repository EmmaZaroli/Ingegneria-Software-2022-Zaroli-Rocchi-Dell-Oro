package it.polimi.ingsw.client.gui.sceneControllers;

import it.polimi.ingsw.client.gui.GuiManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AskNickname {
    @FXML
    private TextField nickname;

    public void sendNickname(ActionEvent actionEvent) {
        GuiManager manager = GuiManager.tryGetInstance();
        manager.sendPlayerNickname(nickname.getText());
    }
}
