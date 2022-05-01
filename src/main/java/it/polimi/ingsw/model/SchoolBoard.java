package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.observer.Observable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * School board.
 */
public class SchoolBoard extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    private final Tower towerColor;
    private final ArrayList<PawnColor> entrance;
    private final DiningRoom diningRoom;
    private final HashSet<PawnColor> professorTable;
    private int towers;

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
    }

    /**
     * Add professor you now control
     *
     * @param color the professor's color
     */
    public void addProfessor(PawnColor color) {
        professorTable.add(color);
    }

    /**
     * Adds a student on the row corresponding to the color of the student in the dining room
     *
     * @param color the color of the student
     * @return true if the player is supposed to take one coin from the table, false otherwise
     */
    public boolean addStudentToDiningRoom(PawnColor color) {
        return diningRoom.addStudent(color);
    }

    /**
     * Add a list of students to the entrance.
     *
     * @param color the colors of the students
     */
    public void addStudentsToEntrance(List<PawnColor> color) {
        entrance.addAll(color);
    }

    /**
     * Add n towers to the school board
     *
     * @param n the number of tower to add
     */
    public void addTowers(int n) {
        towers += n;
        notify(this);
    }

    /**
     * Counts the number of professors the player has
     *
     * @return the number of professors
     */
    public int countProfessors() {
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
     *
     * @param color the color of the professor
     * @return true if the specified element is present in the Set otherwise it returns false
     */
    public void removeProfessor(PawnColor color) {
        professorTable.remove(color);
    }

    /**
     * Remove student from entrance
     *
     * @param color the color of the student
     */
    public void removeStudentFromEntrance(PawnColor color) {
        entrance.remove(color);
    }

    /**
     * Remove one tower from the school board
     */
    public void removeTower() {
        towers--;
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
}
