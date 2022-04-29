package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.observer.Observable;

/**
 * class Cloud tile.
 */
public class CloudTile extends Observable<CloudTile> implements Serializable {
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
    public void addStudents(List<PawnColor> student) {
        students.addAll(student);
        notify(this);
    }

    /**
     * Remove students from cloudTile.
     *
     * @return list of students
     */
    public List<PawnColor> takeStudentsFromCloud() {
        List<PawnColor> copy = new ArrayList<>(students);
        this.students.clear();
        notify(this);
        return copy;
    }

    /**
     * @return the list of student on the cloud
     */
    public List<PawnColor> getStudentsFromCloud() {
        return (List<PawnColor>) students.clone();
    }
}
