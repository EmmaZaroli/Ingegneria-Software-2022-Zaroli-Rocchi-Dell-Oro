package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * IslandCard Dto
 */
public class IslandCardDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 106L;
    private UUID uuid;
    private List<PawnColor> students;
    private Tower tower;
    private boolean hasMotherNature;
    private List<Integer> indices = new ArrayList<>();

    /**
     * Construct the islandCardDto from an islandCard
     * @param origin the characterCard
     */
    public IslandCardDto(IslandCard origin) {
        this.uuid = origin.getUuid();
        this.students = new ArrayList<>(origin.getStudentsFromIsland());
        this.tower = origin.getTower();
        this.hasMotherNature = origin.isHasMotherNature();
        this.indices.addAll(origin.getIndices());
    }

    /**
     * Default constructor
     */
    public IslandCardDto(UUID uuid, List<PawnColor> students, Tower tower, boolean hasMotherNature, List<Integer> indices) {
        this.uuid = uuid;
        this.students = students;
        this.tower = tower;
        this.hasMotherNature = hasMotherNature;
        this.indices.addAll(indices);
    }

    /**
     * Default constructor
     */
    public IslandCardDto(){
        this.uuid = UUID.randomUUID();
        this.students = new ArrayList<>();
        this.tower = Tower.NONE;
        this.hasMotherNature = false;
        this.indices = new ArrayList<>();
    }

    /**
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     *
     * @return the list of pawnColors on the islandCardDto
     */
    public List<PawnColor> getStudents() {
        return this.students;
    }

    /**
     *
     * @return the Tower
     */
    public Tower getTower() {
        return tower;
    }

    /**
     *
     * @return the number of island to witch it's connected to
     */
    public int getSize() {
        return indices.size();
    }

    /**
     *
     * @return true if the islandCardDto has mother nature, false otherwise
     */
    public boolean isHasMotherNature() {
        return hasMotherNature;
    }

    /**
     *
     * @return the list of islands' indexes to witch this islandCard it's connected to
     */
    public List<Integer> getIndices() {
        return indices;
    }

    /**
     *
     * @return a clone of this instance
     */
    public IslandCardDto deepClone() {
        return new IslandCardDto(this.uuid, new ArrayList<>(this.students), this.tower, this.hasMotherNature, new ArrayList<>(this.indices));
    }

    /**
     *
     * @param uuid the new uuid
     * @return a clone of this instance with the updated uuid
     */
    public IslandCardDto withUuid(UUID uuid) {
        IslandCardDto retVal = this.deepClone();
        retVal.uuid = uuid;
        return retVal;
    }

    /**
     *
     * @param students the new list of pawnColors on the islandCard
     * @return a clone of this instance with the updated pawnColors
     */
    public IslandCardDto withStudents(List<PawnColor> students) {
        IslandCardDto retVal = this.deepClone();
        retVal.students = students;
        return retVal;
    }

    /**
     *
     * @return a clone of this instance without the pawnColors
     */
    public IslandCardDto withoutStudents() {
        IslandCardDto retVal = this.deepClone();
        retVal.students = new ArrayList<>();
        return retVal;
    }

    /**
     *
     * @param tower the new Tower
     * @return a clone of this instance with the updated tower
     */
    public IslandCardDto withTower(Tower tower) {
        IslandCardDto retVal = this.deepClone();
        retVal.tower = tower;
        return retVal;
    }

    /**
     *
     * @param hasMotherNature true if the islandCard has motherNature
     * @return a clone of this instance with the updated motherNature boolean
     */
    public IslandCardDto withMotherNature(boolean hasMotherNature) {
        IslandCardDto retVal = this.deepClone();
        retVal.hasMotherNature = hasMotherNature;
        return retVal;
    }

    /**
     *
     * @param index the new index of the islandCard to which this instance is now connected to
     * @return a clone of this instance with the updated indexes
     */
    public IslandCardDto withIndexes(Integer index) {
        IslandCardDto retVal = this.deepClone();
        retVal.indices = new LinkedList<>();
        retVal.indices.add(index);
        return retVal;
    }
}
