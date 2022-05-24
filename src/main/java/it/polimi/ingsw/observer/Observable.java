package it.polimi.ingsw.observer;

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
        System.out.println(modelObserver);
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
}
