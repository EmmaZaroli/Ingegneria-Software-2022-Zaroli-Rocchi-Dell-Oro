package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.RandomHelper;

import java.io.Serial;
import java.io.Serializable;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Table implements Serializable {
//TODO change int playersNumber with PlayersNumber
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
            this.islandCards.add(new IslandCard(java.util.UUID.randomUUID()));
        }
        RandomHelper random = RandomHelper.getInstance();
        int initialPosition = random.getInt(12);
        islandWithMotherNature = initialPosition;
        List<PawnColor> initialized = new ArrayList<>(10);
        initialized.addAll(PawnColor.getValidValues());
        initialized.addAll(PawnColor.getValidValues());
        for (int i = 0; i < 12; i++) {
            if ((Math.abs(i - initialPosition) != 6) && !(islandWithMotherNature == i)) {
                int pawnColor = random.getInt(initialized.size());
                islandCards.get(i).movePawnOnIsland(initialized.get(pawnColor));
                initialized.remove(pawnColor);
            }
        }
        this.bag = new Bag();
        cloudTiles = new ArrayList<>();
        this.cloudTiles.add(new CloudTile(java.util.UUID.randomUUID()));
        this.cloudTiles.add(new CloudTile(java.util.UUID.randomUUID()));
        if (playersNumber == 3) {
            this.cloudTiles.add(new CloudTile(java.util.UUID.randomUUID()));
        }
        professors = new ArrayList<>();
        professors.addAll(PawnColor.getValidValues());
    }

    public Bag getBag() {
        return bag;
    }

    public List<IslandCard> getIslands() {
        return this.islandCards;
    }

    public IslandCard getIsland(UUID uuid) throws WrongUUIDException {
        for (IslandCard island : this.islandCards) {
            if (island.getUuid().equals(uuid)) {
                return island;
            }
        }
        throw new WrongUUIDException();
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
