package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;

/**
 * generic interface, supports the update method
 */
//TODO something is missing
public interface ModelObserver extends Observer {
    void update(CharacterCard message, Object[] parameters);

    void update(IslandCard message);

    void update(GamePhase message);

    void update(Player message);

    void update(CloudTile message);

    void update(AssistantCard message);

    void update(SchoolBoard message);

    void update(Exception message);
}
