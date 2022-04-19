package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;

/**
 * Dining room.
 */
public class DiningRoom implements Serializable {
    @Serial
    private static final long serialVersionUID = 7L;

    private final EnumMap<PawnColor, Integer> students;

    /**
     * Instantiates a new Dining room.
     */
    public DiningRoom() {
        this.students = new EnumMap<>(PawnColor.class);
        for (PawnColor p : PawnColor.values())
            students.put(p, 0);
    }

    /**
     * Adds a student on the row corresponding to the color of the student
     *
     * @param color the color of the student
     * @return true if the player is supposed to take one coin from the table, false otherwise
     */
    public boolean addStudent(PawnColor color) {
        int studentsOfSelectedColor = this.students.get(color);
        studentsOfSelectedColor++;
        this.students.replace(color, studentsOfSelectedColor);
        return studentsOfSelectedColor % 3 == 0;
    }

    /**
     * Used by the controller to determined if the player can still add a students
     * on the board
     *
     * @param color the color of the student that the player wants to add
     * @return false if the number of students its equals to 10, true otherwise
     */
    public boolean canAddStudent(PawnColor color) {
        return this.students.get(color) < 10;
    }

    /**
     * Gets the number of students of that color
     *
     * @param color the color of the students
     * @return the number of students
     */
    public int getStudents(PawnColor color) {
        return this.students.get(color);
    }
}
