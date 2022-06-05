package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.ArrayList;
import java.util.List;

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

    public void notifyPlayer(Player message) {
        for (ModelObserver observer : observers) {
            observer.updatePlayer(message);
        }
    }

    public void notifyPlayerOnline(Player message){
        for (ModelObserver observer : observers) {
            observer.updatePlayerOnline(message);
        }
    }

    public void notifyCoins(int coins){
        for (ModelObserver observer : observers) {
            observer.updatePlayersCoin(coins);
        }
    }

    public void notifyGamePhase(GamePhase message) {
        for (ModelObserver observer : observers) {
            observer.updateGamePhase(message);
        }
    }

    public void notifyAssistantCard(AssistantCard message) {
        for (ModelObserver observer : observers) {
            observer.updateAssistantCard(message);
        }
    }

    public void notifySchoolBoard(SchoolBoard message) {
        for (ModelObserver observer : observers) {
            observer.updateSchoolBoard(message);
        }
    }

    public void notifyAskStudent() {
        for (ModelObserver observer : observers) {
            observer.updateAskStudent();
        }
    }

    public void notifyException(Exception message){
        for (ModelObserver observer : observers) {
            observer.updateException(message);
        }
    }

    public void notifyGameOverFromDisconnection(){
        for (ModelObserver observer : observers) {
            observer.updateGameOverFromDisconnection();
        }
    }

    public void notifyEnoughPlayerOnline(boolean message){
        for (ModelObserver observer : observers) {
            observer.updateEnoughPlayerOnline(message);
        }
    }

    public void notifyWinners(List<String> message){
        for (ModelObserver observer : observers) {
            observer.updateWinners(message);
        }
    }

    public void notifyIslandCard(IslandCard message){
        for (ModelObserver observer : observers) {
            observer.updateIslandCard(message);
        }
    }

    public void notifyCloudTile(CloudTile message){
        for (ModelObserver observer : observers) {
            observer.updateCloudTile(message);
        }
    }

    public void notifyCharacterCard(CharacterCard message, Object[] parameters){
        for (ModelObserver observer : observers) {
            observer.updateCharacterCard(message, parameters);
        }
    }

    public void notifyCharacterCard(CharacterCard message, Object[] parameters,boolean active){
        for (ModelObserver observer : observers) {
            observer.updateCharacterCard(message, parameters,active);
        }
    }
}
