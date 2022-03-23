package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

import java.util.EnumMap;

public class DiningRoom {
    private final EnumMap<PawnColor, Integer> students;

    public DiningRoom() {
        this.students = new EnumMap<>(PawnColor.class);
    }

    //Adds a student and returns true if the player is supposed to take one coin from the table
    public boolean addStudent(PawnColor color) {
        int studentsOfSelectedColor = this.students.get(color);
        studentsOfSelectedColor++;
        this.students.replace(color, studentsOfSelectedColor);
        return studentsOfSelectedColor % 3 == 0;
    }

    public boolean canAddStudent(PawnColor color) {
        //TODO will be used by the controller to throw exceptions
        return this.students.get(color) < 10;
    }

    public int getStudents(PawnColor color) {
        return this.students.get(color);
    }
}
