package it.polimi.ingsw.observer;

import java.util.*;

public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param observer the observer to be added.
     */
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer.
     *
     * @param observer the observer to be removed.
     */
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void notify(T message) {
        for (Observer<T> observer : observers) {
            observer.update(message);
        }
    }
}
