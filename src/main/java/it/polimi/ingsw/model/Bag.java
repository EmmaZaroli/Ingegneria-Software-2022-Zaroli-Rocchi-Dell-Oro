package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.utils.RandomHelper;

import java.util.EnumMap;
import java.util.Map;

public class Bag {
    private final Map<PawnColor, Integer> students;
    private int pawnCount;

    public Bag() {
        this.students = new EnumMap<>(PawnColor.class);
        for (PawnColor pc : PawnColor.values()) {
            students.put(pc, 0);
        }

        this.pawnCount = getPawnCount();
    }

    protected void addStudents(PawnColor student, int size) {
        Integer studentsCount = this.students.get(student);
        studentsCount += size;
        this.students.replace(student, studentsCount);
    }

    protected PawnColor drawStudent() {
        RandomHelper randomHelper = RandomHelper.getInstance();

        int random = randomHelper.getInt(pawnCount);
        int pos = 0;

        for (Map.Entry<PawnColor, Integer> e : students.entrySet()) {
            pos += e.getValue();
            if (pos >= random) {
                pawnCount--;
                return e.getKey();
            }
        }
        //Random value, this statement should not be reached
        return PawnColor.RED;
    }

    private int getPawnCount() {
        return students
                .entrySet()
                .stream()
                .reduce(0, (tmp, element) -> tmp + element.getValue(), Integer::sum);
    }
}
