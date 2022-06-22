package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.dtos.IslandCardDto;

/**
 * Linked Islands
 */
public class LinkedIslands {

    private IslandCardDto island;
    private boolean isMainIsland = true;
    private boolean connectedWithNext = false;

    public LinkedIslands(IslandCardDto island) {
        this.island = island;
    }

    /**
     * @return true if the island is connected with the next one
     */
    public boolean isConnectedWithNext() {
        return connectedWithNext;
    }

    /**
     * @param connectedWithNext true if the island is connected with the next one
     */
    public void setConnectedWithNext(boolean connectedWithNext) {
        this.connectedWithNext = connectedWithNext;
    }

    /**
     * @return the IslandCardDto
     */
    public IslandCardDto getIsland() {
        return island;
    }

    /**
     * @param island the IslandCardDto
     */
    public void setIsland(IslandCardDto island) {
        this.island = island;
    }

    /**
     * @return false if the island was eliminated due to its union with another island,
     *         true if the island is not connected or is the main island of a group of islands
     *
     */
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
