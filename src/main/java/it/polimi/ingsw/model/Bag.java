package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.RandomHelper;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Bag {
    private static final int PAWNS_PER_COLOR = 24;

    private final Map<PawnColor, Integer> students;
    private int pawnCount;

    public Bag() {
        this.students = new EnumMap<>(PawnColor.class);
        for (PawnColor pc : PawnColor.values()) {
            students.put(pc, PAWNS_PER_COLOR);
            this.pawnCount += PAWNS_PER_COLOR;
        }
    }

    private PawnColor drawStudent() {
        RandomHelper randomHelper = RandomHelper.getInstance();
        //randomHelper.getInt(n) returns an integer in [0, n), so we need to add 1
        int random = randomHelper.getInt(pawnCount) + 1;
        int pos = 0;

        for (Map.Entry<PawnColor, Integer> e : students.entrySet()) {
            pos += e.getValue();
            if (random <= pos) {
                removePawn(e.getKey());
                return e.getKey();
            }
        }
        //Random value, this statement should not be reached
        return PawnColor.RED;
    }

    public List<PawnColor> drawStudents(int n) {
        List<PawnColor> drawnStudents = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            drawnStudents.add(drawStudent());
        }

        return drawnStudents;
    }

    //Removes a pawn from the bag
    private void removePawn(PawnColor c) {
        Integer pawnColorCount = this.students.get(c);
        pawnColorCount = pawnColorCount - 1;
        this.students.replace(c, pawnColorCount);
        this.pawnCount--;
    }

    public boolean isEmpty(){
        for (Map.Entry<PawnColor, Integer> e : students.entrySet()){
            if(e.getValue() > 0)
                return false;
        }
        return true;
    }
}
