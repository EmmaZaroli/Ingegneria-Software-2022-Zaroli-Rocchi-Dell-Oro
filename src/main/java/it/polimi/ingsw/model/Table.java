package it.polimi.ingsw.model;

import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.gamecontroller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.observer.ModelObservable;
import it.polimi.ingsw.utils.RandomHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Table
 */
public class Table extends ModelObservable implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private final ArrayList<PawnColor> professors;
    private final ArrayList<IslandCard> islandCards;
    private final Bag bag;
    private final ArrayList<CloudTile> cloudTiles;
    private final PlayersNumber playersNumber;

    /**
     * Instantiates a new table
     * @param playersNumber the number of players
     */
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
                notifyIslandCard(islandCards.get(i));
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

    /**
     *
     * @return the bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     *
     * @return the list of islandCards on the table
     */
    public List<IslandCard> getIslands() {
        return this.islandCards;
    }

    /**
     * @param index the islandCard's index
     * @return the islandCard at that index
     */
    public IslandCard getIsland(int index){
        return this.islandCards.get(index);
    }

    /**
     *
     * @param index the index of the islandCard to be removed
     */
    public void removeIsland(int index){
        this.islandCards.remove(index);
    }

    /**
     * Notify the views the modified islandCard
     * @param island the islandCard
     * @param students the list of students to add on the islandCard
     */
    public void movePawnOnIsland(IslandCard island, List<PawnColor> students) {
        island.movePawnOnIsland(students);

        notifyIslandCard(island);
    }

    /**
     * Notify the views the modified islandCard
     * @param island the islandCard
     * @param students the student to add on the islandCard
     */
    public void movePawnOnIsland(IslandCard island, PawnColor students) {
        island.movePawnOnIsland(students);

        notifyIslandCard(island);
    }

    /**
     *
     * @param uuid the uuid of the islandCard
     * @return the islandCard with that uuid
     * @throws WrongUUIDException if on the table doesn't exist an islandCard with that uuid
     */
    public IslandCard getIsland(UUID uuid) throws WrongUUIDException {
        for (IslandCard island : this.islandCards) {
            if (island.getUuid().equals(uuid)) {
                return island;
            }
        }
        throw new WrongUUIDException();
    }

    /**
     * Notify the views the modified islandCard
     * @param island the islandCard
     * @param tower the Tower
     */
    public void setTower(IslandCard island, Tower tower) {
        island.setTower(tower);

        notifyIslandCard(island);
    }

    /**
     * Notify the views the modified cloudTile
     * @param cloud the cloudTile
     * @param students the list of students to add on the cloudTile
     */
    public void addStudents(CloudTile cloud, List<PawnColor> students) {
        cloud.addStudents(students);

        notifyCloudTile(cloud);
    }

    /**
     * Notify the views the modified cloudTile
     * @param cloud the cloudTile
     * @return the list of students on the cloudTile
     */
    public List<PawnColor> takeStudentsFromCloud(CloudTile cloud) {
        List<PawnColor> retVal = cloud.takeStudentsFromCloud();

        notifyCloudTile(cloud);
        return retVal;
    }

    /**
     *
     * @return the index of the islandCard with mother nature
     */
    public int getIslandWithMotherNature() {
        int i;
        for(i = 0; i < islandCards.size(); i++){
            if(getIsland(i).isHasMotherNature())
                return i;
        }
        return -1;
    }

    /**
     *
     * @return the list of cloudTiles on the table
     */
    public List<CloudTile> getCloudTiles() {
        return this.cloudTiles;
    }

    /**
     *
     * @return the number of islandCards on the table
     */
    public int countIsland() {
        return islandCards.size();
    }

    /**
     *
     * @return the list of professors on the table
     */
    public List<PawnColor> getProfessors() {
        return this.professors;
    }

    /**
     *
     * @return the number of players
     */
    public PlayersNumber getPlayersNumber() {
        return this.playersNumber;
    }

    /**
     *
     * Notify the views the modified islandCards
     * @param index the index of the islandCard that now has mother nature
     */
    public void setIslandWithMotherNature(int index) {
        int oldPosition = getIslandWithMotherNature();
        this.getIsland(getIslandWithMotherNature()).setHasMotherNature(false);

        notifyIslandCard(getIsland(oldPosition));
        this.getIsland(index).setHasMotherNature(true);
        notifyIslandCard(getIsland(getIslandWithMotherNature()));
    }

    /**
     * The connected IslandCard is removed from the table
     * Notify the views the modified islandCard
     * @param originalIndex the index of the islandCard to which the other is connected
     * @param otherIndex the index of the islandCard to be connected
     */
    public void unifyIslands(int originalIndex, int otherIndex){
        IslandCard islandCard = getIsland(originalIndex);
        islandCard.unifyWith(getIsland(otherIndex));
        removeIsland(otherIndex);
        notifyIslandCard(islandCard);
    }
}
