package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.ExpertParameters;
import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.dtos.*;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.List;

/**
 * View class contains a small representation of the game model
 */
public abstract class View implements MessageListener, UserInterface, DisconnectionListener {
    private boolean isExpertGame;
    private List<PlayerInfo> opponents;
    private PlayerInfo me;
    private ArrayList<CloudTileDto> clouds;
    private int tableCoins;
    private final List<LinkedIslands> islands;
    private String currentPlayer;
    private List<ViewCharacterCard> characterCards;
    private ExpertParameters expertParameters;
    private GamePhase currentPhase;
    private boolean areEnoughPlayers;
    private String error;
    private Endpoint endpoint;

    /**
     * Default Constructor
     */
    protected View() {
        this.opponents = new LinkedList<>();
        this.me = new PlayerInfo();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            this.islands.add(new LinkedIslands((new IslandCardDto()).withIndexes(i)));
        this.tableCoins = 0;
        this.characterCards = new ArrayList<>();
        this.areEnoughPlayers = true;
        this.expertParameters = new ExpertParameters();
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

    public List<ViewCharacterCard> getCharacterCards() {
        return this.characterCards;
    }

    public boolean areEnoughPlayers() {
        return areEnoughPlayers;
    }

    public int getNumberOfIslandOnTable(){
        int count = 0;
        for(LinkedIslands islands : this.islands){
            if(!islands.isConnectedWithNext()) count ++;
        }
        return count;
    }

    public Optional<Integer> getCloudIndex(UUID cloudUuid) {
        for (int cloudIndex = 0; cloudIndex < clouds.size(); cloudIndex++) {
            if (clouds.get(cloudIndex).getUuid().equals(cloudUuid))
                return Optional.of(cloudIndex);
        }
        return Optional.empty();
    }

    public Optional<PlayerInfo> getOpponent(String nickname) {
        return opponents.stream().filter(o -> o.getNickname().equals(nickname)).findFirst();
    }

    public Optional<Integer> getOpponentIndex(String nickname) {
        for (int opponentIndex = 0; opponentIndex < opponents.size(); opponentIndex++) {
            if (opponents.get(opponentIndex).getNickname().equals(nickname))
                return Optional.of(opponentIndex);
        }
        return Optional.empty();
    }

    public Optional<Integer> getOpponentIndex(UUID uuid) {
        for (int opponentIndex = 0; opponentIndex < opponents.size(); opponentIndex++) {
            if (opponents.get(opponentIndex).getBoard().getUuid().equals(uuid))
                return Optional.of(opponentIndex);
        }
        return Optional.empty();
    }

    public GamePhase getCurrentPhase() {
        return this.currentPhase;
    }

    public String getError() {
        return error;
    }

    //</editor-fold>

    /**
     * Calls the method corresponding to the type of message arrived
     * The nickname within the message is of the current player
     * @param message the incoming message
     */
    @Override
    public void onMessageReceived(Message message) {
        if (message instanceof NicknameResponseMessage nicknameResponseMessage) handleMessage(nicknameResponseMessage);
        if (message instanceof GametypeResponseMessage gametypeResponseMessage) handleMessage(gametypeResponseMessage);
        if (message instanceof GameMessage gameMessage) handleMessage(gameMessage);
        if (message instanceof ChangedPhaseMessage changedPhaseMessage) handleMessage(changedPhaseMessage);
        if (message instanceof ChangedPlayerMessage changedPlayerMessage) handleMessage(changedPlayerMessage);
        if (message instanceof AssistantPlayedMessage assistantPlayedMessage) handleMessage(assistantPlayedMessage);
        if (message instanceof CloudMessage cloudMessage) handleMessage(cloudMessage);
        if (message instanceof SchoolBoardMessage schoolBoardMessage) handleMessage(schoolBoardMessage);
        if (message instanceof IslandMessage islandMessage) handleMessage(islandMessage);
        if (message instanceof CoinMessage coinMessage) handleMessage(coinMessage);
        if (message instanceof CharacterCardMessage characterCardMessage) handleMessage(characterCardMessage);
        if (message instanceof ExpertParametersMessage expertParametersMessage) handleMessage(expertParametersMessage);
        if (message instanceof GameOverMessage gameOverMessage) handleMessage(gameOverMessage);
        if (message instanceof ConnectionMessage connectionMessage) handleMessage(connectionMessage);
        if (message instanceof ErrorMessage errorMessage) handleMessage(errorMessage);
        if (message instanceof GetDeckMessage getDeckMessage) handleMessage(getDeckMessage);
        if (message instanceof MoveStudentMessage moveStudentMessage) handleMessage(moveStudentMessage);
        if (message instanceof PlayerCanPlayMessage playerCanPlayMessage) handleMessage(playerCanPlayMessage);
    }

    //<editor-fold desc="Message handlers">

    /**
     * Notify the player the nickname's result
     */
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
            }
            case FROM_CONNECTED_PLAYER -> {
                this.showNicknameResult(false, false);
                this.askPlayerNickname();
            }
        }
    }

    /**
     * Notifies the player that the server is waiting for a move
     */
    private void handleMessage(MoveStudentMessage message){
        if(message.getNickname().equals(getMe().getNickname())) {
            if(me.isCanPlayThisRound() && areEnoughPlayers)
                askAction();
        }
    }

    /**
     * Asks the player which assistantCard to play
     */
    private void handleMessage(GetDeckMessage message) {
        if (message.getNickname().equals(getMe().getNickname())) {
            me = me.with(message.getDeck());
            this.currentPhase = GamePhase.PLANNING;
            if(me.isCanPlayThisRound() && areEnoughPlayers)
                this.askAssistantCard(message.getDeck());
        }
    }

    /**
     * Prints the result of the login
     */
    private void handleMessage(GametypeResponseMessage message) {
        if (message.isOk()) {
            this.printEnqueuedMessage();
        } else {
            this.error = ErrorMessages.PARAMETERS_ERROR;
            error(ErrorMessages.PARAMETERS_ERROR);
            this.askServerInfo();
        }
    }

    /**
     * Assigns the gameMessage attributes to this instance's attributes
     */
    private void handleMessage(GameMessage message) {
            GameDto game = message.getGame();
            if(message.getType() == MessageType.GAME_STARTING)
                this.printGameStarting();
            this.isExpertGame = game.isExpert();
            if(this.isExpertGame)
                this.expertParameters = new ExpertParameters(game.getExpertParameters());
            this.opponents = new LinkedList<>();
            for (int i = 0; i < game.getOpponents().size(); i++)
                this.opponents.add(new PlayerInfo(game.getOpponents().get(i)));
            this.me = new PlayerInfo(game.getMe());
            this.clouds = new ArrayList<>(game.getClouds());
            for (IslandCardDto islandCardDto : game.getIslands())
                updateIsland(islandCardDto);
            this.tableCoins = game.getTableCoins();
            this.characterCards = new ArrayList<>();
            for(CharacterCardDto messageCard: game.getCharacterCards())
                this.characterCards.add(new ViewCharacterCard(messageCard, true, false));
            checkCharacterCardActivable();
            this.currentPhase = game.getGamePhase();
            this.currentPlayer = game.getCurrentPlayer();
            this.areEnoughPlayers = game.areEnoughPlayersOnline();
            print();
            if (game.getCurrentPlayer().equals(getMe().getNickname())) {
                if(game.getGamePhase() == GamePhase.PLANNING) {
                    if(me.isCanPlayThisRound() && areEnoughPlayers)
                            askAssistantCard(getMe().getDeck());
                }
                else {
                    if(me.isCanPlayThisRound() && areEnoughPlayers)
                            askAction();
                }
            }
            if (!areEnoughPlayers)
                notEnoughPlayer();

    }

    /**
     * Modify the gamePhase
     */
    private void handleMessage(ChangedPhaseMessage message) {
        if((currentPhase == GamePhase.ACTION_END
                && message.getNewPhase() == GamePhase.PLANNING)
        || (currentPhase == GamePhase.ACTION_MOVE_STUDENTS
                && message.getNewPhase() == GamePhase.PLANNING)){
            me = me.withIsFromActualTurn(false);
            for(int i = 0; i < opponents.size(); i++){
                PlayerInfo opponent = opponents.get(i).withIsFromActualTurn(false);
                opponents.remove(i);
                opponents.add(i, opponent);
            }
        }
        currentPhase = message.getNewPhase();
        if (areEnoughPlayers)
            print();

        if (currentPlayer.equals(getMe().getNickname())
                && !message.getNewPhase().equals(GamePhase.ACTION_MOVE_STUDENTS)
                && me.isCanPlayThisRound() && areEnoughPlayers) {
                    askAction();
        }
        if(currentPhase.equals(GamePhase.ACTION_END)) resetCharacterCards();
    }

    /**
     * In the Action_End gamePhase, the characterCards on the table are set as not active
     */
    private void resetCharacterCards(){
        for(int i=0;i<getCharacterCards().size();i++){
            if(getCharacterCards().get(i).isActive()){
                ViewCharacterCard newCard = getCharacterCards().get(i);
                newCard = newCard.withIsActive(false);
                characterCards.remove(i);
                characterCards.add(i,newCard);
            }
        }
    }

    /**
     * Changed player
     */
    private void handleMessage(ChangedPlayerMessage message) {
        currentPlayer = message.getNickname();
        if (areEnoughPlayers)
            print();
        if (currentPlayer.equals(getMe().getNickname()) && me.isCanPlayThisRound() && areEnoughPlayers) {
                    askAction();
        }
    }

    /**
     * Error message is printed
     */
    private void handleMessage(ErrorMessage message) {
        if (message.getNickname().equals(getMe().getNickname())) {
            error = message.getError();
            error(message.getError());
            if(me.isCanPlayThisRound() && areEnoughPlayers)
                    askAction();
        }
    }

    /**
     * Depending on the gamePhase, asks the player to perform a determined action
     */
    private void askAction() {
        if (currentPhase.equals(GamePhase.ACTION_MOVE_STUDENTS)) askStudents();
        if (currentPhase.equals(GamePhase.ACTION_MOVE_MOTHER_NATURE)) askMotherNatureSteps();
        if (currentPhase.equals(GamePhase.ACTION_CHOOSE_CLOUD)) askCloud();
    }


    /**
     * The assistantCard played is removed from the deck of the currentPlayer
     */
    private void handleMessage(AssistantPlayedMessage message) {
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.with(Optional.of(message.getAssistantCard()));
            this.me = this.me.with(me.getDeck().remove(message.getAssistantCard()));
            this.me = this.me.withIsFromActualTurn(true);
        } else {
            Optional<Integer> opponentIndex = getOpponentIndex(message.getNickname());
            if (opponentIndex.isPresent()) {
                PlayerInfo opponent = opponents.get(opponentIndex.get());
                opponents.remove((int)opponentIndex.get());
                opponent = opponent.with(Optional.of(message.getAssistantCard()));
                opponent = opponent.with(opponent.getDeck().remove(message.getAssistantCard()));
                opponent = opponent.withIsFromActualTurn(true);
                opponents.add(opponentIndex.get(), opponent);
            }
        }
    }

    /**
     * The cloudTile corresponding to the modified cloudTile in the message is updated
     */
    private void handleMessage(CloudMessage message) {
        Optional<Integer> cloudIndex = getCloudIndex(message.getCloud().getUuid());
        if (cloudIndex.isPresent()) {
            clouds.remove((int) cloudIndex.get());
            clouds.add(cloudIndex.get(), message.getCloud());
            if (areEnoughPlayers)
                print();
        }
    }

    /**
     * The schoolBoard corresponding to the modified schoolBoard in the message is updated
     */
    private void handleMessage(SchoolBoardMessage message) {
        if(message.getSchoolBoard().getUuid().equals(getMe().getBoard().getUuid()))
            this.me = this.me.with(message.getSchoolBoard());
        else {
            Optional<Integer> opponentIndex = getOpponentIndex(message.getSchoolBoard().getUuid());
            if (opponentIndex.isPresent()) {
                PlayerInfo opponent = opponents.get(opponentIndex.get());
                opponents.remove((int) opponentIndex.get());
                opponents.add(opponentIndex.get(), opponent.with(message.getSchoolBoard()));
            }
        }
        if (areEnoughPlayers)
            print();
    }

    /**
     * The islandCard corresponding to the modified islandCard in the message is updated
     */
    private void handleMessage(IslandMessage message) {
        updateIsland(message.getIsland());
        if (areEnoughPlayers)
            print();
    }


    private void updateIsland(IslandCardDto newIsland) {
        int firstIsland = newIsland.getIndices().get(0);
        LinkedIslands linkedIsland = islands.get(firstIsland);
        linkedIsland.setIsland(linkedIsland.getIsland().withUuid(newIsland.getUuid()));
        linkedIsland.setIsland(linkedIsland.getIsland().withStudents(newIsland.getStudents()));
        linkedIsland.setIsland(linkedIsland.getIsland().withTower(newIsland.getTower()));
        linkedIsland.setIsland(linkedIsland.getIsland().withMotherNature(newIsland.isHasMotherNature()));
        linkedIsland.setIsMainIsland(true);

        for (Integer index : newIsland.getIndices()) {
            if (index != firstIsland) {
                linkedIsland = islands.get(index);
                linkedIsland.setIsland(linkedIsland.getIsland().withoutStudents());
                linkedIsland.setIsland(linkedIsland.getIsland().withTower(newIsland.getTower()));
                linkedIsland.setIsland(linkedIsland.getIsland().withMotherNature(false));
                linkedIsland.setIsMainIsland(false);
                linkedIsland.setConnectedWithNext(true);
                if (index == getLastIndex(newIsland.getIndices(), islands.size())) {
                    linkedIsland.setConnectedWithNext(false);
                }
            }
        }
    }

    private int getLastIndex(List<Integer> list, int dim) {
        if (list.size() == dim)
            return -1;
        int res = Math.floorMod(list.get(0), dim);
        while (list.contains(Math.floorMod(res + 1, dim)))
            res = Math.floorMod(res + 1, dim);
        return res;
    }

    /**
     * Updates the number of coins belonging to the table or to the player (based on the content of the message)
     */
    private void handleMessage(CoinMessage message) {
        if(!message.isOnTable()){
            if (message.getNickname().equals(me.getNickname())) {
                this.me = this.me.with(message.getCoins());
                checkCharacterCardActivable();
            } else {
                Optional<Integer> opponentIndex = getOpponentIndex(message.getNickname());
                if (opponentIndex.isPresent()) {
                    PlayerInfo opponent = opponents.get(opponentIndex.get());
                    opponents.remove((int)opponentIndex.get());
                    opponents.add(opponentIndex.get(), opponent.with(message.getCoins()));
                }
            }
        }
        else{
            this.tableCoins = message.getCoins();
        }
        if (areEnoughPlayers)
            print();
    }

    /**
     * for every character card, set if it can be activated or not
     */
    private void checkCharacterCardActivable(){
        if(expertParameters.isAlreadyActivateCharacterCard()){
            for(int i = 0; i < characterCards.size(); i++){
                ViewCharacterCard newCharacterCard = characterCards.get(i);
                newCharacterCard = newCharacterCard.withIsActivable(false);
                characterCards.remove(i);
                characterCards.add(i, newCharacterCard);
            }
        }
        else{
            for(int i = 0; i < characterCards.size(); i++){
                ViewCharacterCard newCharacterCard = characterCards.get(i);
                newCharacterCard = newCharacterCard.withIsActivable(me.getCoins() >= newCharacterCard.getPrice());
                characterCards.remove(i);
                characterCards.add(i, newCharacterCard);
            }
        }
    }

    /**
     * Updates the characterCard corresponding to the one within the message
     */
    private void handleMessage(CharacterCardMessage message) {
        CharacterCardDto newCharacterCard = message.getCharacterCard();
        for (int i = 0; i < characterCards.size(); i++) {
            if (characterCards.get(i).getCharacter() == newCharacterCard.getCharacter()) {
                ViewCharacterCard characterCard = characterCards.get(i);
                characterCard = characterCard.withPrice(newCharacterCard.getPrice());
                characterCard = characterCard.withStudents(newCharacterCard.getStudents());
                characterCard = characterCard.withHasCoin(newCharacterCard.hasCoin());
                characterCard = characterCard.withIsActive(true);
                characterCards.remove(i);
                characterCards.add(i, characterCard);
            }
        }
        checkCharacterCardActivable();
        if (areEnoughPlayers)
            print();
    }

    /**
     * Updates ExpertParameters
     */
    private void handleMessage(ExpertParametersMessage message) {
        ExpertParametersDto newParameters = message.getExpertParameters();
        expertParameters = expertParameters.withHasAlreadyActivateCharacterCard(newParameters.hasAlreadyActivateCharacterCard());
        expertParameters = expertParameters.withIsTakeProfessorEvenIfSameStudents(newParameters.isTakeProfessorEvenIfSameStudents());
        expertParameters = expertParameters.withMotherNatureExtraMovements(newParameters.getMotherNatureExtraMovements());
        expertParameters = expertParameters.withIsTowersCountInInfluence(newParameters.isTowersCountInInfluence());
        expertParameters = expertParameters.withExtraInfluence(newParameters.getExtraInfluence());
        expertParameters = expertParameters.withColorWithNoInfluence(newParameters.getColorWithNoInfluence());
        if (areEnoughPlayers)
            print();
        if (currentPlayer.equals(getMe().getNickname()) && currentPhase != GamePhase.ACTION_END && me.isCanPlayThisRound() && areEnoughPlayers) {
                    askAction();
        }
    }

    /**
     * Prints win,lose or draw based on the content of the message
     */
    private void handleMessage(GameOverMessage message) {
        if (message.getWinners().contains(me.getNickname())) {
            if (message.getWinners().size() == 1)
                win();
            else
                draw(message.getWinners().stream().filter(w -> !w.equals(me.getNickname())).findFirst().get());
        } else
            lose(message.getWinners());
        askGameSettings();
    }

    private void handleMessage(PlayerCanPlayMessage message) {
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.withCanPlayThisRound(message.canPlay());
            checkCharacterCardActivable();
        } else {
            Optional<Integer> opponentIndex = getOpponentIndex(message.getNickname());
            if (opponentIndex.isPresent()) {
                PlayerInfo opponent = opponents.get(opponentIndex.get());
                opponents.remove((int)opponentIndex.get());
                opponents.add(opponentIndex.get(), opponent.withCanPlayThisRound(message.canPlay()));
            }
        }
    }

    /**
     * Update the connection state
     */
    private void handleMessage(ConnectionMessage message) {
        switch (message.getType()) {
            case IS_ONLINE -> playerOnline(message.getNickname(), true);
            case IS_OFFLINE -> playerOnline(message.getNickname(), false);
            case NOT_ENOUGH_PLAYERS -> enoughPlayers(false);
            case ENOUGH_PLAYERS -> enoughPlayers(true);
            case GAME_OVER_FROM_DISCONNECTION -> gameOverFromDisconnection();
        }
    }

    /**
     * Update the connection state of a player
     */
    private void playerOnline(String nickname, boolean isOnline) {
        Optional<Integer> playerIndex = getOpponentIndex(nickname);
        if (playerIndex.isPresent()) {
            PlayerInfo opponent = opponents.get(playerIndex.get());
            opponents.remove((int)playerIndex.get());
            opponents.add(playerIndex.get(), opponent.with(isOnline));
            if (areEnoughPlayers)
                print();
        }
    }

    private void enoughPlayers(boolean areEnoughPlayers) {
        if (areEnoughPlayers) {
            print();
            if (currentPlayer.equals(getMe().getNickname()) && me.isCanPlayThisRound()) {
                    if(currentPhase == GamePhase.PLANNING)
                        this.askAssistantCard(getDeck());
                    else
                        askAction();
            }
        }
        else {
            if(this.areEnoughPlayers)
                notEnoughPlayer();
        }
        this.areEnoughPlayers = areEnoughPlayers;
    }

    private void gameOverFromDisconnection(){
        printGameOverFromDisconnection();
        askGameSettings();
    }


    //</editor-fold>

    //<editor-fold desc="Presentation logic">

    public void start() {
        this.printWelcomeMessage();
        this.askServerInfo();
    }

    /**
     * Creates a new socket bound to the chosen ip and port
     */
    public final void startConnection(String ipAddress, int port) {
        try {
            Socket s = new Socket(ipAddress, port);
            System.out.println("PORT: "+s.getPort());
            this.endpoint = new Endpoint(s, false);
            this.endpoint.addMessageListener(this);
            this.endpoint.addDisconnectionListener(this);
            this.endpoint.startReceiving();
            this.askPlayerNickname();
        } catch (IOException e) {
            this.errorAndExit(ErrorMessages.NO_NETWORK);
        }
    }

    /**
     * Sends the server the chosen nickname
     */
    public final void sendPlayerNickname(String nickname) {
        Message message = new NicknameProposalMessage(nickname);
        endpoint.sendMessage(message);
    }

    /**
     * Sends the server the gameMode and player's Number chosen by the player
     */
    protected final void sendGameSettings(PlayersNumber playersNumber, GameMode gameMode) {
        Message message = new GametypeRequestMessage(
                me.getNickname(),
                gameMode, playersNumber);
        endpoint.sendMessage(message);
        this.isExpertGame = (gameMode == GameMode.EXPERT_MODE);
    }

    /**
     * Checks if the number of motherNature steps is valid
     * If it is, then it sends the server the number of motherNature steps
     * @param steps mother nature steps
     * @return false if the number is not valid, true otherwise
     */
    protected final boolean sendMotherNatureSteps(int steps) {
        if(steps<=0) return false;
        if ((steps > getMe().getDiscardPileHead().get().motherNatureMovement() + expertParameters.getMotherNatureExtraMovements()) && !isExpertGame)
            return false;

        Message message = new MoveMotherNatureMessage(me.getNickname(), steps);
        endpoint.sendMessage(message);
        return true;
    }

    /**
     * Checks if the characterCard's index is valid and if it was not played by another player
     * If everything is ok, its sent to the server
     */
    protected final boolean sendAssistantCard(int cardIndex) {
        if (cardIndex < 0 || cardIndex >= me.getDeck().size()) {
            return false;
        }
        if(!canPlayAssistant(getMe().getDeck().get(cardIndex)))
            return false;
        AssistantCard assistantCard = me.getDeck().get(cardIndex);
        Message message = new AssistantPlayedMessage(me.getNickname(), MessageType.ACTION_PLAY_ASSISTANT, assistantCard);
        endpoint.sendMessage(message);
        return true;
    }

    /**
     *
     * @return true if the assistant played was not played by another player
     */
    private boolean canPlayAssistant(AssistantCard assistant) {
        //If assistant is different from every other played assistantCard
        if (isAssistantDifferentFromOthers(assistant)) return true;

        //If assistant is equal to another played assistantCard, check if in the player's deck exist at least one card
        // different from every other one
        for (AssistantCard ac : me.getDeck()) {
            if (isAssistantDifferentFromOthers(ac)) return false;
        }
        return true;
    }

    //Returns true if assistant is different from every other assistants already played in this turn
    private boolean isAssistantDifferentFromOthers(AssistantCard assistant) {
        for (PlayerInfo p : opponents) {
            if(p.getDiscardPileHead().isPresent() && p.isFromActualTurn())
                if (p.getDiscardPileHead().get().equals(assistant))
                    return false;
        }
        return true;
    }

    /**
     * Sends the student to move on the schoolBoard to the server
     * @return true if the pawnColor selected is in the entrance and its dining row is not full
     */
    protected final boolean sendStudentMoveOnBoard(PawnColor student) {
        if(me.getBoard().getStudentsInEntrance(student) < 1)
            return false;
        if(me.getBoard().getDiningRoom().getStudentsInDiningRoom(student) > 9)
            return false;
        Message message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        endpoint.sendMessage(message);
        return true;
    }

    /**
     * Sends the student and the islandCard
     * @return true if the parameters are valid
     */
    protected final boolean sendStudentMoveOnIsland(PawnColor student, int islandIndex) {
        if(me.getBoard().getStudentsInEntrance(student) < 1)
            return false;
        MoveStudentMessage message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, student);
        message.setIslandCard(getIslands().get(getMainIsland(islandIndex)).getIsland());
        endpoint.sendMessage(message);
        return true;
    }

    /**
     * @param index the index chose by the player
     * @return the correct index of an island, excluding the one who are connected
     */
    protected int getMainIsland(int index){
        int count=0;
        int i=0;
        while(index!=count){
            if(!getIslands().get(i).isConnectedWithNext())
                count++;
            i++;
        }
        while(getIslands().get(i).isConnectedWithNext()) i++;
        return i;
    }

    /**
     * Sends the cloudTile
     * @return true if the cloudIndex is valid and the chosen cloudTile is not already empty
     */
    protected final boolean sendCloudChoice(int cloudIndex) {
        if (cloudIndex < 0 || cloudIndex >= getClouds().size())
            return false;
        CloudTileDto cloudTile = getClouds().get(cloudIndex);
        if(cloudTile.getStudents().isEmpty())
            return false;
        Message message = new CloudMessage(me.getNickname(), MessageType.ACTION_CHOOSE_CLOUD, cloudTile);
        endpoint.sendMessage(message);
        return true;
    }

    /**  parameters:
     *   CHARACTER_ONE:      {PawnColor colorFromCard, island UUID}
     *   CHARACTER_SEVEN:    {List<PawnColor> colorsFromCard, List<PawnColor> colorsFromEntrance}
     *   CHARACTER_NINE:     {PawnColor}
     *   CHARACTER_ELEVEN:   {PawnColor colorFromCard}
     *   NB parameters length must be exactly the size required for the specific character
     * */
    protected final boolean sendCharacterCard(int characterIndex, Object[] parameters) {
        if (characterIndex < 0 || characterIndex >= characterCards.size())
            return false;
        if (!areCharacterParametersOk(getCharacterCards().get(characterIndex), parameters))
            return false;
        if(!getCharacterCards().get(characterIndex).isActivable())
            return false;
        if (getMe().getCoins()<getCharacterCards().get(characterIndex).getPrice())
            return false;
        Message message = new CharacterCardMessage(me.getNickname(), MessageType.ACTION_USE_CHARACTER, getCharacterCards().get(characterIndex), parameters);
        endpoint.sendMessage(message);
        return true;
    }

    private boolean areCharacterParametersOk(ViewCharacterCard characterCard, Object[] parameters){
        return switch (characterCard.getCharacter()){
            case CHARACTER_ONE -> CharacterCardHelper.areParametersOkCharacter1(characterCard, parameters, getIslands());
            case CHARACTER_SEVEN -> CharacterCardHelper.areParametersOkCharacter7(characterCard, parameters, me);
            case CHARACTER_NINE -> CharacterCardHelper.areParametersOkCharacter9(parameters);
            case CHARACTER_ELEVEN -> CharacterCardHelper.areParametersOkCharacter11(characterCard, parameters, me);
            default -> true;
        };
    }

    protected boolean areCharacterActive(){
        return expertParameters.isAlreadyActivateCharacterCard();
    }

    protected boolean canActivateCharacter(ViewCharacterCard characterCard){
        if (expertParameters.isAlreadyActivateCharacterCard())
            return false;
        return me.getCoins() >= characterCard.getPrice();
    }

    protected boolean canActivateCharacter(int characterIndex){
        if(characterIndex < 0 || characterIndex >= getCharacterCards().size())
            return false;
        return canActivateCharacter(getCharacterCards().get(characterIndex));
    }
    //</editor-fold>

    @Override
    public void onDisconnect(Object disconnected){
        this.errorAndExit(ErrorMessages.DISCONNECTION);
    }
}
