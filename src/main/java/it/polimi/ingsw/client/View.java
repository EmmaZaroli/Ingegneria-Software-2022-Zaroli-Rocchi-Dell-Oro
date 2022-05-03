package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * View class contains a small representation of the game model
 */
public abstract class View implements MessageListener {
    private boolean isExpertGame;
    private List<PlayerInfo> opponents;
    private PlayerInfo me;
    private List<AssistantCard> deck;
    private List<CloudTile> clouds = new ArrayList<>();
    private int tableCoins;

    private Endpoint endpoint;

    //<editor-fold desc="Getters">
    public PlayerInfo getMe() {
        return me.deepClone();
    }

    public List<PlayerInfo> getOpponents() {
        List<PlayerInfo> retVal = new LinkedList<>();

        for (PlayerInfo player : this.opponents) {
            retVal.add(player.deepClone());
        }

        return retVal;
    }

    public List<AssistantCard> getDeck() {
        //TODO dtos if we have time
        return this.deck;
    }

    public List<CloudTile> getClouds() {
        //TODO dtos if we have time
        return this.clouds;
    }

    public boolean isExpertGame() {
        return isExpertGame;
    }

    public int getCoins() {
        return this.tableCoins;
    }
    //</editor-fold>

    //<editor-fold desc="Facade">
    public abstract void init();

    public abstract void askServerInfo();

    public abstract void askPlayerNickname();

    public abstract void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected);

    public abstract void askGameSettings();

    public abstract void genericMessage(String Message);

    public abstract void changePhase(GamePhase phase);

    public abstract void askAssistantCard(ArrayList<AssistantCard> deck);

    public abstract void updateAssistantCardPlayed(AssistantCard card, String player);

    public abstract void askMotherNatureSteps();

    public abstract void updateCurrentPlayersTurn(String otherPlayer);

    public abstract void updateCloud(CloudTile cloud);

    public abstract void updateIslands(IslandCard island);

    public abstract void updateSchoolBoard(String player, SchoolBoard schoolBoard);

    public abstract void win();

    public abstract void lose();

    public abstract void draw();

    public abstract void errorAndExit(String error);

    public abstract void error(String error);

    public abstract void print();
    //</editor-fold>

    @Override
    public void onMessageReceived(Message message) {
        //TODO
    }

    //<editor-fold desc="Presentation logic">
    protected final void startConnection(String ipAddress, int port) {
        try {
            Socket s = new Socket(ipAddress, port);
            this.endpoint = new Endpoint(s);
            this.endpoint.addMessageListener(this);
            this.endpoint.startReceiving();
            this.askPlayerNickname();
        } catch (IOException e) {
            this.errorAndExit(ErrorMessages.NO_NETWORK);
        }
    }

    protected final void sendPlayerNickname(String nickname) {

    }
    //</editor-fold>
}
