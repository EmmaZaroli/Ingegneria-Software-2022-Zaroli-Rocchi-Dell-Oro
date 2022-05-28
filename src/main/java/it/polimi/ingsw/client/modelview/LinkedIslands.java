package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.dtos.IslandCardDto;

public class LinkedIslands {

    private IslandCardDto island;
    private boolean isMainIsland = true;
    private boolean connectedWithNext = false;

    public LinkedIslands(IslandCardDto island) {
        this.island = island;
    }

    public LinkedIslands() {
    }

    public boolean isConnectedWithNext() {
        return connectedWithNext;
    }

    public void setConnectedWithNext(boolean connectedWithNext) {
        this.connectedWithNext = connectedWithNext;
    }

    public IslandCardDto getIsland() {
        return island;
    }

    public void setIsland(IslandCardDto island) {
        this.island = island;
    }

    public boolean isMainIsland() {
        return isMainIsland;
    }

    /**
     * @param isMainIsland is false if the island was eliminated due to its union with another island,
     *                    true if the island is not connected or is the main island of a group of islands
     */
    public void setIsMainIsland(boolean isMainIsland) {
        this.isMainIsland = isMainIsland;
    }
}
