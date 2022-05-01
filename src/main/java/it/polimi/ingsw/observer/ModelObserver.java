package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;

/**
 * generic interface, supports the update method
 */
public interface ModelObserver {
    void update(Object message);

    void update(CharacterCard message);

    void update(IslandCard message);

    void update(GamePhase message);

    void update(Player message);

    void update(CloudTile message);

    void update(AssistantCard message);

    void update(SchoolBoard message);
}
