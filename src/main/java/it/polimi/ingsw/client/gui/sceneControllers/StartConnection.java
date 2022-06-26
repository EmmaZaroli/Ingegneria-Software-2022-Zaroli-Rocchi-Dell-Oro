package it.polimi.ingsw.client.gui.sceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class StartConnection {
    @FXML
    private TextField ip;
    @FXML
    private TextField port;

    public void tryConnect(ActionEvent event) {
        //GuiManager manager = GuiManager.tryGetInstance();
        //manager.startConnection(ip.getText(), Integer.parseInt(port.getText()));
    }
}
