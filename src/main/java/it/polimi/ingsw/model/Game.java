package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.observer.ModelObservable;
import it.polimi.ingsw.servercontroller.GameEndingListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Game
 */
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

    private boolean enoughPlayersOnline = true;

    private transient List<GameEndingListener> gameEndingListeners = new LinkedList<>();

    /**
     * Instantiates a new Game
     * @param players the players
     * @param table the table
     * @param parameters the Game parameters
     */
    public Game(Player[] players, Table table, GameParameters parameters) {
        this.players = players;
        this.table = table;
        this.currentPlayer = 0;
        this.gamePhase = GamePhase.PLANNING;
        this.parameters = parameters;
    }

    /**
     *
     * @return the current player's schoolBoard
     */
    public SchoolBoard getCurrentPlayerSchoolBoard() {
        return this.players[this.currentPlayer].getBoard();
    }

    /**
     *
     * @return the game uuid
     */
    public UUID getGameId() {
        return gameUUID;
    }

    /**
     *
     * @return a clone of the player's array
     */
    public Player[] getPlayers() {
        return players.clone();
    }

    /**
     *
     * @return the number of players
     */
    public int getPlayersCount() {
        return players.length;
    }

    /**
     *
     * @return the gamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     *
     * @return the number of player that have finished the action Phase
     */
    public int getPlayedCount() {
        return playedCount;
    }

    /**
     *
     * @return the GameParameters
     */
    public GameParameters getParameters() {
        return parameters;
    }

    /**
     *
     * @return the index of the current player
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     *
     * @return the index of the first player to play in the planning phase
     */
    public int getFirstPlayerInPlanning() {
        return firstPlayerInPlanning;
    }

    /**
     *
     * @return the current player's schoolBoard
     */
    public SchoolBoard getCurrentPlayerBoard() {
        return currentPlayerBoard;
    }

    /**
     *
     * @return the table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     *
     * @return the number of pawns already moved
     */
    public int getMovedPawns() {
        return movedPawns;
    }

    /**
     *
     * @return true if there are enough players to play, false otherwise
     */
    public boolean areEnoughPlayersOnline() {
        return enoughPlayersOnline;
    }

    /**
     * Notify the views the new GamePhase
     * @param gamePhase the new gamePhase
     */
    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        notifyGamePhase(gamePhase);
    }

    /**
     *
     * @param playedCount the number of player that have finished the action Phase
     */
    public void setPlayedCount(int playedCount) {
        this.playedCount = playedCount;
    }

    /**
     * Notify the views the current player
     * @param currentPlayer the index of the current player
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyPlayer(players[currentPlayer]);
    }

    /**
     *
     * @param firstPlayerInPlanning the index of the first player to play during the planning phase
     */
    public void setFirstPlayerInPlanning(int firstPlayerInPlanning) {
        this.firstPlayerInPlanning = firstPlayerInPlanning;
    }

    /**
     *
     * @param currentPlayerBoard the current player's schoolBoard
     */
    public void setCurrentPlayerBoard(SchoolBoard currentPlayerBoard) {
        this.currentPlayerBoard = currentPlayerBoard;
    }

    /**
     *
     * @param movedPawns the number of pawns already moved
     */
    public void setMovedPawns(int movedPawns) {
        this.movedPawns = movedPawns;
    }

    /**
     * Notify the views that a pawn was moved
     */
    public void movePawn() {
        this.movedPawns++;
        notifyAskStudent();
    }

    /**
     * Call when the game has ended
     * Notify the views that the game has ended
     * @param winners the winners' nicknames
     */
    public void callWin(List<String> winners) {
        this.gameOver = true;
        this.setGamePhase(GamePhase.GAME_OVER);
        setWinners(winners);
        notifyGameEnding();
    }

    /**
     *
     * @param winner the winner's nickname
     */
    public void callWin(String winner){
        List<String> winnersList = new ArrayList<>();
        winnersList.add(winner);
        callWin(winnersList);
    }

    /**
     * Called if there aren't enough player to continue playing
     * Calls gameOver
     */
    public void callGameOverFromDisconnection() {
        this.gameOver = true;
        this.setGamePhase(GamePhase.GAME_OVER);
        notifyGameOverFromDisconnection();
        notifyGameEnding();
    }

    /**
     * Notify the views that an exception was thrown
     * @param e the exception
     */
    public void throwException(Exception e) {
        this.error = e;
        notifyException(e);
    }

    /**
     *
     * @param enoughPlayersOnline true if there are enough players online
     */
    public void setEnoughPlayersOnline(boolean enoughPlayersOnline) {
        this.enoughPlayersOnline = enoughPlayersOnline;
        notifyEnoughPlayerOnline(enoughPlayersOnline);
    }

    /**
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     *
     * @param playerIndex the new player's index
     */
    public void changePlayer(int playerIndex) {
        setCurrentPlayer(playerIndex);
        setCurrentPlayerBoard(getCurrentPlayerSchoolBoard());
    }

    /**
     *
     * @return the last exception thrown
     */
    public Exception getLastError() {
        return this.error;
    }

    /**
     *
     * @param playerIndex the player's index
     * @return the Player
     */
    public Player getPlayer(int playerIndex) {
        return players[playerIndex];
    }

    /**
     *
     * @return the list of winners
     */
    public List<String> getWinners() {
        return winners;
    }

    /**
     * Notify the views the list of winners
     * @param winners the list of the winner's nicknames
     */
    public void setWinners(List<String> winners) {
        this.winners = winners;
        notifyWinners(this.winners);
    }

    /**
     *
     * @return the number of players online
     */
    public int howManyPlayersOnline() {
        return (int) Arrays.stream(players).filter(Player::isOnline).count();
    }

    /**
     *
     * @param l the Game ending listener to add
     */
    public void addGameEndingListener(GameEndingListener l) {
        this.gameEndingListeners.add(l);
    }

    /**
     *
     * @param l the game ending listener to remove
     */
    public void removeGameEndingListener(GameEndingListener l) {
        this.gameEndingListeners.remove(l);
    }

    // TODO remove if not needed
    public List<GameEndingListener> getGameEndingListeners(){
        return gameEndingListeners;
    }

    /**
     * Notify every game ending listeners that the game has ended
     */
    private void notifyGameEnding() {
        for(GameEndingListener l : gameEndingListeners)
            l.onGameEnding(getGameId());
    }

    public void copyStatusFrom(Game game){
        this.players = game.players;
        this.table = game.table;
        this.gamePhase = game.gamePhase;
        this.playedCount = game.playedCount;
        this.parameters = game.parameters;

        this.currentPlayer = game.currentPlayer;
        this.firstPlayerInPlanning = game.firstPlayerInPlanning;
        this.currentPlayerBoard = game.currentPlayerBoard;
        this.movedPawns = game.movedPawns;
        this.gameOver = game.gameOver;
        this.winners = game.winners;

        this.error = game.error;

        this.enoughPlayersOnline = game.enoughPlayersOnline;
    }

    public void setGameEndingListeners(List<GameEndingListener> gameEndingListeners) {
        this.gameEndingListeners = gameEndingListeners;
    }
}
