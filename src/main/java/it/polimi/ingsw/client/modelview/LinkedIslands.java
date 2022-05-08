package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.model.IslandCard;

import java.util.ArrayList;
import java.util.List;

public class LinkedIslands {

    private IslandCard island;
    private List<IslandCard> linkedislands = new ArrayList<>();
    private boolean mainConnected = true;
    private boolean connectedWithNext = false;

    public boolean isConnectedWithNext() {
        return connectedWithNext;
    }

    public void setConnectedWithNext(boolean connectedWithNext) {
        this.connectedWithNext = connectedWithNext;
    }

    public IslandCard getMainIsland() {
        return island;
    }

    public void setMainIsland(IslandCard island) {
        this.island = island;
    }

    public List<IslandCard> getLinkedislands() {
        return linkedislands;
    }

    public void setLinkedislands(IslandCard linkedisland) {
        this.linkedislands.add(linkedisland);
    }

    public boolean isMainConnected() {
        return mainConnected;
    }

    /**
     * @param isConnected is false if the island was eliminated due to its union with another island,
     *                    true if the island is not connected or is the main island of a group of islands
     */
    public void setMainConnected(boolean isConnected) {
        this.mainConnected = isConnected;
    }
}
