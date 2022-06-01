package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.RandomHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * A class representing the bag
 */
public class Bag implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;

    private static final int PAWNS_PER_COLOR = 24;

    private final EnumMap<PawnColor, Integer> students;
    private int pawnCount;

    /**
     * Builds the new bag
     */
    protected Bag() {
        this.students = new EnumMap<>(PawnColor.class);
        for (PawnColor pc : PawnColor.values()) {
            if (pc != PawnColor.NONE) {
                students.put(pc, PAWNS_PER_COLOR);
                this.pawnCount += PAWNS_PER_COLOR;
            }
        }
    }

    /**
     * Draws a student and removes it from the bag
     *
     * @return The drawn student
     */
    public PawnColor drawStudent() {
        RandomHelper randomHelper = RandomHelper.getInstance();
        if (pawnCount > 0) {
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
        }
        return PawnColor.NONE;
    }

    //Removes a pawn from the bag
    private void removePawn(PawnColor c) {
        Integer pawnColorCount = this.students.get(c);
        pawnColorCount = pawnColorCount - 1;
        this.students.replace(c, pawnColorCount);
        this.pawnCount--;
    }

    /**
     * @return True if the bag is empty
     */
    public boolean isEmpty() {
        for (Map.Entry<PawnColor, Integer> e : students.entrySet()) {
            if (e.getValue() > 0)
                return false;
        }
        return true;
    }

    /**
     * @return numver of students left in bag
     */
    public int getStudentsLeft(){
        int res = 0;
        for (Map.Entry<PawnColor, Integer> e : students.entrySet()) {
            res += e.getValue();
        }
        return res;
    }
}
