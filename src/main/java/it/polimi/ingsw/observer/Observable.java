package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.message.Message;

import java.util.*;

public class Observable {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param observer the observer to be added.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer.
     *
     * @param observer the observer to be removed.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notify(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
