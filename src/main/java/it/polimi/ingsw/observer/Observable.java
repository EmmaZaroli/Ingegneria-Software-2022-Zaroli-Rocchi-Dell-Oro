package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param modelObserver the observer to be added.
     */
    public void addObserver(Observer modelObserver) {
        observers.add(modelObserver);
    }

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

    public void notify(Object message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void notifyModelObserver(Player message) {
        for (Observer observer : observers) {
            ((ModelObserver) observer).update(message);
        }
    }

    public void notifyModelObserver(GamePhase message) {
        for (Observer observer : observers) {
            ((ModelObserver) observer).update(message);
        }
    }

    public void notifyModelObserver(AssistantCard message) {
        for (Observer observer : observers) {
            ((ModelObserver) observer).update(message);
        }
    }

    public void notifyModelObserver(SchoolBoard message) {
        for (Observer observer : observers) {
            ((ModelObserver) observer).update(message);
        }
    }

    //TODO notifyModelObserver for every type of notify from model to view
}
