package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gui extends View {
    private static final int SCREEN_WIDTH = 1535;
    private static final int SCREEN_HEIGHT = 840;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Stage stage;

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
    public void printWelcomeMessage() {
        //TODO evaluate if this methods are in the right place
    }

    @Override
    public void printEnqueuedMessage() {
        //TODO
    }

    @Override
    public void printGameStarting() {
        this.loadScene("/it.polimi.ingsw.client.gui/markups/table.fxml");
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
        //TODO show message
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
        //TODO
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

    public boolean getIsExpertPlayer() {
        return isExpertGame();
    }
    //</editor-fold>
}
