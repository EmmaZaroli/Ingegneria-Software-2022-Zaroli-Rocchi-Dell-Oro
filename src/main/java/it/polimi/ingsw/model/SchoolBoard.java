package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.*;

public class SchoolBoard {
    private final Tower towerColor;
    private final List<PawnColor> entrance;
    private final DiningRoom diningRoom;
    private final Set<PawnColor> professorTable;
    private int towers;

    public SchoolBoard(int towers, Tower towerColor) {
        this.towers = towers;
        this.towerColor = towerColor;
        this.entrance = new ArrayList<>();
        this.diningRoom = new DiningRoom();
        this.professorTable = new HashSet<>();
    }

    public void addProfessor(PawnColor color) {
        professorTable.add(color);
    }

    public boolean addStudentToDiningRoom(PawnColor color) {
        return diningRoom.addStudent(color);
    }

    public void addStudentsToEntrance(List<PawnColor> color) {
        entrance.addAll(color);
    }

    public void addTowers(int n) {
        towers += n;
    }

    public int countProfessors() {
        return professorTable.size();
    }

    public int countStudentsInEntrance() {
        return this.entrance.size();
    }

    public List<PawnColor> getProfessors() {
        return this.professorTable.stream().toList();
    }

    public int getStudentsInDiningRoom(PawnColor color) {
        return diningRoom.getStudents(color);
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public int getTowersCount() {
       return this.towers;
    }

    public boolean isThereProfessor(PawnColor color) {
        return professorTable.contains(color);
    }

    public boolean moveStudentFromEntranceToDiningRoom(PawnColor student) {
        removeStudentFromEntrance(student);
        return addStudentToDiningRoom(student);
    }

    public boolean removeProfessor(PawnColor color) {
        return professorTable.remove(color);
    }

    public void removeStudentFromEntrance(PawnColor color) {
        entrance.remove(color);
    }

    public void removeTower() {
        towers--;
    }
    
    public boolean isStudentInEntrance(PawnColor color) {
        return entrance.contains(color);
    }
}
