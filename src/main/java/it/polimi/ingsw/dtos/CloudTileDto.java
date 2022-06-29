package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CloudTile Dto
 */
public class CloudTileDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 102L;
    private final UUID uuid;
    private final ArrayList<PawnColor> students;

    /**
     * Construct a CloudTileDto from a CloudTile
     * @param origin the cloudTile
     */
    public CloudTileDto(CloudTile origin) {
        this.uuid = origin.getUuid();
        this.students = new ArrayList<>(origin.getStudentsFromCloud());
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
     * @return the list of pawnColor on the CloudTileDto
     */
    public List<PawnColor> getStudents() {
        return this.students;
    }
}
