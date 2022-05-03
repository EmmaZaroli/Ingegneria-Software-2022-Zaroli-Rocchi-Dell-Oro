package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageListener;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * View class contains a small representation of the game model
 */
public abstract class View implements MessageListener {
    protected boolean isExpertGame;
    protected List<PlayerInfo> opponents;
    protected PlayerInfo me;
    protected List<AssistantCard> deck;

    protected List<CloudTile> clouds = new ArrayList<>();

    private Endpoint endpoint;

    public SchoolBoard getBoard() {
        return me.getBoard();
    }

    public AssistantCard getCardThrown() {
        return me.getDiscardPileHead();
    }

    public String getNickname() {
        return me.getNickname();
    }

    public abstract void init();

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

    protected void startConnection(String ipAddress, int port) throws IOException {
        Socket s = new Socket(ipAddress, port);
        this.endpoint = new Endpoint(s);
        this.endpoint.addMessageListener(this);
        this.endpoint.startReceiving();
    }
}
