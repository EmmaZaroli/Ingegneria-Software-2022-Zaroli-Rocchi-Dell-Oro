package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
    private List<LinkedIslands> islands = new ArrayList<>();
    private String currentPlayer;

    private Endpoint endpoint;

    protected View() {
        this.opponents = new LinkedList<>();
        this.me = new PlayerInfo();
        this.deck = new LinkedList<>();
        this.clouds = new LinkedList<>();
        this.tableCoins = 0;
    }

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

    public List<LinkedIslands> getIslands() {
        return this.islands;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
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
    protected abstract void printWelcomeMessage();

    protected abstract void printEnqueuedMessage();

    protected abstract void askServerInfo();

    protected abstract void askPlayerNickname();

    protected abstract void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected);

    protected abstract void askGameSettings();

    protected abstract void genericMessage(String Message);

    protected abstract void changePhase(GamePhase phase);

    protected abstract void askAssistantCard(ArrayList<AssistantCard> deck);

    protected abstract void askMotherNatureSteps();

    protected abstract void updateCurrentPlayersTurn(String otherPlayer);

    protected abstract void win();

    protected abstract void lose();

    protected abstract void draw();

    protected abstract void errorAndExit(String error);

    protected abstract void error(String error);

    protected abstract void print();
    //</editor-fold>

    //<editor-fold desc="Message handlers">
    private void handleMessage(NicknameResponseMessage message) {
        switch (message.getNicknameStatus()) {
            case FREE -> {
                me = me.with(message.getNickname());
                this.showNicknameResult(true, false);
                this.askGameSettings();
            }
            case FROM_DISCONNECTED_PLAYER -> {
                me = me.with(message.getNickname());
                this.showNicknameResult(true, true);
                //TODO restore model view
            }
            case FROM_CONNECTED_PLAYER -> {
                this.showNicknameResult(false, false);
                this.askPlayerNickname();
            }
        }
    }

    private void handleMessage(GametypeResponseMessage message) {
        if (message.isOk()) {
            this.printEnqueuedMessage();
        } else {
            error(ErrorMessages.PARAMETERS_ERROR);
            this.askServerInfo();
        }
    }

    private void handleMessage(CloudMessage message) {
        //TODO dto with wither
        Optional<CloudTile> cloud = this.clouds.stream()
                .filter(x -> x.getUuid().equals(message.getCloud().getUuid())).findFirst();
        //TODO
    }

    private void handleMessage(SchoolBoardMessage message) {
        //TODO dto with wither
        if (message.getNickname().equals(me.getNickname())) {
            //TODO change my schoolboard
        } else {
            Optional<PlayerInfo> player = this.opponents.stream()
                    .filter(x -> x.getNickname().equals(message.getNickname())).findFirst();

            if (player.isPresent()) {
                //TODO change schoolboard
            }
        }
    }
    //</editor-fold>

    @Override
    public void onMessageReceived(Message message) {
        //TODO
        if (message instanceof NicknameResponseMessage) handleMessage((NicknameResponseMessage) message);
        if (message instanceof GametypeResponseMessage) handleMessage((GametypeResponseMessage) message);
        if (message instanceof CloudMessage) handleMessage((CloudMessage) message);
        if (message instanceof SchoolBoardMessage) handleMessage((SchoolBoardMessage) message);
    }

    //<editor-fold desc="Presentation logic">
    public void start() {
        this.printWelcomeMessage();
        this.askServerInfo();
    }

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
        Message m = new NicknameProposalMessage(nickname, MessageType.NICKNAME_PROPOSAL);
        endpoint.sendMessage(m);
    }

    protected final void sendGameSettings(int numberOfPlayers, boolean expertGame) {
        Message m = new GametypeRequestMessage(
                me.getNickname(),
                MessageType.GAME_TYPE_REQUEST,
                expertGame ? GameMode.EXPERT_MODE : GameMode.NORMAL_MODE,
                numberOfPlayers == 2 ? PlayersNumber.TWO : PlayersNumber.THREE);
        endpoint.sendMessage(m);
        this.isExpertGame = expertGame;
    }
    //</editor-fold>
}
