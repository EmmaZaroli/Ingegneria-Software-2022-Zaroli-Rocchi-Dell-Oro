package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.LinkedList;
import java.util.List;

public class SchoolBoard {
    private int towers;
    private final Tower towerColor;
    private final Entrance entrance;
    private final DiningRoom diningRoom;
    private final ProfessorTable professorTable;

    public SchoolBoard(int towers, Tower towerColor) {
        this.towers = towers;
        this.towerColor = towerColor;
        this.entrance = new Entrance();
        this.diningRoom = new DiningRoom();
        this.professorTable = new ProfessorTable();
    }

    public int getTowers() {
        return towers;
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public int getStudentsInEntrance(PawnColor color) {
        return entrance.getStudents(color);
    }

    public int getStudentsInDinigRoom(PawnColor color) {
        return diningRoom.getStudents(color);
    }

    public boolean isThereProfessor(PawnColor color) {
        return professorTable.isThereProfessor(color);
    }

    public void addTowers(int n) {
        towers += n;
        //TODO controllo che non superi il limite
    }

    public void removeTower() {
        towers--;
        //TODO controllo che non scenda sotto zero
    }

    public void addStudentToEntrance(List<PawnColor> color) {
        //TODO
        //entrance.addStudent(color);
    }

    public void removeStudentFromEntrance(PawnColor color) {
        entrance.removeStudent(color);
    }

    public void addStudentToDiningRoom(PawnColor color) {
        diningRoom.getStudents(color);
    }

    public void addProfessor(PawnColor color) {
        professorTable.addProfessor(color);
    }

    public void removeProfessor(PawnColor color) {
        professorTable.removeProfessor(color);
    }

    public void moveStudentFromEntranceToDiningRoom(PawnColor student) {
        //TODO implement
        //removeStudentFromEntrance(color);
        //TODO gestire caso in cui il colore non è presente in entrance
        //addStudentToDiningRoom(color);
    }

    public List<PawnColor> getProfessors(){
        return professorTable.getProfessors();
    }
}
