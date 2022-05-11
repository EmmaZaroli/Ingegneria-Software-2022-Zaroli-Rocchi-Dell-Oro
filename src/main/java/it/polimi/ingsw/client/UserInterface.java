package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.ArrayList;

public interface UserInterface {
    void printWelcomeMessage();

    void printEnqueuedMessage();

    void askServerInfo();

    void askPlayerNickname();

    void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected);

    void askGameSettings();

    void genericMessage(String Message);

    void changePhase(GamePhase phase);

    void askAssistantCard(ArrayList<AssistantCard> deck);

    void askMotherNatureSteps();

    void updateCurrentPlayersTurn(String otherPlayer);

    void win();

    void lose();

    void draw();

    void errorAndExit(String error);

    void error(String error);

    void print();

    void printGameStarting();
}
