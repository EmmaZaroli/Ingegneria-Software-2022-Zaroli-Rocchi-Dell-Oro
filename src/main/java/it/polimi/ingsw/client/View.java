package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.CloudTileDto;
import it.polimi.ingsw.dtos.IslandCardDto;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.*;
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
import java.util.List;

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
    private GamePhase currentPhase;
    private boolean areEnoughPlayers;
    private Endpoint endpoint;

    protected View() {
        this.opponents = new LinkedList<>();
        this.me = new PlayerInfo();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.tableCoins = 0;
        this.areEnoughPlayers = true;
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

    public boolean areEnoughPlayers() {
        return areEnoughPlayers;
    }

    public Optional<Integer> getCloudIndex(UUID cloudUuid){
        for (int cloudIndex = 0; cloudIndex < clouds.size(); cloudIndex++){
            if(clouds.get(cloudIndex).getUuid().equals(cloudUuid))
                return Optional.of(cloudIndex);
        }
        return Optional.empty();
    }

    public Optional<PlayerInfo> getOpponent(String nickname){
        return opponents.stream().filter(o -> o.getNickname().equals(nickname)).findFirst();
    }

    public Optional<Integer> getOpponentIndex(String nickname){
        for (int opponentIndex = 0; opponentIndex < opponents.size(); opponentIndex++){
            if(opponents.get(opponentIndex).getNickname().equals(nickname))
                return Optional.of(opponentIndex);
        }
        return Optional.empty();
    }

    //</editor-fold>

    @Override
    public void onMessageReceived(Message message) {
        if (message instanceof NicknameResponseMessage nicknameResponseMessage) handleMessage(nicknameResponseMessage);
        if (message instanceof GametypeResponseMessage gametypeResponseMessage) handleMessage(gametypeResponseMessage);
        if (message instanceof GameStartingMessage gameStartingMessage) handleMessage(gameStartingMessage);
        if (message instanceof ChangedPhaseMessage changedPhaseMessage) handleMessage(changedPhaseMessage);
        if (message instanceof ChangedPlayerMessage changedPlayerMessage) handleMessage(changedPlayerMessage);
        if (message instanceof AssistantPlayedMessage assistantPlayedMessage) handleMessage(assistantPlayedMessage);
        if (message instanceof CloudMessage cloudMessage) handleMessage(cloudMessage);
        if (message instanceof SchoolBoardMessage schoolBoardMessage) handleMessage(schoolBoardMessage);
        if (message instanceof IslandMessage islandMessage) handleMessage(islandMessage);
        if (message instanceof CoinMessage coinMessage) handleMessage(coinMessage);
        if (message instanceof CharacterCardMessage characterCardMessage) handleMessage(characterCardMessage);
        if (message instanceof GameOverMessage gameOverMessage) handleMessage(gameOverMessage);
        if (message instanceof ConnectionMessage connectionMessage) handleMessage(connectionMessage);
        if (message instanceof ErrorMessage errorMessage) handleMessage(errorMessage);
    }

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

    private void handleMessage(GameStartingMessage message) {
        this.printGameStarting();
        for (int i = 0; i < message.getGame().getOpponents().size(); i++)
            this.opponents.add(new PlayerInfo(message.getGame().getOpponents().get(i)).with(message.getGame().getOpponentsBoard().get(i)));
        this.me = new PlayerInfo(message.getGame().getMe()).with(message.getGame().getSchoolBoard());
        this.clouds = new ArrayList<>(message.getGame().getClouds());
        this.islands = getLinkedIslands(message.getGame().getIslands());
        this.tableCoins = message.getGame().getTableCoins();
        print();
        //TODO GameStartingMessage may be sent to a reconnected player
        this.changePhase(GamePhase.PLANNING);
        updateCurrentPlayersTurn(message.getFirstPlayer());
        if (message.getFirstPlayer().equals(getMe().getNickname()))
            this.askAssistantCard(message.getDeckfirstPlayer());
    }

    private void handleMessage(ChangedPhaseMessage message){
        currentPhase = message.getNewPhase();
        changePhase(currentPhase);
    }

    private void handleMessage(ChangedPlayerMessage message){
        currentPlayer = message.getNickname();
        print();
        //TODO add changePlayer() in UserInterface ?
    }

    private void handleMessage(AssistantPlayedMessage message) {
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.with(message.getAssistantCard());
        } else {
            //TODO withers in list
        }
        this.print();
    }

    private void handleMessage(CloudMessage message) {
        Optional<Integer> cloudIndex = getCloudIndex(message.getCloud().getUuid());
        if(cloudIndex.isPresent()){
            clouds.remove((int)cloudIndex.get());
            clouds.add(cloudIndex.get(), message.getCloud());
            print();
        }
    }

    private void handleMessage(SchoolBoardMessage message) {
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.with(message.getSchoolBoard());
        } else {
            Optional<Integer> opponentIndex = getOpponentIndex(message.getNickname());
            if (opponentIndex.isPresent()) {
                PlayerInfo opponent = opponents.get(opponentIndex.get());
                opponents.remove((int)opponentIndex.get());
                opponents.add(opponentIndex.get(), opponent.with(message.getSchoolBoard()));
            }
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

    private void handleMessage(CoinMessage message) {
        //TODO coins on the table?
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.with(message.getCoins());
        } else {
            //TODO withers in list
        }
        this.print();
    }

    private void handleMessage(CharacterCardMessage message) {
        //TODO
    }

    private void handleMessage(GameOverMessage message) {
        if(message.getWinners().contains(me.getNickname())){
            if(message.getWinners().size() == 1)
                win();
            else
                draw(message.getWinners().stream().filter(w -> !w.equals(me.getNickname())).findFirst().get());
        }
        else
            lose(message.getWinners());
    }

    private void handleMessage(ConnectionMessage message) {
        switch (message.getType()){
            case IS_ONLINE -> playerOnline(message.getNickname(), true);
            case IS_OFFLINE -> playerOnline(message.getNickname(), false);
            case NOT_ENOUGH_PLAYERS -> enoughPlayers(false);
            case ENOUGH_PLAYERS -> enoughPlayers(true);
            case GAME_OVER_FROM_DISCONNECTION -> gameOverFromDisconnection();
        }
    }

    private void playerOnline(String nickname, boolean isOnline){
        Optional<PlayerInfo> playerInfo = getOpponent(nickname);
        if(playerInfo.isPresent()){
            playerInfo.get().setOnline(isOnline);
            print();
        }
    }

    private void enoughPlayers(boolean areEnoughPlayers){
        this.areEnoughPlayers = areEnoughPlayers;
        if(areEnoughPlayers)
            print();
        else
            notEnoughPlayer();
    }

    private void handleMessage(ErrorMessage message) {
        //TODO
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

    protected final boolean sendPlayerNickname(String nickname) {
        Message message = new NicknameProposalMessage(nickname);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendGameSettings(PlayersNumber playersNumber, GameMode gameMode) {
        Message message = new GametypeRequestMessage(
                me.getNickname(),
                gameMode, playersNumber);
        endpoint.sendMessage(message);
        this.isExpertGame = (gameMode == GameMode.EXPERT_MODE);
        return true;
    }

    protected final boolean sendMotherNatureSteps(int steps) {
        if(steps < 0)
            return false;
        Message message = new MoveMotherNatureMessage(me.getNickname(), steps);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendAssistantCard(int cardIndex) {
        if(cardIndex < 0 || cardIndex >= me.getDeck().size())
            return false;
        AssistantCard assistantCard = me.getDeck().get(cardIndex);
        Message message = new AssistantPlayedMessage(me.getNickname(), MessageType.ACTION_PLAY_ASSISTANT, assistantCard);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendStudentMoveOnBoard(PawnColor student) {
        if(!me.getBoard().getEntrance().contains(student))
            return false;
        Message message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendStudentMoveOnIsland(PawnColor student, int islandIndex) {
        if(!me.getBoard().getEntrance().contains(student) || islandIndex < 0 || islandIndex >= getIslands().size())
            return false;
        MoveStudentMessage message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        message.setIslandCard(getIslands().get(islandIndex).getMainIsland());
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendCloudChoice(int cloudIndex) {
        if(cloudIndex < 0 || cloudIndex >= getClouds().size())
            return false;
        CloudTileDto cloudTile = getClouds().get(cloudIndex);
        Message message = new CloudMessage(me.getNickname(), MessageType.ACTION_CHOOSE_CLOUD, cloudTile);
        endpoint.sendMessage(message);
        return true;
    }

    /*  parameters:
    *   CHARACTER_ONE:      {PawnColor colorFromCard, island UUID}
    *   CHARACTER_SEVEN:    {List<PawnColor> colorsFromCard, List<PawnColor> colorsFromEntrance}
    *   CHARACTER_NINE:     {PawnColor}
    *   CHARACTER_ELEVEN:   {PawnColor colorFromCard}
    *   NB parameters.lenght must be exactly the size required for the specific character
    * */
    protected final boolean sendCharacterCard(int characterIndex, Object[] parameters) {
        if(characterIndex < 0 || characterIndex >= characterCards.size())
            return false;
        if(!areCharacterParametersOk(getCharacterCards().get(characterIndex), parameters))
            return false;
        Message message = new CharacterCardMessage(me.getNickname(), MessageType.ACTION_USE_CHARACTER, getCharacterCards().get(characterIndex), parameters);
        endpoint.sendMessage(message);
        return true;
    }

    private boolean areCharacterParametersOk(CharacterCard characterCard, Object[] parameters){
        return switch (characterCard.getCharacter()){
            case CHARACTER_ONE -> areParametersOkCharacter1(characterCard, parameters);
            case CHARACTER_SEVEN -> areParametersOkCharacter7(characterCard, parameters);
            case CHARACTER_NINE -> areParametersOkCharacter9(parameters);
            case CHARACTER_ELEVEN -> areParametersOkCharacter11(characterCard, parameters);
            default -> true;
        };
    }

    private boolean areParametersOkCharacter1(CharacterCard card, Object[] parameters){
        if(parameters.length != 2)
            return false;
        if(!(parameters[0] instanceof PawnColor && parameters[1] instanceof UUID))
            return false;
        if(!(card instanceof CharacterCardWithSetUpAction cardWithSetUpAction))
            return false;
        if(!(cardWithSetUpAction.getStudents().contains(parameters[0])))
            return false;
        for(LinkedIslands island : getIslands()){
            if(island.getMainIsland().getUuid().equals(parameters[1]))
                return true;
        }
        return false;
    }

    private boolean areParametersOkCharacter7(CharacterCard card, Object[] parameters){
        if(parameters.length != 2)
            return false;
        if(!(parameters[0] instanceof List<?> && parameters[1] instanceof List<?>))
            return false;
        if(!(card instanceof CharacterCardWithSetUpAction cardWithSetUpAction))
            return false;
        List<PawnColor> colorsFromCard = (List<PawnColor>) parameters[0];
        List<PawnColor> colorsFromEntrance = (List<PawnColor>) parameters[1];
        Map<PawnColor, Integer> cardinalityCard = ((CharacterCardWithSetUpAction) cardWithSetUpAction).getStudentsCardinality();
        Map<PawnColor, Integer> cardinalityEntrance = me.getBoard().getStudentsInEntranceCardinality();
        for(PawnColor color : PawnColor.values()){
            if(colorsFromCard.stream().filter(x -> x==color).count() > cardinalityCard.get(color))
                return false;
        }
        for(PawnColor color : PawnColor.values()){
            if(colorsFromEntrance.stream().filter(x -> x==color).count() > cardinalityEntrance.get(color))
                return false;
        }
        return true;
    }

    private boolean areParametersOkCharacter9(Object[] parameters){
        return (parameters.length == 1) && (parameters[0] instanceof PawnColor);
    }

    private boolean areParametersOkCharacter11(CharacterCard card, Object[] parameters){
        if(parameters.length != 1)
            return false;
        if(!(parameters[0] instanceof PawnColor))
            return false;
        if(!(card instanceof CharacterCardWithSetUpAction cardWithSetUpAction))
            return false;
        return cardWithSetUpAction.getStudents().contains(parameters[0]);
    }
    //</editor-fold>
}
