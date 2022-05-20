package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class IslandCardDto implements Serializable {
    private static final long serialVersionUID = 102L;
    private final UUID uuid;
    private final ArrayList<PawnColor> students;
    private final Tower tower;
    private final int size;
    private final boolean hasMotherNature;

    public IslandCardDto(IslandCard origin) {
        this.uuid = origin.getUuid();
        this.students = new ArrayList<>(origin.getStudentsFromIsland());
        this.tower = origin.getTower();
        this.size = origin.getSize();
        this.hasMotherNature = origin.isHasMotherNature();
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
        return size;
    }

    public boolean isHasMotherNature() {
        return hasMotherNature;
    }
}
