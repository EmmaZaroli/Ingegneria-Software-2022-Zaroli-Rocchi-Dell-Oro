package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloudTileDto implements Serializable {
    private static final long serialVersionUID = 102L;
    private final UUID uuid;
    private final ArrayList<PawnColor> students;

    public CloudTileDto(CloudTile origin) {
        this.uuid = origin.getUuid();
        this.students = new ArrayList<>(origin.getStudentsFromCloud());
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public List<PawnColor> gerStudents() {
        return this.students;
    }
}
