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
    private List<ViewCharacterCard> characterCards;
    private ExpertParameters expertParameters;
    private GamePhase currentPhase;
    private boolean areEnoughPlayers;
    private String error;
    private Endpoint endpoint;

    protected View() {
        this.opponents = new LinkedList<>();
        this.me = new PlayerInfo();
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            this.islands.add(new LinkedIslands((new IslandCardDto()).withIndeces(i)));
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

    private void handleMessage(MoveStudentMessage message){
        if(message.getNickname().equals(getMe().getNickname())) {
            if(me.isCanPlayThisRound())
                if(areEnoughPlayers)
                    askAction();
        }
    }

    private void handleMessage(GetDeckMessage message) {
        if (message.getNickname().equals(getMe().getNickname())) {
            me = me.with(message.getDeck());
            this.currentPhase = GamePhase.PLANNING;
            if(me.isCanPlayThisRound())
                if(areEnoughPlayers)
                    this.askAssistantCard(message.getDeck());
        }
    }

    private void handleMessage(GametypeResponseMessage message) {
        if (message.isOk()) {
            this.printEnqueuedMessage();
        } else {
            this.error = ErrorMessages.PARAMETERS_ERROR;
            error(ErrorMessages.PARAMETERS_ERROR);
            this.askServerInfo();
        }
    }

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
                this.characterCards.add(new ViewCharacterCard(messageCard, true, false)); //TODO check this
            checkCharacterCardActivable();
            this.currentPhase = game.getGamePhase();
            this.currentPlayer = game.getCurrentPlayer();
            this.areEnoughPlayers = game.areEnoughPlayersOnline();
            print();
            if (game.getCurrentPlayer().equals(getMe().getNickname())) {
                if(game.getGamePhase() == GamePhase.PLANNING) {
                    if(me.isCanPlayThisRound())
                        if(areEnoughPlayers)
                            askAssistantCard(getMe().getDeck());
                }
                else {
                    if(me.isCanPlayThisRound())
                        if(areEnoughPlayers)
                            askAction();
                }
            }
            if (!areEnoughPlayers)
                notEnoughPlayer();

    }

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
                && !message.getNewPhase().equals(GamePhase.ACTION_MOVE_STUDENTS)) {
            if(me.isCanPlayThisRound())
                if(areEnoughPlayers)
                    askAction();
        }
        if(currentPhase.equals(GamePhase.ACTION_END)) resetCharacterCards();
    }

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

    private void handleMessage(ChangedPlayerMessage message) {
        currentPlayer = message.getNickname();
        if (areEnoughPlayers)
            print();
        if (currentPlayer.equals(getMe().getNickname())) {
            if(me.isCanPlayThisRound())
                if(areEnoughPlayers)
                    askAction();
        }
    }

    private void handleMessage(ErrorMessage message) {
        if (message.getNickname().equals(getMe().getNickname())) {
            error = message.getError();
            error(message.getError());
            if(me.isCanPlayThisRound())
                if(areEnoughPlayers)
                    askAction();;
        }
    }

    private void askAction() {
        if (currentPhase.equals(GamePhase.ACTION_MOVE_STUDENTS)) askStudents();
        if (currentPhase.equals(GamePhase.ACTION_MOVE_MOTHER_NATURE)) askMotherNatureSteps();
        if (currentPhase.equals(GamePhase.ACTION_CHOOSE_CLOUD)) askCloud();
    }


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

    private void handleMessage(CloudMessage message) {
        Optional<Integer> cloudIndex = getCloudIndex(message.getCloud().getUuid());
        if (cloudIndex.isPresent()) {
            clouds.remove((int) cloudIndex.get());
            clouds.add(cloudIndex.get(), message.getCloud());
            if (areEnoughPlayers)
                print();
        }
    }

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

    //for every character card, set if it can be activated or not
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

    private void handleMessage(ExpertParametersMessage message) {
        ExpertParametersDto newParameters = message.getExpertParameters();
        expertParameters = expertParameters.withHasAlreadyActivateCharacterCard(newParameters.hasAlreadyActivateCharacterCard());
        expertParameters = expertParameters.withIsTakeProfessorEvenIfSameStudents(newParameters.isTakeProfessorEvenIfSameStudents());
        expertParameters = expertParameters.withmotherNatureExtraMovements(newParameters.getMotherNatureExtraMovements());
        expertParameters = expertParameters.withIsTowersCountInInfluence(newParameters.isTowersCountInInfluence());
        expertParameters = expertParameters.withExtraInfluence(newParameters.getExtraInfluence());
        expertParameters = expertParameters.withColorWithNoInfluence(newParameters.getColorWithNoInfluence());
        if (areEnoughPlayers)
            print();
        if (currentPlayer.equals(getMe().getNickname()) && currentPhase != GamePhase.ACTION_END) {
            if(me.isCanPlayThisRound())
                if(areEnoughPlayers)
                    askAction();
        }
    }

    private void handleMessage(GameOverMessage message) {
        if (message.getWinners().contains(me.getNickname())) {
            if (message.getWinners().size() == 1)
                win();
            else
                draw(message.getWinners().stream().filter(w -> !w.equals(me.getNickname())).findFirst().get());
        } else
            lose(message.getWinners());
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

    private void handleMessage(ConnectionMessage message) {
        switch (message.getType()) {
            case IS_ONLINE -> playerOnline(message.getNickname(), true);
            case IS_OFFLINE -> playerOnline(message.getNickname(), false);
            case NOT_ENOUGH_PLAYERS -> enoughPlayers(false);
            case ENOUGH_PLAYERS -> enoughPlayers(true);
            case GAME_OVER_FROM_DISCONNECTION -> gameOverFromDisconnection();
        }
    }

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
        this.areEnoughPlayers = areEnoughPlayers;
        if (areEnoughPlayers) {
            print();
            if (currentPlayer.equals(getMe().getNickname())) {
                if(me.isCanPlayThisRound()){
                    if(currentPhase == GamePhase.PLANNING)
                        this.askAssistantCard(getDeck());
                    else
                        askAction();
                }
            }
        }
        else
            notEnoughPlayer();
    }


    //</editor-fold>

    //<editor-fold desc="Presentation logic">
    public void start() {
        this.printWelcomeMessage();
        this.askServerInfo();
    }

    public final void startConnection(String ipAddress, int port) {
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

    public final boolean sendPlayerNickname(String nickname) {
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
        if(steps<=0) return false;
        if ((steps > getMe().getDiscardPileHead().get().motherNatureMovement() + expertParameters.getMotherNatureExtraMovements()) && !isExpertGame)
            return false;

        Message message = new MoveMotherNatureMessage(me.getNickname(), steps);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendAssistantCard(int cardIndex) {
        if (cardIndex < 0 || cardIndex >= me.getDeck().size()) {
            return false;
        }
        /*
        for(PlayerInfo opponent : getOpponents()){
            if(opponent.getDiscardPileHead().isPresent() && opponent.isFromActualTurn() && opponent.getDiscardPileHead().get().value() == getMe().getDeck().get(cardIndex).value() && getMe().getDeck().size()!=1)
                return false;
        }*/
        if(!canPlayAssistant(getMe().getDeck().get(cardIndex)))
            return false;
        AssistantCard assistantCard = me.getDeck().get(cardIndex);
        Message message = new AssistantPlayedMessage(me.getNickname(), MessageType.ACTION_PLAY_ASSISTANT, assistantCard);
        endpoint.sendMessage(message);
        return true;
    }

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

    protected final boolean sendStudentMoveOnBoard(PawnColor student) {
        Message message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendStudentMoveOnIsland(PawnColor student, int islandIndex) {
        MoveStudentMessage message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_ISLAND, student);
        message.setIslandCard(getIslands().get(getMainIsland(islandIndex)).getIsland());
        endpoint.sendMessage(message);
        return true;
    }

    //TODO check if this works
    private int getMainIsland(int index){
        int count=0;
        int i=0;
        while(index!=count){
            if(!getIslands().get(i).isConnectedWithNext())
                count++;
            i++;
        }
        return i;
    }

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

    /*  parameters:
     *   CHARACTER_ONE:      {PawnColor colorFromCard, island UUID}
     *   CHARACTER_SEVEN:    {List<PawnColor> colorsFromCard, List<PawnColor> colorsFromEntrance}
     *   CHARACTER_NINE:     {PawnColor}
     *   CHARACTER_ELEVEN:   {PawnColor colorFromCard}
     *   NB parameters.lenght must be exactly the size required for the specific character
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
            case CHARACTER_ELEVEN -> CharacterCardHelper.areParametersOkCharacter11(characterCard, parameters);
            default -> true;
        };
    }

    protected boolean areCharacterActive(){
        if (expertParameters.isAlreadyActivateCharacterCard())
            return true;
        return false;
    }

    protected boolean canActivateCharacter(ViewCharacterCard characterCard){
        if (expertParameters.isAlreadyActivateCharacterCard())
            return false;
        if(me.getCoins() < characterCard.getPrice())
            return false;
        return true;
    }

    protected boolean canActivateCharacter(int characterInedx){
        if(characterInedx < 0 || characterInedx >= getCharacterCards().size())
            return false;
        return canActivateCharacter(getCharacterCards().get(characterInedx));
    }
    //</editor-fold>
}
