package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.observer.ModelObservable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * School board.
 */
public class SchoolBoard extends ModelObservable implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    private final Tower towerColor;
    private final ArrayList<PawnColor> entrance;
    private final DiningRoom diningRoom;
    private final HashSet<PawnColor> professorTable;
    private int towers;
    private final UUID uuid;

    /**
     * Instantiates a new School board.
     *
     * @param towers     the number of towers to add
     * @param towerColor the color of the player's towers
     */
    public SchoolBoard(int towers, Tower towerColor) {
        this.towers = towers;
        this.towerColor = towerColor;
        this.entrance = new ArrayList<>();
        this.diningRoom = new DiningRoom();
        this.professorTable = new HashSet<>();
        this.uuid = java.util.UUID.randomUUID();
    }

    /**
     * Add professor you now control
     * Notify the views the changed schoolBoard
     * @param color the professor's color
     */
    public void addProfessor(PawnColor color) {
        professorTable.add(color);
        notifySchoolBoard(this);
    }

    /**
     * Adds a student on the row corresponding to the color of the student in the dining room
     * Notify the views the changed schoolBoard
     * @param color the color of the student
     * @return true if the player is supposed to take one coin from the table, false otherwise
     */
    public boolean addStudentToDiningRoom(PawnColor color) {
        boolean retVal = diningRoom.addStudent(color);
        notifySchoolBoard(this);
        return retVal;
    }

    /**
     * Add a list of students to the entrance.
     * Notify the views the changed schoolBoard
     * @param color the colors of the students
     */
    public void addStudentsToEntrance(List<PawnColor> color) {
        entrance.addAll(color);
        notifySchoolBoard(this);
    }

    /**
     * Add n towers to the school board
     * Notify the views the changed schoolBoard
     * @param n the number of tower to add
     */
    public void addTowers(int n) {
        towers += n;
        notifySchoolBoard(this);
    }

    /**
     * Counts the number of professors the player has
     *
     * @return the number of professors
     */
    public int getProfessorsCount() {
        return professorTable.size();
    }

    /**
     * Count students in entrance
     *
     * @return students in entrance
     */
    public int countStudentsInEntrance() {
        return this.entrance.size();
    }

    /**
     * Return the list of professors the player has
     *
     * @return the professors
     */
    public Set<PawnColor> getProfessors() {
        return this.professorTable;
    }

    /**
     * Gets students in dining room.
     *
     * @param color the color
     * @return the students in dining room
     */
    public int getStudentsInDiningRoom(PawnColor color) {
        return diningRoom.getStudents(color);
    }

    /**
     * Gets player's towers color
     *
     * @return the tower color
     */
    public Tower getTowerColor() {
        return towerColor;
    }

    /**
     * Gets the number of towers
     *
     * @return the towers count
     */
    public int getTowersCount() {
        return this.towers;
    }

    /**
     * check if the player has the professor of that color
     *
     * @param color the color of the professor
     * @return true if the player has the professor of that color, false otherwise
     */
    public boolean isThereProfessor(PawnColor color) {
        return professorTable.contains(color);
    }

    /**
     * Adds a student int the dining room on the row corresponding to the color of the student
     *
     * @param student the color of the student
     * @return true if the player is supposed to take one coin from the table, false otherwise
     */
    public boolean moveStudentFromEntranceToDiningRoom(PawnColor student) {
        removeStudentFromEntrance(student);
        return addStudentToDiningRoom(student);
    }

    /**
     * Remove professor from the player
     * Notify the views the changed schoolBoard
     *
     * @param color the color of the professor to remove from the schoolBoard
     */
    public void removeProfessor(PawnColor color) {
        professorTable.remove(color);
        notifySchoolBoard(this);
    }

    /**
     * Remove student from entrance
     * Notify the views the changed schoolBoard
     *
     * @param color the color of the student
     */
    public void removeStudentFromEntrance(PawnColor color) {
        entrance.remove(color);
        notifySchoolBoard(this);
    }

    /**
     * Remove one tower from the school board
     * Notify the views the changed schoolBoard
     */
    public void removeTower() {
        towers--;
        notifySchoolBoard(this);
    }

    /**
     * check if the entrance has a student of that color
     *
     * @param color the color of the student
     * @return true if there's a student of that color, false otherwise
     */
    public boolean isStudentInEntrance(PawnColor color) {
        return entrance.contains(color);
    }

    /**
     * @return the students in the entrance
     */
    public List<PawnColor> getEntrance() {
        return this.entrance;
    }

    /**
     *
     * @param color the color of the student
     * @return the number of students of that color in the entrance
     */
    public int getStudentsInEntrance(PawnColor color){
        return (int) entrance.stream().filter(x -> x.equals(color)).count();
    }

    /**
     *
     * @return the number of students in the entrance
     */
    public int getStudentsInEntrance(){
        return entrance.size();
    }

    /**
     *
     * @return a map that maps to each student's color, their number of occurrences in the entrance
     */
    public Map<PawnColor, Integer> getStudentsInEntranceCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsInEntrance(color));
        }
        return res;
    }

    /**
     *
     * @return the uuid of the schoolBoard
     */
    public UUID getUuid(){
        return this.uuid;
    }
}
