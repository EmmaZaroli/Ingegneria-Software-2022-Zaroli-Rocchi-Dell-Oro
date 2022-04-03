package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

class DiningRoomTest extends TestCase {

    DiningRoom diningRoom = new DiningRoom();

    @Test
    void getCoin() {
        boolean b;
        for (int i = 1; diningRoom.canAddStudent(PawnColor.BLUE); i++) {
            b = i % 3 == 0;
            Assertions.assertEquals(b, diningRoom.addStudent(PawnColor.BLUE));
        }
        Assertions.assertFalse(diningRoom.addStudent(PawnColor.RED));
        Assertions.assertFalse(diningRoom.addStudent(PawnColor.RED));
        Assertions.assertFalse(diningRoom.addStudent(PawnColor.PINK));
    }

    @Test
    void getStudent() {
        Assertions.assertEquals(0, diningRoom.getStudents(PawnColor.BLUE));
        this.getCoin();
        Assertions.assertEquals(10, diningRoom.getStudents(PawnColor.BLUE));
        Assertions.assertEquals(1, diningRoom.getStudents(PawnColor.PINK));
        Assertions.assertEquals(2, diningRoom.getStudents(PawnColor.RED));
        Assertions.assertEquals(0, diningRoom.getStudents(PawnColor.YELLOW));
        Assertions.assertEquals(0, diningRoom.getStudents(PawnColor.GREEN));
    }
}
