package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.List;

class TestView extends View {
    @Override
    public void printWelcomeMessage() {

    }

    @Override
    public void printEnqueuedMessage() {

    }

    @Override
    public void askServerInfo() {

    }

    @Override
    public void askPlayerNickname() {

    }

    @Override
    public void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected) {

    }

    @Override
    public void askGameSettings() {

    }

    @Override
    public void genericMessage(String message) {

    }

    @Override
    public void changePhase(GamePhase phase) {

    }

    @Override
    public void askAssistantCard(List<AssistantCard> deck) {

    }

    @Override
    public void askMotherNatureSteps() {

    }

    @Override
    public void askStudents() {

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
    public void printGameStarting() {

    }

    @Override
    public void notEnoughPlayer() {

    }

    @Override
    public void printGameOverFromDisconnection() {

    }
}
