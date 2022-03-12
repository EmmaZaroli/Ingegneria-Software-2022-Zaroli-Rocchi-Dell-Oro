package it.polimi.ingsw.model;

import java.util.*;
import it.polimi.ingsw.model.enums.*;


public class IslandCard {
    private boolean hasMotherNature;
    private List<PawnColor> students;
    /*
    private List<Tower> towers;
    private int size;
     */
    public IslandCard() {
        this.students = new ArrayList<>();
         /*
        towers=new ArrayList<>();
        size=1;
        */
    }

    public boolean isHasMotherNature() {
        return hasMotherNature;
    }

    public List<PawnColor> getStudents() {
        return students;
    }
}
