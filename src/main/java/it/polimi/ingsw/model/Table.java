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

    public Table(PlayersNumber playersNumber) {
        this.playersNumber = playersNumber;
        islandCards = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            this.islandCards.add(new IslandCard(java.util.UUID.randomUUID(), i));
        }
        RandomHelper random = RandomHelper.getInstance();
        int initialPosition = random.getInt(12);
        islandCards.get(initialPosition).setHasMotherNature(true);
        List<PawnColor> initialized = new ArrayList<>(10);
        initialized.addAll(PawnColor.getValidValues());
        initialized.addAll(PawnColor.getValidValues());
        for (int i = 0; i < 12; i++) {
            if ((Math.abs(i - initialPosition) != 6) && (getIslandWithMotherNature() != i)) {
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

    public IslandCard getIsland(int index){
        return this.islandCards.get(index);
    }

    public void removeIsland(int index){
        this.islandCards.remove(index);
    }

    public void movePawnOnIsland(IslandCard island, List<PawnColor> students) {
        island.movePawnOnIsland(students);
        notifyModelObserver(island);
    }

    public void movePawnOnIsland(IslandCard island, PawnColor students) {
        island.movePawnOnIsland(students);
        notifyModelObserver(island);
    }

    public IslandCard getIsland(UUID uuid) throws WrongUUIDException {
        for (IslandCard island : this.islandCards) {
            if (island.getUuid().equals(uuid)) {
                return island;
            }
        }
        throw new WrongUUIDException();
    }

    public void setTower(IslandCard island, Tower tower) {
        island.setTower(tower);
        notifyModelObserver(island);
    }

    public void addStudents(CloudTile cloud, List<PawnColor> students) {
        cloud.addStudents(students);
        notifyModelObserver(cloud);
    }

    public List<PawnColor> takeStudentsFromCloud(CloudTile cloud) {
        List<PawnColor> retVal = cloud.takeStudentsFromCloud();
        notifyModelObserver(cloud);
        return retVal;
    }

    public int getIslandWithMotherNature() {
        int i;
        for(i = 0; i < islandCards.size(); i++){
            if(getIsland(i).isHasMotherNature())
                return i;
        }
        return -1;
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
        int oldPosition = getIslandWithMotherNature();
        this.getIsland(getIslandWithMotherNature()).setHasMotherNature(false);
        notifyModelObserver(getIsland(oldPosition));
        this.getIsland(index).setHasMotherNature(true);
        notifyModelObserver(getIsland(getIslandWithMotherNature()));
    }

    public void unifyIslands(int originalIndex, int otherIndex){
        IslandCard islandCard = getIsland(originalIndex);
        islandCard.unifyWith(getIsland(otherIndex));
        removeIsland(otherIndex);
        notify(islandCard);
    }
}
