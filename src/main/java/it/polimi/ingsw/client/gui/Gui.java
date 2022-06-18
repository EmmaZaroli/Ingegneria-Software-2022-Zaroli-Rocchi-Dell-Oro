package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.sceneControllers.SchoolBoard;
import it.polimi.ingsw.client.gui.sceneControllers.SelectAssistant;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gui extends View implements Initializable {
    public static final int SCREEN_WIDTH = 1535;
    public static final int SCREEN_HEIGHT = 840;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Stage stage;

    private Alert sharedAlert;
    private boolean gameHasStarted = false;

    @FXML
    private SchoolBoard opponent1;
    @FXML
    private SchoolBoard opponent2;
    @FXML
    private GridPane assistants;

    public Gui(Stage stage) {
        this.stage = stage;
    }

    private void loadScene(String resourceName) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
            fxmlLoader.setController(this);
            try {
                Scene scene = new Scene(fxmlLoader.load(), SCREEN_WIDTH, SCREEN_HEIGHT);
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Unable to load scene " + resourceName, e);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        print();
    }

    @Override
    public void printWelcomeMessage() {
        //TODO evaluate if this methods are in the right place
    }

    @Override
    public void printEnqueuedMessage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sharedAlert = new Alert(Alert.AlertType.INFORMATION, "You've been added to the lobby. Please, wait for a game to start");
                while (sharedAlert.showAndWait() != null && !gameHasStarted) {
                }
            }
        });
    }

    @Override
    public void printGameStarting() {
        if (this.sharedAlert != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    sharedAlert.close();
                }
            });
        }
        this.loadScene("/it.polimi.ingsw.client.gui/markups/table.fxml");
        this.gameHasStarted = true;
    }

    @Override
    public void askServerInfo() {
        this.loadScene("/it.polimi.ingsw.client.gui/markups/start-connection.fxml");
    }

    @Override
    public void askPlayerNickname() {
        this.loadScene("/it.polimi.ingsw.client.gui/markups/ask-nickname.fxml");
    }

    @Override
    public void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (playerReconnected) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You've been reconnected to your previous game", ButtonType.OK);
                    alert.showAndWait();
                } else if (!nicknameAccepted) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Your nickname is not available, please, select another nickname", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }

    @Override
    public void askGameSettings() {
        this.loadScene("/it.polimi.ingsw.client.gui/markups/ask-game-settings.fxml");
    }

    @Override
    public void genericMessage(String Message) {
        //TODO
    }

    @Override
    public void changePhase(GamePhase phase) {
        //TODO
    }

    @Override
    public void askAssistantCard(List<AssistantCard> deck) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new SelectAssistant(stage, getDeck(), new AssistantCardEventHandler() {
                    @Override
                    public void onSelect(int index) {
                        sendAssistantCard(index);
                    }
                });
                dialog.showAndWait();
            }
        });
    }

    @Override
    public void askMotherNatureSteps() {
        //TODO
    }

    @Override
    public void askStudents() {
        //TODO
    }

    @Override
    public void askCloud() {

    }

    @Override
    public void updateCurrentPlayersTurn(String otherPlayer) {

    }

    @Override
    public void win() {

    }

    @Override
    public void lose(List<String> winners) {

    }

    @Override
    public void draw(String otherWinner) {

    }

    @Override
    public void errorAndExit(String error) {

    }

    @Override
    public void error(String error) {

    }

    @Override
    public void print() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (opponent1 != null) {
                    opponent1.setPlayer(getOpponents().get(0));
                }
                if (opponent2 != null && getOpponents().size() > 1) {
                    opponent2.setPlayer(getOpponents().get(1));
                }
            }
        });
    }

    @Override
    public void notEnoughPlayer() {

    }

    @Override
    public void gameOverFromDisconnection() {

    }

    public void tryConnect(ActionEvent event) {
        TextField ip = (TextField) stage.getScene().lookup("#ip");
        TextField port = (TextField) stage.getScene().lookup("#port");
        startConnection(ip.getText(), Integer.parseInt(port.getText()));
    }

    public void sendNickname(ActionEvent actionEvent) {
        TextField nickname = (TextField) stage.getScene().lookup("#nickname");
        this.sendPlayerNickname(nickname.getText());
    }

    public void sendSettings(ActionEvent event) {
        RadioButton twoPlayers = (RadioButton) stage.getScene().lookup("#twoPlayers");
        RadioButton threePlayers = (RadioButton) stage.getScene().lookup("#threePlayers");
        RadioButton standard = (RadioButton) stage.getScene().lookup("#standardMode");
        RadioButton expert = (RadioButton) stage.getScene().lookup("#expert");

        if ((twoPlayers.isSelected() || threePlayers.isSelected()) && (standard.isSelected() || expert.isSelected())) {
            PlayersNumber playersNumber = twoPlayers.isSelected() ? PlayersNumber.TWO : PlayersNumber.THREE;
            GameMode mode = standard.isSelected() ? GameMode.NORMAL_MODE : GameMode.EXPERT_MODE;
            this.sendGameSettings(playersNumber, mode);
        }
    }

    //<editor-fold desc="Bindings">
    public boolean getIsThreePlayers() {
        return this.getOpponents().size() == 2;
    }

    public boolean getIsExpertGame() {
        return isExpertGame();
    }

    public int getMyCoins() {
        return 46;
    }
    //</editor-fold>
}
