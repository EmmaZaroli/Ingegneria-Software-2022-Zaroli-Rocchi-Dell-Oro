package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.sceneControllers.*;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    private boolean isChooseCloud = false;

    @FXML
    private SchoolBoard opponent1;
    @FXML
    private SchoolBoard opponent2;
    @FXML
    private GridPane assistants;
    @FXML
    private Cloud cloud1;
    @FXML
    private Cloud cloud2;
    @FXML
    private Cloud cloud3;
    @FXML
    private ImageView discard1;
    @FXML
    private ImageView discard2;
    @FXML
    private ImageView discardMe;
    @FXML
    private Island island0;
    @FXML
    private Island island1;
    @FXML
    private Island island2;
    @FXML
    private Island island3;
    @FXML
    private Island island4;
    @FXML
    private Island island5;
    @FXML
    private Island island6;
    @FXML
    private Island island7;
    @FXML
    private Island island8;
    @FXML
    private Island island9;
    @FXML
    private Island island10;
    @FXML
    private Island island11;
    @FXML
    private EditableSchoolBoard mySchoolBoard;
    @FXML
    private Label message;

    @FXML
    private BigBridge bridge01;
    @FXML
    private Bridge bridge12;
    @FXML
    private Bridge bridge23;
    @FXML
    private Bridge bridge34;
    @FXML
    private Bridge bridge45;
    @FXML
    private BigBridge bridge56;
    @FXML
    private BigBridge bridge67;
    @FXML
    private Bridge bridge78;
    @FXML
    private Bridge bridge89;
    @FXML
    private Bridge bridge910;
    @FXML
    private Bridge bridge1011;
    @FXML
    private BigBridge bridge110;

    private String currentPhase, currentPlayer;

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
        Platform.runLater(() -> {
            sharedAlert = new Alert(Alert.AlertType.INFORMATION, "You've been added to the lobby. Please, wait for a game to start");
            while (sharedAlert.showAndWait().isPresent() && !gameHasStarted) {
            }
        });
    }

    @Override
    public void printGameStarting() {
        if (this.sharedAlert != null) {
            Platform.runLater(() -> sharedAlert.close());
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
        Platform.runLater(() -> {
            if (playerReconnected) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You've been reconnected to your previous game", ButtonType.OK);
                alert.showAndWait();
                this.loadScene("/it.polimi.ingsw.client.gui/markups/table.fxml");
                this.gameHasStarted = true;
            } else if (!nicknameAccepted) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Your nickname is not available, please, select another nickname", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    @Override
    public void askGameSettings() {
        this.loadScene("/it.polimi.ingsw.client.gui/markups/ask-game-settings.fxml");
    }

    @Override
    public void genericMessage(String message) {
        Platform.runLater(() -> {
            sharedAlert = new Alert(Alert.AlertType.INFORMATION, message);
            sharedAlert.showAndWait();
        });
    }

    @Override
    public void changePhase(GamePhase phase) {
        if (message != null) {
            this.currentPhase = phase.name();
            Platform.runLater(() -> message.setText("Current player: " + currentPlayer + " Phase: " + currentPhase));
        }
    }

    @Override
    public void askAssistantCard(List<AssistantCard> deck) {
        Platform.runLater(() -> {
            Dialog dialog = new SelectAssistant(stage, getDeck(), this::sendAssistantCard);
            dialog.showAndWait();
        });
    }

    @Override
    public void askMotherNatureSteps() {
        mySchoolBoard.disableDragAndDrop();
        Platform.runLater(() -> {
            List<ButtonType> buttons = new LinkedList<>();
            for (int i = 1; i <= getMe().getDiscardPileHead().get().motherNatureMovement(); i++) {
                buttons.add(new ButtonType("+" + i, ButtonBar.ButtonData.OK_DONE));
            }
            sharedAlert = new Alert(Alert.AlertType.INFORMATION, "Move Mother Nature of... steps", buttons.toArray(new ButtonType[]{}));
            Optional<ButtonType> response = sharedAlert.showAndWait();
            if (response.isPresent()) {
                sendMotherNatureSteps(Integer.parseInt((response.get().getText()).substring(1)));
            }
        });
    }

    @Override
    public void askStudents() {
        Platform.runLater(() -> {
            if (message != null) {
                message.setText("Move a student from your entrance");
            }
        });
        mySchoolBoard.enableDragAndDrop();
    }

    @Override
    public void askCloud() {
        this.isChooseCloud = true;
        if (message != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    message.setText("Select a cloud");
                }
            });
        }
        if (cloud1 != null) {
            cloud1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isChooseCloud) {
                        sendCloudChoice(0);
                        isChooseCloud = false;
                    }
                }
            });
        }
        if (cloud2 != null) {
            cloud2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isChooseCloud) {
                        sendCloudChoice(1);
                        isChooseCloud = false;
                    }
                }
            });
        }
        if (cloud3 != null) {
            cloud3.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isChooseCloud) {
                        sendCloudChoice(2);
                        isChooseCloud = true;
                    }
                }
            });
        }
    }

    @Override
    public void updateCurrentPlayersTurn(String otherPlayer) {
        if (message != null) {
            this.currentPlayer = otherPlayer;
            Platform.runLater(() -> message.setText("Current player: " + currentPlayer + " Current game phase: " + currentPhase));
        }
    }

    @Override
    public void win() {
        Platform.runLater(() -> {
            sharedAlert = new Alert(Alert.AlertType.INFORMATION, "You won the game");
            sharedAlert.showAndWait();
            sharedAlert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent dialogEvent) {
                    start();
                }
            });
        });
    }

    @Override
    public void lose(List<String> winners) {
        String winnersString = "";
        for (String w : winners) {
            winnersString += w;
            winnersString += " ";
        }
        final String finalWinnerString = winnersString;
        Platform.runLater(() -> {
            sharedAlert = new Alert(Alert.AlertType.INFORMATION, "You lost. " + finalWinnerString + "won the game");
            sharedAlert.showAndWait();
            sharedAlert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent dialogEvent) {
                    start();
                }
            });
        });
    }

    @Override
    public void draw(String otherWinner) {
        //TODO
    }

    @Override
    public void errorAndExit(String error) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sharedAlert = new Alert(Alert.AlertType.ERROR, error);
                sharedAlert.showAndWait();
                sharedAlert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                    @Override
                    public void handle(DialogEvent dialogEvent) {
                        Platform.exit();
                    }
                });
            }
        });
    }

    @Override
    public void error(String error) {
        Platform.runLater(() -> {
            sharedAlert = new Alert(Alert.AlertType.WARNING, error);
            sharedAlert.showAndWait();
        });
    }

    @Override
    public void print() {
        Platform.runLater(() -> {
            changePhase(getCurrentPhase());
            updateCurrentPlayersTurn(getCurrentPlayer());
            updateBridges();
            if (opponent1 != null) {
                opponent1.setPlayer(getOpponents().get(0));
                opponent1.setIsExpert(this.isExpertGame());
            }
            if (opponent2 != null && getOpponents().size() > 1) {
                opponent2.setPlayer(getOpponents().get(1));
                opponent2.setIsExpert(this.isExpertGame());
            }
            if (cloud1 != null) {
                cloud1.setStudents(getClouds().get(0).getStudents());
            }
            if (cloud2 != null) {
                cloud2.setStudents(getClouds().get(1).getStudents());
            }
            if (cloud3 != null && getClouds().size() > 2) {
                cloud3.setStudents(getClouds().get(2).getStudents());
            }
            if (discard1 != null) {
                if (getOpponents().get(0).getDiscardPileHead().isPresent()) {
                    discard1.setImage(new Image("/it.polimi.ingsw.client.gui/assets/Assistente ("
                            + getOpponents().get(0).getDiscardPileHead().get().value() + ").png"));
                }
            }
            if (discard2 != null && getOpponents().size() > 1) {
                if (getOpponents().get(1).getDiscardPileHead().isPresent()) {
                    discard2.setImage(new Image("/it.polimi.ingsw.client.gui/assets/Assistente ("
                            + getOpponents().get(1).getDiscardPileHead().get().value() + ").png"));
                }
            }
            if (discardMe != null) {
                if (getMe().getDiscardPileHead().isPresent()) {
                    discardMe.setImage(new Image("/it.polimi.ingsw.client.gui/assets/Assistente ("
                            + getMe().getDiscardPileHead().get().value() + ").png"));
                }
            }
            if (island0 != null) {
                island0.setCloud(getIslands().get(0));
            }
            if (island1 != null) {
                island1.setCloud(getIslands().get(1));
            }
            if (island2 != null) {
                island2.setCloud(getIslands().get(2));
            }
            if (island3 != null) {
                island3.setCloud(getIslands().get(3));
            }
            if (island4 != null) {
                island4.setCloud(getIslands().get(4));
            }
            if (island5 != null) {
                island5.setCloud(getIslands().get(5));
            }
            if (island6 != null) {
                island6.setCloud(getIslands().get(6));
            }
            if (island7 != null) {
                island7.setCloud(getIslands().get(7));
            }
            if (island8 != null) {
                island8.setCloud(getIslands().get(8));
            }
            if (island9 != null) {
                island9.setCloud(getIslands().get(9));
            }
            if (island10 != null) {
                island10.setCloud(getIslands().get(10));
            }
            if (island11 != null) {
                island11.setCloud(getIslands().get(11));
            }
            if (mySchoolBoard != null) {
                mySchoolBoard.setPlayer(getMe());
                mySchoolBoard.setController(this);
            }
        });
    }

    private void updateBridges() {
        if (bridge01 != null) {
            bridge01.setVisible(getIslands().get(0).isConnectedWithNext());
        }
        if (bridge12 != null) {
            bridge12.setVisible(getIslands().get(1).isConnectedWithNext());
        }
        if (bridge23 != null) {
            bridge23.setVisible(getIslands().get(2).isConnectedWithNext());
        }
        if (bridge34 != null) {
            bridge34.setVisible(getIslands().get(3).isConnectedWithNext());
        }
        if (bridge45 != null) {
            bridge45.setVisible(getIslands().get(4).isConnectedWithNext());
        }
        if (bridge56 != null) {
            bridge56.setVisible(getIslands().get(5).isConnectedWithNext());
        }
        if (bridge67 != null) {
            bridge67.setVisible(getIslands().get(6).isConnectedWithNext());
        }
        if (bridge78 != null) {
            bridge78.setVisible(getIslands().get(7).isConnectedWithNext());
        }
        if (bridge89 != null) {
            bridge89.setVisible(getIslands().get(8).isConnectedWithNext());
        }
        if (bridge910 != null) {
            bridge910.setVisible(getIslands().get(9).isConnectedWithNext());
        }
        if (bridge1011 != null) {
            bridge1011.setVisible(getIslands().get(10).isConnectedWithNext());
        }
        if (bridge110 != null) {
            bridge110.setVisible(getIslands().get(11).isConnectedWithNext());
        }
    }

    @Override
    public void notEnoughPlayer() {
        Platform.runLater(() -> {
            sharedAlert = new Alert(Alert.AlertType.WARNING, "The game has been suspended because you're opponents are offline.");
            while (sharedAlert.showAndWait().isPresent() && !gameHasStarted) {
            }
        });
    }

    @Override
    public void gameOverFromDisconnection() {
        Platform.runLater(() -> {
            sharedAlert = new Alert(Alert.AlertType.CONFIRMATION, "You're opponents are offline. You won the game!");
            sharedAlert.showAndWait();
            sharedAlert.setOnCloseRequest(dialogEvent -> {
                //TODO
            });
        });
    }

    //TODO controls using already defined methods
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

    public void sendToBoard(PawnColor c) {
        sendStudentMoveOnBoard(c);
    }

    public void sendToIsland(PawnColor c, int index) {
        sendStudentMoveOnIsland(c, index);
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
