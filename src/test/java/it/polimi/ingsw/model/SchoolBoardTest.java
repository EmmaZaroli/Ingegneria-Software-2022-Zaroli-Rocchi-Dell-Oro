package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SchoolBoardTest extends TestCase {
    SchoolBoard schoolBoard = new SchoolBoard(8, Tower.BLACK);

    private List<PawnColor> studentsGenerator() {
        List<PawnColor> l1 = new ArrayList<>();
        l1.add(PawnColor.BLUE);
        l1.add(PawnColor.BLUE);
        l1.add(PawnColor.RED);
        l1.add(PawnColor.BLUE);
        l1.add(PawnColor.YELLOW);
        l1.add(PawnColor.GREEN);
        l1.add(PawnColor.PINK);
        return l1;
    }

    @Test
    void movingStudents() {
        schoolBoard.addStudentsToEntrance(this.studentsGenerator());
        Assertions.assertEquals(7, schoolBoard.countStudentsInEntrance());
        schoolBoard.moveStudentFromEntranceToDiningRoom(PawnColor.RED);
        if (schoolBoard.isStudentInEntrance(PawnColor.RED))
            schoolBoard.moveStudentFromEntranceToDiningRoom(PawnColor.RED);
        Assertions.assertEquals(1, schoolBoard.getStudentsInDiningRoom(PawnColor.RED));
    }

    private int countProfessor(PawnColor pawnColor) {
        int i = 0;
        for (PawnColor p : schoolBoard.getProfessors()) {
            if (p.equals(pawnColor)) i++;
        }
        return i;
    }

    @Test
    void movingProfessors() {
        schoolBoard.addProfessor(PawnColor.RED);
        Assertions.assertEquals(1, countProfessor(PawnColor.RED));
        if (!schoolBoard.isThereProfessor(PawnColor.RED)) {
            schoolBoard.addProfessor(PawnColor.RED);
        }
        Assertions.assertEquals(1, countProfessor(PawnColor.RED));
        Assertions.assertEquals(1, schoolBoard.getProfessorsCount());
        schoolBoard.removeProfessor(PawnColor.RED);
        Assertions.assertEquals(0, schoolBoard.getProfessorsCount());
    }

    @Test
    void movingTowers() {
        schoolBoard.addTowers(1);
        Assertions.assertEquals(9, schoolBoard.getTowersCount());
        Assertions.assertEquals(Tower.BLACK, schoolBoard.getTowerColor());
        schoolBoard.removeTower();
        Assertions.assertEquals(8, schoolBoard.getTowersCount());
    }
}
