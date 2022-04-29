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
        VirtualView[] virtualViews = buildVirtualViews(gameModel);
        GameController gameController = buildGameController(gameModel, virtualViews);
        return new GameHandler(users.toArray(new User[0]), gameController, gameModel, virtualViews);
    }

    private Game buildGameModel() {
        return switch (gameMode) {
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

    private GameController buildGameController(Game gameModel, VirtualView[] virtualViews) {
        return switch (gameMode) {
            case NORMAL_MODE -> buildNormalGameController(gameModel, virtualViews);
            case EXPERT_MODE -> buildExpertGameController(gameModel, virtualViews);
        };
    }

    private GameController buildNormalGameController(Game gameModel, VirtualView[] virtualViews) {
        TableController tableController = new TableController(gameModel.getTable());

        return new GameController(gameModel, tableController, virtualViews);
    }

    private ExpertGameController buildExpertGameController(Game gameModel, VirtualView[] virtualViews) {
        ExpertTableController tableController = new ExpertTableController((ExpertTable) gameModel.getTable());

        return new ExpertGameController((ExpertGame) gameModel, tableController, virtualViews);
    }

    private VirtualView[] buildVirtualViews(Game gameModel) {
        VirtualView[] virtualViews = new VirtualView[playersNumber.getPlayersNumber()];
        for (int i = 0; i < virtualViews.length; i++) {
            virtualViews[i] = new VirtualView(users.get(i), gameModel);
        }
        return virtualViews;
    }
}
