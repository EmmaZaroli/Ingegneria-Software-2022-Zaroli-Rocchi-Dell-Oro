package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    void drawAllStudents() {
        EnumMap<PawnColor, Integer> drawnStudents = new EnumMap<>(PawnColor.class);
        for (PawnColor pc : PawnColor.values()) {
            drawnStudents.put(pc, 0);
        }
        Bag b = new Bag();

        while(! b.isEmpty()) {
            PawnColor p = b.drawStudent();
            Integer count = drawnStudents.get(p);
            count++;
            drawnStudents.replace(p, count);
        }

        assertEquals(24, drawnStudents.get(PawnColor.RED));
        assertEquals(24, drawnStudents.get(PawnColor.BLUE));
        assertEquals(24, drawnStudents.get(PawnColor.YELLOW));
        assertEquals(24, drawnStudents.get(PawnColor.GREEN));
        assertEquals(24, drawnStudents.get(PawnColor.PINK));

        //This doesn't make sense but makes 100% coverage on the model
        assertThrows(Exception.class, () -> b.drawStudent());
    }
}