package it.polimi.ingsw.model;

import java.util.*;
import it.polimi.ingsw.model.enums.*;


public class IslandCard {
    private boolean hasMotherNature;
    private final List<PawnColor> students;
    private Tower tower;
    private int size;

    public IslandCard() {
        this.students = new ArrayList<>();
        tower = Tower.NONE;
        size=1;
        hasMotherNature=false;
    }

    public void setMotherNatureHere(boolean b) {
        hasMotherNature=b;
    }

    public void movePawnOnIsland(PawnColor student){
        students.add(student);
    }

    public int countInfluence(List<PawnColor> professors, Tower towerColor) {
        int influence = 0;
        for (PawnColor p : students) {
            if (professors.contains(p)) {
                influence++;
            }
        }
        if (towerColor.equals(tower)) {
            influence++;
        }
        return influence;
    }

    public int getSize() {
        return size;
    }
    public Tower getTower() {
        return tower;
    }
    public void setTower(Tower tower) {
        this.tower=tower;
    }
}
