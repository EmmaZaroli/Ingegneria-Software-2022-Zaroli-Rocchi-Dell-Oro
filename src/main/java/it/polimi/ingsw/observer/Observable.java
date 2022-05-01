package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final List<ModelObserver> modelObservers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param modelObserver the observer to be added.
     */
    public void addObserver(ModelObserver modelObserver) {
        modelObservers.add(modelObserver);
    }

    /**
     * Removes an observer.
     *
     * @param modelObserver the observer to be removed.
     */
    public void removeObserver(ModelObserver modelObserver) {
        modelObservers.remove(modelObserver);
    }

    public void notify(Object message) {
        for (ModelObserver modelObserver : modelObservers) {
            modelObserver.update(message);
        }
    }
}
