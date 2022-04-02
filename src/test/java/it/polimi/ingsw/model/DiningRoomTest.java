package it.polimi.ingsw.model;


import it.polimi.ingsw.model.DiningRoom;
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
            assertEquals(b, diningRoom.addStudent(PawnColor.BLUE));
        }
        assertFalse(diningRoom.addStudent(PawnColor.RED));
        assertFalse(diningRoom.addStudent(PawnColor.RED));
        assertFalse(diningRoom.addStudent(PawnColor.PINK));
    }

    @Test
    void getStudent() {
        assertEquals(0, diningRoom.getStudents(PawnColor.BLUE));
        this.getCoin();
        assertEquals(10, diningRoom.getStudents(PawnColor.BLUE));
        assertEquals(1, diningRoom.getStudents(PawnColor.PINK));
        assertEquals(2, diningRoom.getStudents(PawnColor.RED));
        assertEquals(0, diningRoom.getStudents(PawnColor.YELLOW));
        assertEquals(0, diningRoom.getStudents(PawnColor.GREEN));
    }
}
