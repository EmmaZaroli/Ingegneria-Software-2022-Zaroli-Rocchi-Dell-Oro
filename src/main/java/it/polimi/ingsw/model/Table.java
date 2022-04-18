package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.RandomHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    private final ArrayList<PawnColor> professors;
    private final ArrayList<IslandCard> islandCards;
    private final Bag bag;
    private final ArrayList<CloudTile> cloudTiles;
    private final int playersNumber;
    private int islandWithMotherNature;

    public Table(int playersNumber) {
        this.playersNumber = playersNumber;
        islandCards = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            this.islandCards.add(new IslandCard());
        }
        RandomHelper random = RandomHelper.getInstance();
        int initialPosition = random.getInt(12);
        islandWithMotherNature = initialPosition;
        List<PawnColor> initialized = new ArrayList<>(10);
        initialized.addAll((Arrays.stream(PawnColor.values()).toList()));
        initialized.addAll((Arrays.stream(PawnColor.values()).toList()));
        for (int i = 0; i < 12; i++) {
            if (Math.abs(i - initialPosition) != 6) {
                int pawnColor = random.getInt(initialized.size());
                islandCards.get((i + initialPosition) % 12).movePawnOnIsland(initialized.get(pawnColor));
                initialized.remove(pawnColor);
            }
        }
        this.bag = new Bag();
        cloudTiles = new ArrayList<>();
        this.cloudTiles.add(new CloudTile());
        this.cloudTiles.add(new CloudTile());
        if (playersNumber == 3) {
            this.cloudTiles.add(new CloudTile());
        }
        professors = new ArrayList<>();
        professors.addAll(Arrays.asList(PawnColor.values()));
    }

    public Bag getBag() {
        return bag;
    }

    public List<IslandCard> getIslands() {
        return this.islandCards;
    }

    public int getIslandWithMotherNature() {
        return this.islandWithMotherNature;
    }

    public List<CloudTile> getCloudTiles() {
        return this.cloudTiles;
    }

    public int countIsland() {
        return islandCards.size();
    }

    public List<PawnColor> getProfessors() {
        return this.professors;
    }

    public int getPlayersNumber() {
        return this.playersNumber;
    }

    public void setIslandWithMotherNature(int index) {
        this.islandWithMotherNature = index;
    }
}
