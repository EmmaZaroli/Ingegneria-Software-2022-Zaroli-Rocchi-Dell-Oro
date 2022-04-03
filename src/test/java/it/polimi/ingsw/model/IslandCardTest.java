package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.*;

class IslandCardTest extends TestCase {

    IslandCard islandCard1 = new IslandCard();


    private List<PawnColor> listPawnColor() {
        List<PawnColor> students = new ArrayList<>();
        students.add(PawnColor.BLUE);
        students.add(PawnColor.RED);
        students.add(PawnColor.GREEN);
        return students;
    }

    @Test
    void PawnOnIsland() {
        List<PawnColor> list = listPawnColor();
        islandCard1.movePawnOnIsland(list);
        Assertions.assertEquals(list, islandCard1.getStudentsFromIsland());
        islandCard1.movePawnOnIsland(PawnColor.BLUE);
        list.add(PawnColor.BLUE);
        Assertions.assertEquals(list, islandCard1.getStudentsFromIsland());
    }

    @Test
    void Tower() {
        islandCard1.setTower(Tower.BLACK);
        Assertions.assertEquals(Tower.BLACK, islandCard1.getTower());
    }

    @Test
    void size() {
        islandCard1.incrementSize();
        Assertions.assertEquals(2, islandCard1.getSize());
    }

    @Test
    void influence1() {
        List<PawnColor> list1 = new ArrayList<>();
        islandCard1.setTower(Tower.BLACK);
        Assertions.assertEquals(1, islandCard1.countInfluence(list1, Tower.BLACK));
        list1.add(PawnColor.YELLOW);
        Assertions.assertEquals(1, islandCard1.countInfluence(list1, Tower.BLACK));
        list1.add(PawnColor.PINK);
        Assertions.assertEquals(1, islandCard1.countInfluence(list1, Tower.BLACK));
    }

    @Test
    void influence2() {
        List<PawnColor> list = listPawnColor();
        islandCard1.movePawnOnIsland(list);
        islandCard1.setTower(Tower.BLACK);
        Assertions.assertEquals(4, islandCard1.countInfluence(list, Tower.BLACK));
        Assertions.assertEquals(3, islandCard1.countInfluence(list, Tower.WHITE));
        Assertions.assertEquals(3, islandCard1.countInfluence(list, Tower.NONE));
        Assertions.assertEquals(islandCard1.countInfluence(list, Tower.GREY), 3);

    }

}
