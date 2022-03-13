package it.polimi.ingsw.model;
import java.util.*;
import it.polimi.ingsw.model.enums.*;

public class CloudTile {
    private PlayerCountIcon playerCountIcon;
    private List<PawnColor> students;

    public CloudTile(PlayerCountIcon playerCountIcon) {
        this.playerCountIcon = playerCountIcon;
        students = new ArrayList<>();
    }

    //method to add students (draft)
    public void AddStudents(List<PawnColor> students){
        for(PawnColor c : students){
            this.students.add(c);
        }
    }

    //method to return the students in a cloud
    public List<PawnColor> takeStudentsFromCloud(){
        return students;
    }

    //method to remove students (draft)
    public void clearCloud(){
        students.clear();
    }

}
