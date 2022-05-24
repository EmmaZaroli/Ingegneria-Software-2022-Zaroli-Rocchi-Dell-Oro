package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.dtos.IslandCardDto;

import java.util.ArrayList;
import java.util.List;

public class LinkedIslands {

    private IslandCardDto island;
    private List<LinkedIslands> linkedislands = new ArrayList<>();
    private boolean mainConnected = true;
    private boolean connectedWithNext = false;

    public boolean isConnectedWithNext() {
        return connectedWithNext;
    }

    public void setConnectedWithNext(boolean connectedWithNext) {
        this.connectedWithNext = connectedWithNext;
    }

    public IslandCardDto getMainIsland() {
        return island;
    }

    public void setMainIsland(IslandCardDto island) {
        this.island = island;
    }

    public List<LinkedIslands> getLinkedislands() {
        return linkedislands;
    }

    public void addLinkedislands(LinkedIslands linkedIslands) {
        this.linkedislands.add(linkedIslands);
    }

    public void addLinkedislands(List<LinkedIslands> linkedisland) {
        this.linkedislands.addAll(linkedisland);
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
