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

    //TODO add an identifier?...Not only to this class
    private final ArrayList<PawnColor> students;

    /**
     * Instantiates a new Cloud tile.
     */
    public CloudTile() {
        students = new ArrayList<>();
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
}
