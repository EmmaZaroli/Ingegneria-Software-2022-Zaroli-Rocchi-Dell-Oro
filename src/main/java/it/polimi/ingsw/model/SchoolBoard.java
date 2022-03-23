package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;


import java.util.*;


public class SchoolBoard {
    private int towers;
    private final Tower towerColor;
    private final List<PawnColor> entrance;
    private final DiningRoom diningRoom;
    private final Set<PawnColor> professorTable;

    public SchoolBoard(int towers, Tower towerColor) {
        this.towers = towers;
        this.towerColor = towerColor;
        this.entrance = new ArrayList<>(7);
        this.diningRoom = new DiningRoom();
        this.professorTable = new HashSet<>();
    }

    public int getTowers() {
        return towers;
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public int getStudentsInEntrance(PawnColor color) {
        int numOfPawn = 0;
        for (PawnColor p : entrance) {
            if (p.equals(color)) numOfPawn++;
        }
        return numOfPawn;
    }

    public int getStudentsInDinigRoom(PawnColor color) {
        return diningRoom.getStudents(color);
    }

    public boolean isThereProfessor(PawnColor color) {
        return professorTable.contains(color);
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
        entrance.addAll(color);
    }

    public void removeStudentFromEntrance(PawnColor color) {
        entrance.remove(color);
    }

    public void addStudentToDiningRoom(PawnColor color) {
        diningRoom.getStudents(color);
    }

    public void addProfessor(PawnColor color) {
        professorTable.add(color);
    }

    //TODO exception if professor not present(?)
    public void removeProfessor(PawnColor color) {
        professorTable.remove(color);
    }

    public void moveStudentFromEntranceToDiningRoom(PawnColor student) {
        //TODO implement
        //removeStudentFromEntrance(color);
        //TODO gestire caso in cui il colore non è presente in entrance
        //addStudentToDiningRoom(color);
    }

    public List<PawnColor> getProfessors() {
        List<PawnColor> clone = new ArrayList<>(professorTable);
        return clone;
    }

    public int howManyProfessors(){
        return professorTable.howManyProfessors();
    }
}
