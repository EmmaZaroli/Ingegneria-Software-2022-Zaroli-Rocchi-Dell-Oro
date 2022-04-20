package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;
import it.polimi.ingsw.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

import java.util.LinkedList;
import java.util.List;

public class GameControllerBuilder {
    private GameMode gameMode = GameMode.NORMAL_MODE;   //if no GameMode is specified, the default mode will be normal
    private PlayersNumber playersNumber = PlayersNumber.THREE;  //if no player number is specified, the default number will be three
    private List<String> playersNames = new LinkedList<>();

    public GameControllerBuilder gameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    public GameControllerBuilder playersNumber(PlayersNumber playersNumber) {
        this.playersNumber = playersNumber;
        return this;
    }

    public GameControllerBuilder player(String name) {
        this.playersNames.add(name);
        return this;
    }

    public GameController build() throws InvalidPlayerNumberException {
        checkPlayerNumberValidity();
        return switch (gameMode) {
//            case NORMAL_MODE -> buildGame(GameController.class, Player.class, Table.class, TableController.class, GameParameters.class, Game.class);
//            case EXPERT_MODE -> buildGame(ExpertGameController.class, ExpertPlayer.class, ExpertTable.class, ExpertTableController.class, ExpertGameParameters.class, ExpertGame.class);
            case NORMAL_MODE -> buildNormalGameController();
            case EXPERT_MODE -> buildExpertGameController();
        };
    }

    public GameControllerBuilder reset() {
        this.playersNames.clear();
        return this;
    }

    public boolean isGameFull() {
        return playersNames.size() == playersNumber.getPlayersNumber();
    }

    private void checkPlayerNumberValidity() throws InvalidPlayerNumberException {
        if (playersNames.size() != playersNumber.getPlayersNumber())
            throw new InvalidPlayerNumberException((playersNames.size() > playersNumber.getPlayersNumber()) ? InvalidPlayerNumberException.Reason.TOO_MANY_PLAYERS : InvalidPlayerNumberException.Reason.NOT_ENOUGH_PLAYERS);
    }

    //TODO if the following works, remove this
    private GameController buildNormalGameController() {
        Player[] players = new Player[playersNames.size()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(playersNames.get(i), Wizzard.values()[i], Tower.values()[i]);
        }

        Table table = new Table(playersNames.size());

        TableController tableController = new TableController(table);

        GameParameters parameters = new GameParameters();

        Game game = new Game(players, table, parameters);

        return new GameController(game, tableController);
    }

    private ExpertGameController buildExpertGameController() {
        ExpertPlayer[] players = new ExpertPlayer[playersNames.size()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new ExpertPlayer(playersNames.get(i), Wizzard.values()[i], Tower.values()[i]);
        }

        ExpertTable table = new ExpertTable(playersNames.size());

        ExpertTableController tableController = new ExpertTableController(table);

        ExpertGameParameters parameters = new ExpertGameParameters();

        ExpertGame game = new ExpertGame(players, table, parameters);

        return new ExpertGameController(game, tableController);
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
}
