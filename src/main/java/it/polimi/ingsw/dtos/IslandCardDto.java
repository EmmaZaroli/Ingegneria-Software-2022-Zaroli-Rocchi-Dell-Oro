package it.polimi.ingsw.dtos;

import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class IslandCardDto implements Serializable {
    private static final long serialVersionUID = 106L;
    private UUID uuid;
    private List<PawnColor> students;
    private Tower tower;
    private boolean hasMotherNature;
    private List<Integer> indices;

    public IslandCardDto(IslandCard origin) {
        this.uuid = origin.getUuid();
        this.students = new ArrayList<>(origin.getStudentsFromIsland());
        this.tower = origin.getTower();
        this.hasMotherNature = origin.isHasMotherNature();
        this.indices = origin.getIndices();
    }

    public IslandCardDto(UUID uuid, List<PawnColor> students, Tower tower, boolean hasMotherNature, List<Integer> indices) {
        this.uuid = uuid;
        this.students = students;
        this.tower = tower;
        this.hasMotherNature = hasMotherNature;
        this.indices = indices;
    }

    public IslandCardDto(){
        this.uuid = UUID.randomUUID();
        this.students = new ArrayList<>();
        this.tower = Tower.NONE;
        this.hasMotherNature = false;
        this.indices = new ArrayList<>();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public List<PawnColor> getStudents() {
        return this.students;
    }

    public Tower getTower() {
        return tower;
    }

    public int getSize() {
        return indices.size();
    }

    public boolean isHasMotherNature() {
        return hasMotherNature;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public IslandCardDto deepClone() {
        //TODO dtos if we have time
        return new IslandCardDto(this.uuid, new ArrayList<>(this.students), this.tower, this.hasMotherNature, new ArrayList<>(this.indices));
    }

    public IslandCardDto withUuid(UUID uuid) {
        IslandCardDto retVal = this.deepClone();
        retVal.uuid = uuid;
        return retVal;
    }

    public IslandCardDto withStudents(List<PawnColor> students) {
        IslandCardDto retVal = this.deepClone();
        retVal.students = students;
        return retVal;
    }

    public IslandCardDto withTower(Tower tower) {
        IslandCardDto retVal = this.deepClone();
        retVal.tower = tower;
        return retVal;
    }

    public IslandCardDto withMotherNature(boolean hasMotherNature) {
        IslandCardDto retVal = this.deepClone();
        retVal.hasMotherNature = hasMotherNature;
        return retVal;
    }

    public IslandCardDto withIndeces(List<Integer> indeces) {
        IslandCardDto retVal = this.deepClone();
        retVal.indices = indeces;
        return retVal;
    }

    public IslandCardDto withIndeces(Integer index) {
        IslandCardDto retVal = this.deepClone();
        retVal.indices = new LinkedList<>();
        retVal.indices.add(index);
        return retVal;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }
}
