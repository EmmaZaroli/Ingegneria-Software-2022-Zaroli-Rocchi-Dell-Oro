package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GuiManager extends View {
    private Stage stage;
    private static GuiManager instance;

    public GuiManager(Stage stage) {
        this.stage = stage;
        this.start();
        instance = this;
    }

    public static GuiManager tryGetInstance() {
        return instance;
    }

    @Override
    public void printWelcomeMessage() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/start-connection.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 400, 400);
            stage.setScene(scene);
        } catch (IOException e) {
            //TODO
        }
    }

    @Override
    public void printEnqueuedMessage() {

    }

    @Override
    public void printGameStartingMessage() {

    }

    @Override
    public void askServerInfo() {
    }

    @Override
    public void askPlayerNickname() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.client.gui/ask-nickname.fxml"));
        try {
            stage.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            //TODO
        }
    }

    @Override
    public void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected) {

    }

    @Override
    public void askGameSettings() {

    }

    @Override
    public void genericMessage(String Message) {

    }

    @Override
    public void changePhase(GamePhase phase) {

    }

    @Override
    public void askAssistantCard(ArrayList<AssistantCard> deck) {

    }

    @Override
    public void askMotherNatureSteps() {

    }

    @Override
    public void updateCurrentPlayersTurn(String otherPlayer) {

    }

    @Override
    public void win() {

    }

    @Override
    public void lose() {

    }

    @Override
    public void draw() {

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
    public void printGameStarting() {

    }
}
