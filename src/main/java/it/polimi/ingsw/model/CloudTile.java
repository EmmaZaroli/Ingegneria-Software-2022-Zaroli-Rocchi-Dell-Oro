package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.observer.Observable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * class Cloud tile.
 */
public class CloudTile extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;
    private final UUID uuid;
    private final ArrayList<PawnColor> students;

    /**
     * Instantiates a new Cloud tile.
     */
    public CloudTile(UUID uuid) {
        this.uuid = uuid;
        students = new ArrayList<>();
    }

    /**
     * @return the uuid of the cloudTile
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Add students on the cloudTile
     *
     * @param student A list of students to place on the cloud
     */
    protected void addStudents(List<PawnColor> student) {
        students.addAll(student);
    }

    /**
     * Remove students from cloudTile.
     *
     * @return list of students
     */
    protected List<PawnColor> takeStudentsFromCloud() {
        List<PawnColor> copy = new ArrayList<>(students);
        this.students.clear();
        notify(this);
        return copy;
    }
}
