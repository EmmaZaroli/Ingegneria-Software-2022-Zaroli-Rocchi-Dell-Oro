package it.polimi.ingsw.servercontroller;

import java.util.UUID;

public interface GameEndingListener {
    void onGameEnding(UUID uuid);
}
