package it.polimi.ingsw.model;

import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.gamecontroller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.utils.RandomHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Table extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private final ArrayList<PawnColor> professors;
    private final ArrayList<IslandCard> islandCards;
    private final Bag bag;
    private final ArrayList<CloudTile> cloudTiles;
    private final PlayersNumber playersNumber;
    private int islandWithMotherNature;

    public Table(PlayersNumber playersNumber) {
        this.playersNumber = playersNumber;
        islandCards = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            this.islandCards.add(new IslandCard(java.util.UUID.randomUUID()));
        }
        RandomHelper random = RandomHelper.getInstance();
        int initialPosition = random.getInt(12);
        islandWithMotherNature = initialPosition;
        islandCards.get(initialPosition).setHasMotherNature(true);
        List<PawnColor> initialized = new ArrayList<>(10);
        initialized.addAll(PawnColor.getValidValues());
        initialized.addAll(PawnColor.getValidValues());
        for (int i = 0; i < 12; i++) {
            if ((Math.abs(i - initialPosition) != 6) && (islandWithMotherNature != i)) {
                int pawnColor = random.getInt(initialized.size());
                islandCards.get(i).movePawnOnIsland(initialized.get(pawnColor));
                notify(islandCards.get(i));
                initialized.remove(pawnColor);
            }
        }
        this.bag = new Bag();
        cloudTiles = new ArrayList<>();
        this.cloudTiles.add(new CloudTile(java.util.UUID.randomUUID()));
        this.cloudTiles.add(new CloudTile(java.util.UUID.randomUUID()));
        if (playersNumber == PlayersNumber.THREE) {
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

    public void movePawnOnIsland(IslandCard island, List<PawnColor> students) {
        island.movePawnOnIsland(students);
        notify(island);
    }

    public void movePawnOnIsland(IslandCard island, PawnColor students) {
        island.movePawnOnIsland(students);
        notify(island);
    }

    public IslandCard getIsland(UUID uuid) throws WrongUUIDException {
        for (IslandCard island : this.islandCards) {
            if (island.getUuid().equals(uuid)) {
                return island;
            }
        }
        throw new WrongUUIDException();
    }

    public void incrementSize(IslandCard island) {
        islandCards.stream()
                .filter(x -> x.equals(island))
                .forEach(x -> {
                    x.incrementSize();
                    notify(x);
                });
    }

    public void setTower(IslandCard island, Tower tower) {
        island.setTower(tower);
        notify(island);
    }

    public void addStudents(CloudTile cloud, List<PawnColor> students) {
        cloud.addStudents(students);
        notify(cloud);
    }

    public List<PawnColor> takeStudentsFromCloud(CloudTile cloud) {
        List<PawnColor> retVal = cloud.takeStudentsFromCloud();
        notify(cloud);
        return retVal;
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

    public PlayersNumber getPlayersNumber() {
        return this.playersNumber;
    }

    public void setIslandWithMotherNature(int index) {
        this.islandCards.get(islandWithMotherNature).setHasMotherNature(false);
        this.islandWithMotherNature = index;
        this.islandCards.get(islandWithMotherNature).setHasMotherNature(true);
    }
}
