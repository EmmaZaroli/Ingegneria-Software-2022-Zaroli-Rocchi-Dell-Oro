package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.dtos.CloudTileDto;
import it.polimi.ingsw.dtos.GameDto;
import it.polimi.ingsw.dtos.IslandCardDto;
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

import javax.swing.*;
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
    private List<CharacterCardDto> characterCards;
    protected int numberOfIslandOnTable;
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
            this.islands.add(new LinkedIslands(new IslandCardDto()));
        this.tableCoins = 0;
        this.characterCards = new ArrayList<>();
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

    public List<CharacterCardDto> getCharacterCards() {
        return this.characterCards;
    }

    public boolean areEnoughPlayers() {
        return areEnoughPlayers;
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

    public GamePhase getCurrentPhase() {
        return this.currentPhase;
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
        if (message instanceof GetDeckMessage getDeckMessage) handleMessage(getDeckMessage);
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

    private void handleMessage(GetDeckMessage message) {
        if (message.getNickname().equals(getMe().getNickname())) {
            me = me.with(message.getDeck());
            this.askAssistantCard(message.getDeck());
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
            GameDto game = message.getGame();
            this.printGameStarting();
            for (int i = 0; i < game.getOpponents().size(); i++)
                this.opponents.add(new PlayerInfo(game.getOpponents().get(i)).with(game.getOpponentsBoard().get(i)));
            this.me = new PlayerInfo(game.getMe()).with(game.getSchoolBoard());
            this.clouds = new ArrayList<>(game.getClouds());
            for (IslandCardDto islandCardDto : game.getIslands())

                updateIsland(islandCardDto);
            this.tableCoins = game.getTableCoins();
            this.characterCards = game.getCharacterCards();
            print();
            this.changePhase(game.getGamePhase());

            updateCurrentPlayersTurn(game.getCurrentPlayer());
            if (game.getCurrentPlayer().equals(getMe().getNickname())) {
                //TODO may not be planning phase
                me = me.with(message.getDeckFirstPlayer());
                this.askAssistantCard(game.getCurrentPlayerDeck());
            }

    }

    private void handleMessage(ChangedPhaseMessage message) {
        currentPhase = message.getNewPhase();
    }

    private void handleMessage(ChangedPlayerMessage message) {
        print();
        currentPlayer = message.getNickname();
        updateCurrentPlayersTurn(message.getNickname());
        if (currentPlayer.equals(getMe().getNickname())) askAction();
    }

    private void handleMessage(ErrorMessage message) {
        if (message.getNickname().equals(getMe().getNickname())) {
            error = message.getError();
            error(message.getError());
            askAction();
        }
    }

    private void askAction() {
        if (currentPhase.equals(GamePhase.ACTION_MOVE_STUDENTS)) askStudents();
        if (currentPhase.equals(GamePhase.ACTION_MOVE_MOTHER_NATURE)) askMotherNatureSteps();
        if (currentPhase.equals(GamePhase.ACTION_CHOOSE_CLOUD)) askCloud();

    }


    private void handleMessage(AssistantPlayedMessage message) {
        if (message.getNickname().equals(me.getNickname())) {
            this.me = this.me.with(message.getAssistantCard());
            this.me = this.me.with(me.getDeck().remove(message.getAssistantCard()));
        } else {
            Optional<Integer> opponentIndex = getOpponentIndex(message.getNickname());
            if (opponentIndex.isPresent()) {
                PlayerInfo opponent = opponents.get(opponentIndex.get());
                opponents.remove(opponentIndex.get());
                opponent = opponent.with(message.getAssistantCard());
                opponent = opponent.with(opponent.getDeck().remove(message.getAssistantCard()));
                opponents.add(opponentIndex.get(), opponent);
            }
        }
    }

    private void handleMessage(CloudMessage message) {
        Optional<Integer> cloudIndex = getCloudIndex(message.getCloud().getUuid());
        if (cloudIndex.isPresent()) {
            clouds.remove((int) cloudIndex.get());
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
                opponents.remove((int) opponentIndex.get());
                opponents.add(opponentIndex.get(), opponent.with(message.getSchoolBoard()));
            }
        }
        this.print();
        if(message.getNickname().equals(me.getNickname()))
            askAction();
    }

    private void handleMessage(IslandMessage message) {
        updateIsland(message.getIsland());

        this.print();
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

        this.print();
    }

    private void handleMessage(CharacterCardMessage message) {
        CharacterCardDto newCharacterCard = message.getCharacterCard();
        for(int i = 0; i < characterCards.size(); i++){
            if(characterCards.get(i).getCharacter() == newCharacterCard.getCharacter()){
                CharacterCardDto characterCard = characterCards.get(i);
                characterCard = characterCard.with(newCharacterCard.getPrice());
                characterCard = characterCard.with(newCharacterCard.getStudents());
                characterCards.remove(i);
                characterCards.add(i, characterCard);
            }
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
            opponents.remove(playerIndex.get());
            opponents.add(playerIndex.get(), opponent.with(isOnline));
            print();
        }
    }

    private void enoughPlayers(boolean areEnoughPlayers) {
        this.areEnoughPlayers = areEnoughPlayers;
        if (areEnoughPlayers)
            print();
        else
            notEnoughPlayer();
    }


    //</editor-fold>

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
        if (steps < 0)
            return false;
        Message message = new MoveMotherNatureMessage(me.getNickname(), steps);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendAssistantCard(int cardIndex) {
        if (cardIndex < 0 || cardIndex >= me.getDeck().size()) {
            return false;
        }
        AssistantCard assistantCard = me.getDeck().get(cardIndex);
        Message message = new AssistantPlayedMessage(me.getNickname(), MessageType.ACTION_PLAY_ASSISTANT, assistantCard);
        endpoint.sendMessage(message);
        return true;
    }


    protected final boolean sendStudentMoveOnBoard(PawnColor student) {
        Message message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendStudentMoveOnIsland(PawnColor student, int islandIndex) {
        MoveStudentMessage message = new MoveStudentMessage(me.getNickname(), MessageType.ACTION_MOVE_STUDENTS_ON_BOARD, student);
        message.setIslandCard(getIslands().get(islandIndex).getIsland());
        endpoint.sendMessage(message);
        return true;
    }

    protected final boolean sendCloudChoice(int cloudIndex) {
        if (cloudIndex < 0 || cloudIndex >= getClouds().size())
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
        if (characterIndex < 0 || characterIndex >= characterCards.size())
            return false;
        if (!areCharacterParametersOk(getCharacterCards().get(characterIndex), parameters))
            return false;
        Message message = new CharacterCardMessage(me.getNickname(), MessageType.ACTION_USE_CHARACTER, getCharacterCards().get(characterIndex), parameters);
        endpoint.sendMessage(message);
        return true;
    }

    private boolean areCharacterParametersOk(CharacterCardDto characterCard, Object[] parameters){
        return switch (characterCard.getCharacter()){


            case CHARACTER_ONE -> areParametersOkCharacter1(characterCard, parameters);
            case CHARACTER_SEVEN -> areParametersOkCharacter7(characterCard, parameters);
            case CHARACTER_NINE -> areParametersOkCharacter9(parameters);
            case CHARACTER_ELEVEN -> areParametersOkCharacter11(characterCard, parameters);
            default -> true;
        };
    }

    private boolean areParametersOkCharacter1(CharacterCardDto card, Object[] parameters){
        if(parameters.length != 2)

            return false;
        if (!(parameters[0] instanceof PawnColor && parameters[1] instanceof UUID))
            return false;
        if(!(card.isWithSetUpAction()))
            return false;
        if(!(card.getStudents().contains(parameters[0])))

            return false;


        for (LinkedIslands island : getIslands()) {
            if (island.getIsland().getUuid().equals(parameters[1]))
                return true;
        }
        return false;
    }

    private boolean areParametersOkCharacter7(CharacterCardDto card, Object[] parameters){
        if(parameters.length != 2)

            return false;
        if (!(parameters[0] instanceof List<?> && parameters[1] instanceof List<?>))
            return false;
        if(!(card.isWithSetUpAction()))


            return false;
        List<PawnColor> colorsFromCard = (List<PawnColor>) parameters[0];
        List<PawnColor> colorsFromEntrance = (List<PawnColor>) parameters[1];
        Map<PawnColor, Integer> cardinalityCard = card.getStudentsCardinality();
        Map<PawnColor, Integer> cardinalityEntrance = me.getBoard().getStudentsInEntranceCardinality();
        for (PawnColor color : PawnColor.values()) {
            if (colorsFromCard.stream().filter(x -> x == color).count() > cardinalityCard.get(color))
                return false;
        }
        for (PawnColor color : PawnColor.values()) {
            if (colorsFromEntrance.stream().filter(x -> x == color).count() > cardinalityEntrance.get(color))
                return false;
        }
        return true;
    }

    private boolean areParametersOkCharacter9(Object[] parameters) {
        return (parameters.length == 1) && (parameters[0] instanceof PawnColor);
    }

    private boolean areParametersOkCharacter11(CharacterCardDto card, Object[] parameters){
        if(parameters.length != 1)

            return false;
        if (!(parameters[0] instanceof PawnColor))
            return false;
        if(!(card.isWithSetUpAction()))


            return false;
        return card.getStudents().contains(parameters[0]);
    }
    //</editor-fold>
}
