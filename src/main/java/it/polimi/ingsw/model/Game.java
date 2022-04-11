package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

//TODO getters to the view should not expose their rep
public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID gameUUID = UUID.randomUUID();

    private final Player[] players;
    //had to remove final from table because it would have been impossible to make it ExpertTable in ExpertGameController
    protected Table table;
    private GamePhase gamePhase;
    private int playedCount;
    private GameParameters parameters;

    private int currentPlayer;
    //TODO this value is used but never initialized
    private int firstPlayerInRound;
    protected SchoolBoard currentPlayerBoard;
    private int movedPawns;

    public Game(Player[] players) {
        this.players = players;
        //TODO how to handle tableController?
        this.table = new Table(this.players.length);
        this.currentPlayer = 0;
        this.parameters = new GameParameters();
    }

    public SchoolBoard getCurrentPlayerSchoolBoard() {
        return this.players[this.currentPlayer].getBoard();
    }

    public UUID getGameId() {
        return gameUUID;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getPlayersCount() {
        return players.length;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public int getPlayedCount() {
        return playedCount;
    }

    public GameParameters getParameters() {
        return parameters;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getFirstPlayerInRound() {
        return firstPlayerInRound;
    }

    public SchoolBoard getCurrentPlayerBoard() {
        return currentPlayerBoard;
    }

    public Table getTable() {
        return this.table;
    }

    public int getMovedPawns() {
        return movedPawns;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void setPlayedCount(int playedCount) {
        this.playedCount = playedCount;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setFirstPlayerInRound(int firstPlayerInRound) {
        this.firstPlayerInRound = firstPlayerInRound;
    }

    public void setCurrentPlayerBoard(SchoolBoard currentPlayerBoard) {
        this.currentPlayerBoard = currentPlayerBoard;
    }

    public void setMovedPawns(int movedPawns) {
        this.movedPawns = movedPawns;
    }
}
