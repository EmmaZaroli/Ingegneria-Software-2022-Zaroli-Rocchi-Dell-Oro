package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.model.IslandCard;

import java.util.ArrayList;
import java.util.List;

public class LinkedIslands {

    private IslandCard island;
    private List<IslandCard> linkedislands = new ArrayList<>();
    private boolean connected = false;

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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected() {
        this.connected = true;
    }
}
