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
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.view.VirtualView;

import java.util.LinkedList;
import java.util.List;

public class GameHandlerBuilder {
    private GameMode gameMode = GameMode.NORMAL_MODE;   //if no GameMode is specified, the default mode will be normal
    private PlayersNumber playersNumber = PlayersNumber.THREE;  //if no player number is specified, the default number will be three
    private List<User> users = new LinkedList<>();

    public GameHandlerBuilder gameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    public GameHandlerBuilder playersNumber(PlayersNumber playersNumber) {
        this.playersNumber = playersNumber;
        return this;
    }

    public GameHandlerBuilder player(User user) {
        this.users.add(user);
        return this;
    }

    public GameHandlerBuilder reset() {
        this.users.clear();
        return this;
    }

    public boolean isGameFull() {
        return users.size() == playersNumber.getPlayersNumber();
    }

    public GameHandler build() throws InvalidPlayerNumberException {
        checkPlayerNumberValidity();
        return buildGameHandler();
    }

    private void checkPlayerNumberValidity() throws InvalidPlayerNumberException {
        if (users.size() != playersNumber.getPlayersNumber())
            throw new InvalidPlayerNumberException((users.size() > playersNumber.getPlayersNumber()) ? InvalidPlayerNumberException.Reason.TOO_MANY_PLAYERS : InvalidPlayerNumberException.Reason.NOT_ENOUGH_PLAYERS);
    }

    private GameHandler buildGameHandler() {
        Game gameModel = buildGameModel();
        GameController gameController = buildGameController(gameModel);
        VirtualView[] virtualViews = buildVirtualViews(gameModel);
        return new GameHandler(users.toArray(new User[0]), gameController, gameModel, virtualViews);
    }

    private Game buildGameModel() {
        return switch (gameMode) {
//            case NORMAL_MODE -> buildGame(GameController.class, Player.class, Table.class, TableController.class, GameParameters.class, Game.class);
//            case EXPERT_MODE -> buildGame(ExpertGameController.class, ExpertPlayer.class, ExpertTable.class, ExpertTableController.class, ExpertGameParameters.class, ExpertGame.class);
            case NORMAL_MODE -> buildNormalGameModel();
            case EXPERT_MODE -> buildExpertGameModel();
        };
    }

    private Game buildNormalGameModel() {
        Player[] players = new Player[playersNumber.getPlayersNumber()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(users.get(i).getNickname(), Wizzard.values()[i], Tower.values()[i], playersNumber.getPlayersNumber());
        }

        Table table = new Table(playersNumber.getPlayersNumber());

        GameParameters parameters = new GameParameters();

        return new Game(players, table, parameters);
    }

    private Game buildExpertGameModel() {
        ExpertPlayer[] players = new ExpertPlayer[playersNumber.getPlayersNumber()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new ExpertPlayer(users.get(i).getNickname(), Wizzard.values()[i], Tower.values()[i], playersNumber.getPlayersNumber());
        }

        ExpertTable table = new ExpertTable(playersNumber.getPlayersNumber());

        ExpertGameParameters parameters = new ExpertGameParameters();

        return new ExpertGame(players, table, parameters);
    }

    private GameController buildGameController(Game gameModel) {
        return switch (gameMode) {
//            case NORMAL_MODE -> buildGame(GameController.class, Player.class, Table.class, TableController.class, GameParameters.class, Game.class);
//            case EXPERT_MODE -> buildGame(ExpertGameController.class, ExpertPlayer.class, ExpertTable.class, ExpertTableController.class, ExpertGameParameters.class, ExpertGame.class);
            case NORMAL_MODE -> buildNormalGameController(gameModel);
            case EXPERT_MODE -> buildExpertGameController(gameModel);
        };
    }

    //TODO if the following works, remove this
    private GameController buildNormalGameController(Game gameModel) {
        TableController tableController = new TableController(gameModel.getTable());

        return new GameController(gameModel, tableController);
    }

    private ExpertGameController buildExpertGameController(Game gameModel) {
        ExpertTableController tableController = new ExpertTableController((ExpertTable) gameModel.getTable());

        return new ExpertGameController((ExpertGame) gameModel, tableController);
    }

    //TODO it will probably crash
//    private <TReturn extends GameController,
//            TPlayer extends Player,
//            TTable extends Table,
//            TTableController extends TableController,
//            TParameters extends GameParameters,
//            TGame extends Game>
//    TReturn buildGame(
//            Class returnType,
//            Class playerType,
//            Class tableType,
//            Class tableControllerType,
//            Class parametersType,
//            Class gameType) throws Exception{
//
//        TPlayer[] players = (TPlayer[]) Array.newInstance(playerType, playersNames.size());
//        for(int i = 0; i < players.length; i++){
//            players[i] = (TPlayer) returnType.getConstructor().newInstance(playersNames.get(i), Wizzard.values()[i], Tower.values()[i]);
//        }
//
//        TTable table = (TTable) tableType.getConstructor().newInstance(playersNames.size());
//
//        TTableController tableController = (TTableController) tableControllerType.getConstructor().newInstance(table);
//
//        TParameters parameters = (TParameters) parametersType.getConstructor().newInstance();
//
//        TGame game = (TGame) gameType.getConstructor().newInstance(players, table, parameters);
//
//        return (TReturn) returnType.getConstructor().newInstance(game, tableController);
//    }

    private VirtualView[] buildVirtualViews(Game gameModel) {
        VirtualView[] virtualViews = new VirtualView[playersNumber.getPlayersNumber()];
        for (int i = 0; i < virtualViews.length; i++) {
            virtualViews[i] = new VirtualView(users.get(i).getEndpoint(), users.get(i).getNickname(), gameModel);
        }
        return virtualViews;
    }
}
