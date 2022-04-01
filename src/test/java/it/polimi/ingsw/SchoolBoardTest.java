package it.polimi.ingsw;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import junit.framework.TestCase;
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
    void t1() {
        schoolBoard.addStudentsToEntrance(this.studentsGenerator());
        assertEquals(7, schoolBoard.countStudentsInEntrance());
        schoolBoard.moveStudentFromEntranceToDiningRoom(PawnColor.RED);
        if (schoolBoard.isStudentInEntrance(PawnColor.RED))
            schoolBoard.moveStudentFromEntranceToDiningRoom(PawnColor.RED);
        assertEquals(1, schoolBoard.getStudentsInDiningRoom(PawnColor.RED));
    }
}
