package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.*;
import java.util.*;

public class Table {
    private Set<PawnColor> professors;
    private List<IslandCard> islandCards;
    private final Bag bag;
    private final List<CloudTile> cloudTiles;

    public Table(PlayerCountIcon playerCountIcon) {
        professors = new HashSet<>();
        professors.addAll(Arrays.asList(PawnColor.values()));
        islandCards = new ArrayList<>();
        for(int i=0; i<12; i++){
            this.islandCards.add(new IslandCard());
        }
        this.bag = new Bag();
        cloudTiles = new ArrayList<>();
        this.cloudTiles.add(new CloudTile(playerCountIcon));
        this.cloudTiles.add(new CloudTile(playerCountIcon));
        if(playerCountIcon.equals(PlayerCountIcon.THREE)){
            this.cloudTiles.add(new CloudTile(playerCountIcon));
        }
    }


}
