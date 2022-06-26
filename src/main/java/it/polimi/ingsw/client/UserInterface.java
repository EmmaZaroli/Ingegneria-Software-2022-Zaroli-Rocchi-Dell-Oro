package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.List;

public interface UserInterface {
    void printWelcomeMessage();

    void printEnqueuedMessage();

    void askServerInfo();

    void askPlayerNickname();

    void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected);

    void askGameSettings();

    void genericMessage(String message);

    //TODO remove this
    void changePhase(GamePhase phase);

    void askAssistantCard(List<AssistantCard> deck);

    void askMotherNatureSteps();

    void askStudents();

    void askCloud();

    void updateCurrentPlayersTurn(String otherPlayer);

    void win();

    void lose(List<String> winners);

    void draw(String otherWinner);

    void errorAndExit(String error);

    void error(String error);

    void print();

    void printGameStarting();

    void notEnoughPlayer();

    void printGameOverFromDisconnection();
}
