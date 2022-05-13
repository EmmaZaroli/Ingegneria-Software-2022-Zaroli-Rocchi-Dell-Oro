package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.MessageType;
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
public abstract class View implements MessageListener, UserInterface {
    private boolean isExpertGame;
    private List<PlayerInfo> opponents;
    private PlayerInfo me;
    private ArrayList<AssistantCard> deck;
    private ArrayList<CloudTile> clouds;
    private int tableCoins;
    private List<LinkedIslands> islands;
    private String currentPlayer;
    private List<CharacterCard> characterCards;

    private Endpoint endpoint;

    protected View() {
        this.opponents = new LinkedList<>();
        this.me = new PlayerInfo();
        this.deck = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
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

    public List<CharacterCard> getCharacterCards() {
        return this.characterCards;
    }
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
            this.me = this.me.with(message.getSchoolBoard());
        } else {
            Optional<PlayerInfo> player = this.opponents.stream()
                    .filter(x -> x.getNickname().equals(message.getNickname())).findFirst();

            if (player.isPresent()) {
                //TODO change schoolboard
            }
        }
        this.print();
    }

    private void handleMessage(AssistantPlayedMessage message) {
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.with(message.getAssistantCard());
        } else {
            //TODO withers in list
        }
        this.print();
    }

    private void handleMessage(CoinMessage message) {
        //TODO coins on the table?
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.with(message.getCoins());
        } else {
            //TODO withers in list
        }
        this.print();
    }

    private void handleMessage(IslandMessage message) {
        //TODO after we put the deleted island in the message
        //only if the field deletedIsland is not empty, else only update the island and print
        int islandMain = 0; // main island
        int islandCancelled = 0; // island deleted
        for (int k = 0; k < 12; k++) {
            if (islands.get(k).getMainIsland().getUuid().equals(message.getIsland().getUuid())) {
                islandMain = k;
            }
            if (islands.get(k).getMainIsland().getUuid().equals(message.getDeletedIsland().getUuid())) {
                islandCancelled = k;
            }
        }

        if (islands.get(islandMain).getLinkedislands().contains(islands.get(Math.floorMod(islandCancelled - 1, 12)))) {
            islands.get(Math.floorMod(islandCancelled - 1, 12)).setLinkedislands(islands.get(islandCancelled).getMainIsland());
        } else {
            islands.get(islandCancelled).setLinkedislands(islands.get((Math.floorMod(islandCancelled + 1, 12))).getMainIsland());
        }
        islands.get(islandCancelled).setMainConnected(false);
        islands.get(islandMain).setLinkedislands(islands.get(islandCancelled).getLinkedislands());
        islands.get(islandMain).setLinkedislands(islands.get(islandCancelled).getMainIsland());

        //update island part

        //TODO need a function to add only the new students on the islands
        //islands.get(islandMain).getMainIsland().movePawnOnIsland();

        Tower newTower = message.getIsland().getTower();
        islands.get(islandMain).getMainIsland().setTower(newTower);
        for (IslandCard island : islands.get(islandMain).getLinkedislands()) {
            island.setTower(newTower);
        }
        this.print();
    }
    //</editor-fold>

    @Override
    public void onMessageReceived(Message message) {
        //TODO
        if (message instanceof NicknameResponseMessage) handleMessage((NicknameResponseMessage) message);
        if (message instanceof GametypeResponseMessage) handleMessage((GametypeResponseMessage) message);
        if (message instanceof CloudMessage) handleMessage((CloudMessage) message);
        if (message instanceof SchoolBoardMessage) handleMessage((SchoolBoardMessage) message);
        if (message instanceof AssistantPlayedMessage) handleMessage((AssistantPlayedMessage) message);
        if (message instanceof CoinMessage) handleMessage((CoinMessage) message);
        if (message instanceof IslandMessage) handleMessage((IslandMessage) message);
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

    protected final void sendMotherNatureSteps(int steps) {
        Message m = new MoveMotherNatureMessage(me.getNickname(), steps);
        endpoint.sendMessage(m);
    }
    //</editor-fold>
}
