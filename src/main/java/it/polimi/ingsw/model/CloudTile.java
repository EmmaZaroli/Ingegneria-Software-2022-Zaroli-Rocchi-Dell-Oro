package it.polimi.ingsw.model;

import java.util.*;

import it.polimi.ingsw.model.enums.*;

public class CloudTile {
    //TODO probably it is better to place this info somewhere else
    private PlayerCountIcon playerCountIcon;
    private final List<PawnColor> students;

    public CloudTile(PlayerCountIcon playerCountIcon) {
        this.playerCountIcon = playerCountIcon;
        students = new ArrayList<>();
    }

    //method to add students
    public void addStudents(List<PawnColor> student) {
        students.addAll(student);
    }

    //method to return the students in a cloud
    public List<PawnColor> takeStudentsFromCloud() {
        List<PawnColor> copy = new ArrayList<>(students);
        this.clearCloud();
        return copy;
    }

    //method to remove students
    private void clearCloud() {
        students.clear();
    }

}
