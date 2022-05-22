package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.CloudTileDto;
import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.dtos.IslandCardDto;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * View class contains a small representation of the game model
 */
public abstract class View implements MessageListener, UserInterface {
    private boolean isExpertGame;
    private List<PlayerInfo> opponents = new ArrayList<>();
    private PlayerInfo me;
    private ArrayList<CloudTileDto> clouds;
    private int tableCoins;
    private List<LinkedIslands> islands;
    private String currentPlayer;
    private List<CharacterCard> characterCards;
    protected int numberOfIslandOnTable;
    private Endpoint endpoint;

    protected View() {
        this.opponents = new LinkedList<>();
        this.me = new PlayerInfo();
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
        return this.me.getDeck();
    }

    public List<CloudTileDto> getClouds() {
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

        Optional<CloudTileDto> cloud = this.clouds.stream()
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

        if (islands.get(islandMain).getLinkedislands().contains(islands.get(Math.floorMod(islandCancelled - 1, 12)).getMainIsland())) {
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
        islands.get(islandMain).setMainIsland(islands.get(islandMain).getMainIsland().withTower(newTower));
        for (IslandCardDto island : islands.get(islandMain).getLinkedislands()) {
            island.setTower(newTower);
        }
        this.print();
    }

    private void handleMessage(GameStartingMessage message) {
        this.printGameStarting();
        for (int i = 0; i < message.getGame().getOpponents().size(); i++)
            this.opponents.add(new PlayerInfo(message.getGame().getOpponents().get(i)).with(message.getGame().getOpponentsBoard().get(i)));
        this.me = new PlayerInfo(message.getGame().getMe()).with(message.getGame().getSchoolBoard());
        this.clouds = new ArrayList<>(message.getGame().getClouds());
        this.islands = getLinkedIslands(message.getGame().getIslands());
        this.tableCoins = message.getGame().getTableCoins();
        print();
        this.changePhase(GamePhase.PLANNING);
        updateCurrentPlayersTurn(message.getFirstPlayer());
        if (message.getFirstPlayer().equals(getMe().getNickname()))
            this.askAssistantCard(message.getDeckfirstPlayer());
    }
    //</editor-fold>

    private List<LinkedIslands> getLinkedIslands(List<IslandCardDto> list){
        List<LinkedIslands> res = new LinkedList<>();
        for(IslandCardDto island : list){
            res.addAll(getLinkedIslands(island));
        }
        return res;
    }

    private List<LinkedIslands> getLinkedIslands(IslandCardDto island){
        List<LinkedIslands> res = new LinkedList<>();
        int studentsPerIsland = Math.max(1, island.getStudents().size() / island.getSize());
        int studentsIndex = 0;
        for(int i = 0; i < island.getSize(); i++){
            LinkedIslands linkedIsland = new LinkedIslands();
            IslandCardDto islandDto = new IslandCardDto();
            //TODO set uuid
            islandDto = islandDto.withIndeces(island.getIndices().get(i));
            if(i == 0)
                islandDto = islandDto.withMotherNature(island.isHasMotherNature());
            else
                islandDto = islandDto.withMotherNature(false);
            islandDto = islandDto.withTower(island.getTower());
            if(studentsIndex + studentsPerIsland > island.getStudents().size()){
                islandDto = islandDto.withStudents(island.getStudents().subList(studentsIndex, island.getStudents().size()));
                studentsIndex = island.getStudents().size();
            }
            else{
                if(i == island.getSize() - 1){
                    islandDto = islandDto.withStudents(island.getStudents().subList(studentsIndex, island.getStudents().size()));
                }
                else{
                    islandDto = islandDto.withStudents(island.getStudents().subList(studentsIndex, studentsIndex + studentsPerIsland));
                    studentsIndex += studentsPerIsland;
                }
            }

            linkedIsland.setMainIsland(island);
            if(i == 0)
                linkedIsland.setMainConnected(true);
            else
                linkedIsland.setMainConnected(false);

            if(i == island.getSize() - 1)
                linkedIsland.setConnectedWithNext(false);
            else
                linkedIsland.setConnectedWithNext(true);
            res.add(linkedIsland);
        }

        for (int i = 0; i < res.size(); i++){
            LinkedIslands linkedIsland = res.get(i);
            for(int j = 0; j < res.size(); j++){
                if(j != i)
                    linkedIsland.setLinkedislands(res.get(j).getMainIsland());
            }
        }
        return res;
    }

    @Override
    public void onMessageReceived(Message message) {
        //TODO
        if (message instanceof NicknameResponseMessage) handleMessage((NicknameResponseMessage) message);
        if (message instanceof GametypeResponseMessage) handleMessage((GametypeResponseMessage) message);
        if (message instanceof GameStartingMessage) handleMessage((GameStartingMessage) message);
        if (message instanceof CloudMessage) handleMessage((CloudMessage) message);
        if (message instanceof SchoolBoardMessage) handleMessage((SchoolBoardMessage) message);
        if (message instanceof AssistantPlayedMessage) handleMessage((AssistantPlayedMessage) message);
        if (message instanceof CoinMessage) handleMessage((CoinMessage) message);
        if (message instanceof IslandMessage) handleMessage((IslandMessage) message);
        if (message instanceof GameStartingMessage) handleMessage((GameStartingMessage) message);
    }

    //<editor-fold desc="Presentation logic">
    public void start() {
        this.printWelcomeMessage();
        this.askServerInfo();
    }

    protected final void startConnection(String ipAddress, int port) {
        try {
            Socket s = new Socket(ipAddress, port);

            this.endpoint = new Endpoint(s, false);
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

    protected final void sendAssistantCard(AssistantCard assistantCard) {

    }

    protected final void sendStudents(Map<PawnColor, Integer> move) {

    }

    protected final void sendCloudChoice(CloudTileDto cloud) {

    }

    protected final void sendCharacterCard(CharacterCard card) {

    }
    //</editor-fold>
}
