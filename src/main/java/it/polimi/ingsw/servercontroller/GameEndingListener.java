package it.polimi.ingsw.servercontroller;

import java.util.UUID;

/**
 * Notify the uuid of the game ended
 */
public interface GameEndingListener {
    void onGameEnding(UUID uuid);
}
