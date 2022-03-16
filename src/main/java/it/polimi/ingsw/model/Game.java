package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;

public class Game {
    private Player[] players;
    private int currentPlayer;
    private boolean isFirsRound;
    private GamePhase gamePhase;

    public Game(Player[] players) {
        this.players = players; //TODO eventualmente verranno passati solo i nickname e i wizard, e nel costruttore verranno costruiti i player
        this.currentPlayer = 0;
        this.isFirsRound = true;
        this.gamePhase = GamePhase.PLANNING_ADD_STUDENTS_TO_CLOUD;
    }

    private int pickNextPlayer() {
        //TODO
        return 0;
    }
}
