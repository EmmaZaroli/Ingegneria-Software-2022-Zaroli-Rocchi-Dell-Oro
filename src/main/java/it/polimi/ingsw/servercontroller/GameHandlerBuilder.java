package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.gamecontroller.ExpertGameController;
import it.polimi.ingsw.gamecontroller.ExpertTableController;
import it.polimi.ingsw.gamecontroller.GameController;
import it.polimi.ingsw.gamecontroller.TableController;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.gamecontroller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizard;
import it.polimi.ingsw.view.VirtualView;

import java.util.LinkedList;
import java.util.List;

/**
 * Game Handler Builder
 * Builds the game and assigns it to a gameHandler
 */
public class GameHandlerBuilder {
    private GameMode gameMode = GameMode.NORMAL_MODE;   //if no GameMode is specified, the default mode will be normal
    private PlayersNumber playersNumber = PlayersNumber.THREE;  //if no player number is specified, the default number will be three
    private final List<User> users = new LinkedList<>();

    private final List<GameReadyListener> gameStartingListeners = new LinkedList<>();

    /**
     * Assigns the gameMode
     * @param gameMode the gameMode
     * @return this instance, updated
     */
    public GameHandlerBuilder gameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    /**
     * Assigns the number of players
     * @param playersNumber the number of players
     * @return this instance, updated
     */
    public GameHandlerBuilder playersNumber(PlayersNumber playersNumber) {
        this.playersNumber = playersNumber;
        return this;
    }

    /**
     * Adds a user to the list of users
     * @param user the user to add
     * @return this instance, updated
     */
    public GameHandlerBuilder player(User user) {
        this.users.add(user);
        return this;
    }

    /**
     * Removes a user to the list of users
     * @param user the user to remove
     * @return this instance, updated
     */
    public GameHandlerBuilder removePlayer(User user) {
        this.users.remove(user);
        return this;
    }

    /**
     * Resets the lists of this instance
     * @return this instance, updated
     */
    public GameHandlerBuilder reset() {
        this.users.clear();
        this.gameStartingListeners.clear();
        return this;
    }

    /**
     *
     * @return true if the number of users already enqueued is equals to the number of players needed for the game
     */
    public boolean isGameFull() {
        return users.size() == playersNumber.getPlayersNumber();
    }

    /**
     * Builds the gameHandler
     * @return the gameHandler
     * @throws InvalidPlayerNumberException  Signals that in GameControllerBuilder the specified PlayerCount does not match the actual number of player passed to the builder
     */
    public GameHandler build() throws InvalidPlayerNumberException {
        checkPlayerNumberValidity();
        return buildGameHandler();
    }

    /**
     * Used for loaded a previously closed game
     * @param game the game
     * @return the gameHandler
     * @throws InvalidPlayerNumberException if the number of users already enqueued is not equals to the number of players needed for the game
     */
    public GameHandler load(Game game) throws InvalidPlayerNumberException {
        checkPlayerNumberValidity();
        return buildGameHandler(game);
    }

    /**
     * Checks if the number of users already enqueued is equals to the number of players needed for the game
     * @throws InvalidPlayerNumberException if the number of users already enqueued is not equals to the number of players needed for the game
     */
    private void checkPlayerNumberValidity() throws InvalidPlayerNumberException {
        if (users.size() != playersNumber.getPlayersNumber())
            throw new InvalidPlayerNumberException((users.size() > playersNumber.getPlayersNumber()) ? InvalidPlayerNumberException.Reason.TOO_MANY_PLAYERS : InvalidPlayerNumberException.Reason.NOT_ENOUGH_PLAYERS);
    }

    /**
     * Builds a new Game
     * @return the gameHandler
     */
    private GameHandler buildGameHandler() {
        Game gameModel = buildGameModel();
        VirtualView[] virtualViews = buildVirtualViews(gameModel);
        GameController gameController = buildGameController(gameModel, virtualViews);
        gameController.init();
        setListeners(gameModel, gameController, virtualViews);
        notifyGameReady();
        return new GameHandler(new LinkedList<>(users), gameController, gameModel, virtualViews);
    }

    /**
     * Loads a previously closed game
     * @param gameModel the previous game
     * @return the gameHandler
     */
    private GameHandler buildGameHandler(Game gameModel) {
        VirtualView[] virtualViews = buildVirtualViews(gameModel);
        GameController gameController = buildGameController(gameModel, virtualViews);
        gameController.setObservers();
        setListeners(gameModel, gameController, virtualViews);
        notifyGameReady();
        return new GameHandler(new LinkedList<>(users), gameController, gameModel, virtualViews);
    }

