package it.polimi.ingsw.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Network implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
    }
    

}
