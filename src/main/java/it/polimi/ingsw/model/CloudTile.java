package it.polimi.ingsw.model;

import java.util.*;

import it.polimi.ingsw.model.enums.*;

/**
 * class Cloud tile.
 */
public class CloudTile {
    //TODO probably it is better to place this info somewhere else
    private final PlayerCountIcon playerCountIcon;
    private final List<PawnColor> students;

    /**
     * Instantiates a new Cloud tile.
     *
     * @param playerCountIcon the player count icon
     */
    public CloudTile(PlayerCountIcon playerCountIcon) {
        this.playerCountIcon = playerCountIcon;
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
