package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.observer.ModelObservable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Game extends ModelObservable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID gameUUID = UUID.randomUUID();

    protected Player[] players;
    protected Table table;
    private GamePhase gamePhase;
    private int playedCount;
    protected GameParameters parameters;

    protected int currentPlayer;
    private int firstPlayerInPlanning;
    protected SchoolBoard currentPlayerBoard;
    private int movedPawns;
    private boolean gameOver = false;
    private List<String> winners = new ArrayList<>();

    private Exception error;

    private boolean enoughPlayerOnline;

    public Game(Player[] players, Table table, GameParameters parameters) {
        this.players = players;
        this.table = table;
        this.currentPlayer = 0;
        this.gamePhase = GamePhase.PLANNING;
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

    public int getFirstPlayerInPlanning() {
        return firstPlayerInPlanning;
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
        notifyGamePhase(gamePhase);
    }

    public void setPlayedCount(int playedCount) {
        this.playedCount = playedCount;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyPlayer(players[currentPlayer]);
    }

    public void setFirstPlayerInPlanning(int firstPlayerInPlanning) {
        this.firstPlayerInPlanning = firstPlayerInPlanning;
    }

    public void setCurrentPlayerBoard(SchoolBoard currentPlayerBoard) {
        this.currentPlayerBoard = currentPlayerBoard;
    }

    public void setMovedPawns(int movedPawns) {
        this.movedPawns = movedPawns;
    }

    public void movePawn() {
        this.movedPawns++;
        notifyModelObserver("ask student");
    }

    public void callWin(List<String> winners) {
        this.gameOver = true;
        this.setGamePhase(GamePhase.GAME_OVER);
        setWinners(winners);
    }

    public void callWin(String winner){
        List<String> winners = new ArrayList<>();
        winners.add(winner);
        callWin(winners);
    }

    public void callGameOverFromDisconnection() {
        this.gameOver = true;
        notifyGameOverFromDisconnection();
    }

    public void throwException(Exception e) {
        this.error = e;
        notifyException(e);
    }

    public void setEnoughPlayerOnline(boolean enoughPlayerOnline) {
        this.enoughPlayerOnline = enoughPlayerOnline;
        notifyEnoughPlayerOnline(enoughPlayerOnline);
        //TODO block all action if false
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void changePlayer(int playerIndex) {
        setCurrentPlayer(playerIndex);
        setCurrentPlayerBoard(getCurrentPlayerSchoolBoard());
    }

    public Exception getLastError() {
        return this.error;
    }

    public Player getPlayer(int playerIndex) {
        return players[playerIndex];
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
        notifyWinners(this.winners);
    }

    public int howManyPlayersOnline() {
        return (int) Arrays.stream(players).filter(Player::isOnline).count();
    }


}
