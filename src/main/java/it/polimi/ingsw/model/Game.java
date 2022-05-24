package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.observer.Observable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class Game extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID gameUUID = UUID.randomUUID();

    protected Player[] players;
    protected Table table;
    private GamePhase gamePhase;
    private int playedCount;
    protected GameParameters parameters;

    protected int currentPlayer;
    private int firstPlayerInRound;
    protected SchoolBoard currentPlayerBoard;
    private int movedPawns;
    private boolean gameOver = false;

    private Exception error;

    private boolean enoughPlayerOnline;

    public Game(Player[] players, Table table, GameParameters parameters) {
        this.players = players;
        this.table = table;
        this.currentPlayer = 0;
        this.parameters = parameters;
    }

    public SchoolBoard getCurrentPlayerSchoolBoard() {
        return this.players[this.currentPlayer].getBoard();
    }

    public UUID getGameId() {
        return gameUUID;
    }

    public Player[] getPlayers() {
        return players.clone();
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

    public boolean isEnoughPlayerOnline() {
        return enoughPlayerOnline;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        notify(gamePhase);
    }

    public void setPlayedCount(int playedCount) {
        this.playedCount = playedCount;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        notify(players[currentPlayer]);
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

    public void movePawn() {
        this.movedPawns++;
    }

    public void callWin(String nicknameWinner) {
        this.gameOver = true;
        notify(nicknameWinner);
    }

    public void callGameOverFromDisconnection() {
        this.gameOver = true;
        //TODO notify
    }

    public void throwException(Exception e) {
        notify(e);
    }

    public void setEnoughPlayerOnline(boolean enoughPlayerOnline) {
        this.enoughPlayerOnline = enoughPlayerOnline;
        //TODO notify
        //TODO block all action if false
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void changePlayer(int playerIndex) {
        System.out.println("changed player in game");
        setCurrentPlayer(playerIndex);
        setCurrentPlayerBoard(getCurrentPlayerSchoolBoard());
        notify(this.players[currentPlayer]);
    }

    public void setError(Exception e) {
        this.error = e;
        notify(this.error);
    }

    public Exception getLastError() {
        return this.error;
    }

    public Player getPlayer(int playerIndex) {
        return players[playerIndex];
    }

    public int howManyPlayersOnline() {
        return (int) Arrays.stream(players).filter(Player::isOnline).count();
    }
}
