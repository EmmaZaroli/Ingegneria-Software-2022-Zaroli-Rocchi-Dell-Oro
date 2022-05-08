package it.polimi.ingsw.model;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.enums.PawnColor;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameParametersTest extends TestCase {
    GameParameters gameParameters = new GameParameters(PlayersNumber.TWO, GameMode.NORMAL_MODE);
    ExpertGameParameters expertGameParameters = new ExpertGameParameters(PlayersNumber.TWO, GameMode.NORMAL_MODE);

    @Test
    void initValues() {
        gameParameters.setInitialStudentsCount(10);
        gameParameters.setInitialTowersCount(8);
        gameParameters.setStudentsToDraw(3);
        gameParameters.setStudentsToMove(3);
        Assertions.assertEquals(10, gameParameters.getInitialStudentsCount());
        Assertions.assertEquals(8, gameParameters.getInitialTowersCount());
        Assertions.assertEquals(3, gameParameters.getStudentsToDraw());
        Assertions.assertEquals(3, gameParameters.getStudentsToMove());
    }

    @Test
    void expertInitValues() {
        expertGameParameters.setInitialStudentsCount(10);
        expertGameParameters.setMotherNatureExtraMovements(1);
        expertGameParameters.setTowersCountInInfluence(true);
        expertGameParameters.setExtraInfluence(2);
        expertGameParameters.setColorWithNoInfluence(PawnColor.RED);
        expertGameParameters.setTakeProfessorEvenIfSameStudents(true);
        Assertions.assertEquals(10, expertGameParameters.getInitialStudentsCount());
        Assertions.assertEquals(2, expertGameParameters.getExtraInfluence());
        Assertions.assertTrue(expertGameParameters.isTowersCountInInfluence());
        Assertions.assertEquals(1, expertGameParameters.getMotherNatureExtraMovements());
        Assertions.assertEquals(PawnColor.RED, expertGameParameters.getColorWithNoInfluence());
        Assertions.assertTrue(expertGameParameters.isTakeProfessorEvenIfSameStudents());
    }
}
