package it.polimi.ingsw.client.gui.sceneControllers;

import javafx.beans.property.SimpleIntegerProperty;

public class Table {
    public SimpleIntegerProperty myCoins = new SimpleIntegerProperty();

    public int getMyCoins() {
        return myCoins.get();
    }
}
