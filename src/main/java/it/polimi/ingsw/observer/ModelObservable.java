package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Observable
 * This class is extended by the model, and represents an observable object
 */
public class ModelObservable {

    private final List<ModelObserver> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param modelObserver the observer to be added.
     */
    public void addObserver(ModelObserver modelObserver) {
        observers.add(modelObserver);
    }

    /**
     * Removes an observer.
     *
     * @param modelObserver the observer to be removed.
     */
    public void removeObserver(ModelObserver modelObserver) {
        observers.remove(modelObserver);
    }

    /**
     * Notifies all the current observers through the update method and passes to them the modified Player
     *
     * @param message the modified Player to be passed to the observers.
     */
    public void notifyPlayer(Player message) {
        for (ModelObserver observer : observers) {
            observer.updatePlayer(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the modified Player
     * Used if the state of a player (online/offline) was modified
     * @param message the modified Player to be passed to the observers.
     */
    public void notifyPlayerOnline(Player message){
        for (ModelObserver observer : observers) {
            observer.updatePlayerOnline(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the Player
     * Used if the player can play this round
     * @param message the Player to be passed to the observers.
     */
    public void notifyPlayerCanPlay(Player message){
        for (ModelObserver observer : observers) {
            observer.updatePlayerCanPlay(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the current player's coins
     *
     * @param coins the current player's coins to be passed to the observers.
     */
    public void notifyPlayerCoins(int coins){
        for (ModelObserver observer : observers) {
            observer.updatePlayerCoin(coins);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the new number of coins on the table
     *
     * @param coins the new table's coins to be passed to the observers.
     */
    public void notifyTableCoins(int coins){
        for (ModelObserver observer : observers) {
            observer.updateTableCoins(coins);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the new gamePhase
     *
     * @param message the new gamePhase to be passed to the observers.
     */
    public void notifyGamePhase(GamePhase message) {
        for (ModelObserver observer : observers) {
            observer.updateGamePhase(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the played assistantCard
     * Used when an assistantCard is been correctly played
     * @param message the played assistantCard to be passed to the observers.
     */
    public void notifyAssistantCard(AssistantCard message) {
        for (ModelObserver observer : observers) {
            observer.updateAssistantCard(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the modified schoolBoard
     *
     * @param message the modified schoolBoard to be passed to the observers.
     */
    public void notifySchoolBoard(SchoolBoard message) {
        for (ModelObserver observer : observers) {
            observer.updateSchoolBoard(message);
        }
    }

    /**
     * Notifies all the current observers through the update method that the server is waiting for a move
     */
    public void notifyAskStudent() {
        for (ModelObserver observer : observers) {
            observer.updateAskStudent();
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the exception message
     *
     * @param message the exception to be passed to the observers.
     */
    public void notifyException(Exception message){
        for (ModelObserver observer : observers) {
            observer.updateException(message);
        }
    }

    /**
     * Notifies all the current observers through the update method that the player/players previously disconnected
     * didn't reconnect in time for the game, so the only player left is proclaimed winner
     */
    public void notifyGameOverFromDisconnection(){
        for (ModelObserver observer : observers) {
            observer.updateGameOverFromDisconnection();
        }
    }

    /**
     * Notifies all the current observers through the update method if there are enough player online
     *
     * @param message true if there are enough player to continue playing, false otherwise
     */
    public void notifyEnoughPlayerOnline(boolean message){
        for (ModelObserver observer : observers) {
            observer.updateEnoughPlayerOnline(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the winners' nicknames
     *
     * @param message the winners' nicknames to be passed to the observers.
     */
    public void notifyWinners(List<String> message){
        for (ModelObserver observer : observers) {
            observer.updateWinners(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the modified islandCard
     *
     * @param message the modified islandCard to be passed to the observers.
     */
    public void notifyIslandCard(IslandCard message){
        for (ModelObserver observer : observers) {
            observer.updateIslandCard(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the modified cloudTile
     *
     * @param message the modified cloudTile to be passed to the observers.
     */
    public void notifyCloudTile(CloudTile message){
        for (ModelObserver observer : observers) {
            observer.updateCloudTile(message);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the modified characterCard
     *
     * @param message the modified characterCard to be passed to the observers.
     */
    public void notifyCharacterCard(CharacterCard message, Object[] parameters){
        for (ModelObserver observer : observers) {
            observer.updateCharacterCard(message, parameters);
        }
    }

    /**
     * Notifies all the current observers through the update method and passes to them the modified expertParameters
     *
     * @param message the modified expertParameters to be passed to the observers.
     */
    public void notifyExpertParameters(ExpertGameParameters message){
        for (ModelObserver observer : observers) {
            observer.updateExpertParameters(message);
        }
    }
}
