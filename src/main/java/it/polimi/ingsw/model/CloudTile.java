package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.model.enums.*;

/**
 * class Cloud tile.
 */
public class CloudTile implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;

    //TODO probably it is better to place this info somewhere else
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
     * @param student
     */
//method to add students
    public void addStudents(List<PawnColor> student) {
        students.addAll(student);
    }

    /**
     * Remove students from cloudTile.
     *
     * @return list of students
     */
//method to return the students in a cloud
    public List<PawnColor> takeStudentsFromCloud() {
        List<PawnColor> copy = new ArrayList<>(students);
        this.students.clear();
        return copy;
    }
}
