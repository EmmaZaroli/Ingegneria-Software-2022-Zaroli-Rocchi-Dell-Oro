package it.polimi.ingsw.model;
import java.util.*;
import it.polimi.ingsw.model.enums.*;

public class CloudTile {
    private PlayerCountIcon playerCountIcon;
    private final List<PawnColor> students;

    public CloudTile(PlayerCountIcon playerCountIcon) {
        this.playerCountIcon = playerCountIcon;
        students = new ArrayList<>();
    }

    //method to add students (draft)
    public void AddStudents(List<PawnColor> student) {
        students.addAll(student);
    }

    //method to return the students in a cloud
    public List<PawnColor> takeStudentsFromCloud() {
        List<PawnColor> copy = new ArrayList<>(students);
        this.clearCloud();
        return copy;
    }

    //method to remove students (draft)
    private void clearCloud(){
        students.clear();
    }

}
