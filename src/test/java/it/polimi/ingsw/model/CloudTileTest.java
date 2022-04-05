package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import java.util.*;

class CloudTileTest extends TestCase {


    @Test
    void TakeStudentFromCloud() {
        CloudTile cloud = new CloudTile(PlayerCountIcon.TWO_FOUR);
        List<PawnColor> students = new ArrayList<>();
        students.add(PawnColor.BLUE);
        students.add(PawnColor.RED);
        students.add(PawnColor.GREEN);
        cloud.addStudents(students);
        Assertions.assertEquals(cloud.takeStudentsFromCloud(), students);
    }


}
