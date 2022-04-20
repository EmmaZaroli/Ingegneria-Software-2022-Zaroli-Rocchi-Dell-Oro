package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CloudTileTest extends TestCase {

    @Test
    void takeStudentFromCloud() {
        CloudTile cloud = new CloudTile(java.util.UUID.randomUUID());
        List<PawnColor> students = new ArrayList<>();
        students.add(PawnColor.BLUE);
        students.add(PawnColor.RED);
        students.add(PawnColor.GREEN);
        cloud.addStudents(students);
        Assertions.assertEquals(cloud.takeStudentsFromCloud(), students);
    }


}
