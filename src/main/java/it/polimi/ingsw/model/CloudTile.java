package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.observer.ModelObservable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * class Cloud tile.
 */
public class CloudTile extends ModelObservable implements Serializable {
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
        notifyCloudTile(this);
        return copy;
    }

    /**
     * @return the list of student on the cloud
     */
    public List<PawnColor> getStudentsFromCloud() {
        return (List<PawnColor>) students.clone();
    }

    /**
     *
     * @param color the color of the student
     * @return the number of students of that color on the cloudTile
     */
    public int getStudentsNumber(PawnColor color){
        return (int) students.stream().filter(x -> x.equals(color)).count();
    }

    /**
     *
     * @return the number of students on the cloudTile
     */
    public int getStudentsNumber(){
        return students.size();
    }

    /**
     *
     * @return a map that maps to each student's color, their number of occurrences on the card
     */
    public Map<PawnColor, Integer> getStudentsCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsNumber(color));
        }
        return res;
    }
}
