package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;

import java.util.LinkedList;
import java.util.List;

public class GameControllerBuilder {
    private GameMode gameMode = GameMode.NORMAL_MODE;   //if no GameMode is specified, the default mode will be normal
    //private PlayerCountIcon playersNumber = PlayerCountIcon.THREE;  //if no player number is specified, the default number will be three
    private List<String> playersNames = new LinkedList<>();

    public GameControllerBuilder gameMode(GameMode gameMode){
        this.gameMode = gameMode;
        return this;
    }

    /*
    public GameControllerBuilder playersNumber(PlayerCountIcon playersNumber){
        this.playersNumber = playersNumber;
        return this;
    }*/

    public GameControllerBuilder player(String name){
        this.playersNames.add(name);
        return this;
    }

    public GameController build(){
        return switch (gameMode){
            case NORMAL_MODE -> buildNormalGameController();
            case EXPERT_MODE -> buildExpertGameController();
        };
    }

    private GameController buildNormalGameController(){
        Player[] players = new Player[playersNames.size()];
        for(int i = 0; i < players.length; i++){
            players[i] = new Player(playersNames.get(i), Wizzard.values()[i], Tower.values()[i]);
        }

        Table table = new Table(playersNames.size());

        TableController tableController = new TableController(table);

        GameParameters parameters = new GameParameters();

        Game game = new Game(players, table, parameters);

        return new GameController(game, tableController);
    }

    private ExpertGameController buildExpertGameController(){
        ExpertPlayer[] players = new ExpertPlayer[playersNames.size()];
        for(int i = 0; i < players.length; i++){
            players[i] = new ExpertPlayer(playersNames.get(i), Wizzard.values()[i], Tower.values()[i]);
        }

        ExpertTable table = new ExpertTable(playersNames.size());

        ExpertTableController tableController = new ExpertTableController(table);

        ExpertGameParameters parameters = new ExpertGameParameters();

        ExpertGame game = new ExpertGame(players, table, parameters);

        return new ExpertGameController(game, tableController);
    }

    //TODO similar code in buildNormalGameController() and buildExpertGameController(), is there a way to generalize it?
    //TODO maybe create function for building players, expertplayers, tablecontroller, experttablecontroller...
    //TODO check if the size of playerNames equals playerNumbers
}