    /**
     * Adds the disconnectionListeners
     * @param gameModel the game
     * @param gameController the gameController
     * @param virtualViews the virtualViews
     */
    private void setListeners(Game gameModel, GameController gameController, VirtualView[] virtualViews){
        for (VirtualView virtualView : virtualViews) {
            if (virtualView.getClientHandler().isPresent()) {
                virtualView.getClientHandler().get().addDisconnectionListener(gameController);
            }
            virtualView.addListener(gameController);
        }
    }

    /**
     * Builds the game based on the gameMode chose
     * @return the game
     */
    private Game buildGameModel() {
        return switch (gameMode) {
            case NORMAL_MODE -> buildNormalGameModel();
            case EXPERT_MODE -> buildExpertGameModel();
        };
    }

    /**
     * Builds the Normal game
     * @return the game
     */
    private Game buildNormalGameModel() {
        Player[] players = new Player[playersNumber.getPlayersNumber()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(users.get(i).getNickname(), Wizard.values()[i], Tower.getValidValues()[i], playersNumber.getPlayersNumber());
        }

        Table table = new Table(playersNumber);

        GameParameters parameters = new GameParameters(playersNumber, gameMode);

        return new Game(players, table, parameters);
    }

    /**
     * Builds the Expert game
     * @return the game
     */
    private Game buildExpertGameModel() {
        ExpertPlayer[] players = new ExpertPlayer[playersNumber.getPlayersNumber()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new ExpertPlayer(users.get(i).getNickname(), Wizard.values()[i], Tower.getValidValues()[i], playersNumber.getPlayersNumber());
        }

        ExpertTable table = new ExpertTable(playersNumber);

        ExpertGameParameters parameters = new ExpertGameParameters(playersNumber, gameMode);

        return new ExpertGame(players, table, parameters);
    }

    /**
     * Builds the gameController based on the gameMode chose
     * @param gameModel the game
     * @param virtualViews the virtualViews
     * @return the gameController
     */
    private GameController buildGameController(Game gameModel, VirtualView[] virtualViews) {
        GameController gameController = switch (gameMode) {
            case NORMAL_MODE -> buildNormalGameController(gameModel, virtualViews);
            case EXPERT_MODE -> buildExpertGameController(gameModel, virtualViews);
        };
        return gameController;
    }

    /**
     * Builds the Normal gameController
     * @param gameModel the game
     * @param virtualViews the virtualViews
     * @return the gameController
     */
    private GameController buildNormalGameController(Game gameModel, VirtualView[] virtualViews) {
        TableController tableController = new TableController(gameModel.getTable(), gameModel.getParameters());
        return new GameController(gameModel, tableController, virtualViews);
    }

    /**
     * Builds the Expert gameController
     * @param gameModel the game
     * @param virtualViews the virtualViews
     * @return the expertGameController
     */
    private ExpertGameController buildExpertGameController(Game gameModel, VirtualView[] virtualViews) {
        ExpertTableController tableController = new ExpertTableController((ExpertTable) gameModel.getTable(), (ExpertGameParameters) gameModel.getParameters());
        return new ExpertGameController((ExpertGame) gameModel, tableController, virtualViews);
    }

    /**
     * Builds the virtualViews for every user
     * @param gameModel the game
     * @return the virtualViews
     */
    private VirtualView[] buildVirtualViews(Game gameModel) {
        VirtualView[] virtualViews = new VirtualView[playersNumber.getPlayersNumber()];
        for (int i = 0; i < virtualViews.length; i++) {
            virtualViews[i] = new VirtualView(users.get(i), gameModel);
        }
        return virtualViews;
    }

    /**
     * Adds a gameReadyListener
     * @param l the gameReadyListener
     */
    public void addGameStartingListener(GameReadyListener l) {
        this.gameStartingListeners.add(l);
    }

    /**
     * remove a gameReadyListener
     * @param l the gameReadyListener
     */
    public void removeGameStartingListener(GameReadyListener l) {
        this.gameStartingListeners.remove(l);
    }

    /**
     * Notifies the gameReadyListeners that the game is ready
     */
    private void notifyGameReady() {
        List<GameReadyListener> temporaryCopy = new LinkedList<>(gameStartingListeners);
        temporaryCopy.forEach(GameReadyListener::onGameReady);
    }
}
