package it.polimi.ingsw;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import org.junit.Before;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.*;

public class IslandCardTest {

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
        assertEquals(list, islandCard1.getStudentsFromIsland());
        islandCard1.movePawnOnIsland(PawnColor.BLUE);
        list.add(PawnColor.BLUE);
        assertEquals(list, islandCard1.getStudentsFromIsland());
    }

    @Test
    void Tower() {
        islandCard1.setTower(Tower.BLACK);
        assertEquals(islandCard1.getTower(), Tower.BLACK);
    }

    @Test
    void size() {
        islandCard1.incrementSize();
        assertEquals(islandCard1.getSize(), 2);
    }

    @Test
    void influence1() {
        List<PawnColor> list1 = new ArrayList<>();
        islandCard1.setTower(Tower.BLACK);
        assertEquals(islandCard1.countInfluence(list1, Tower.BLACK), 1);
        list1.add(PawnColor.YELLOW);
        assertEquals(islandCard1.countInfluence(list1, Tower.BLACK), 1);
        list1.add(PawnColor.PINK);
        assertEquals(islandCard1.countInfluence(list1, Tower.BLACK), 1);
    }

    @Test
    void influence2() {
        List<PawnColor> list = listPawnColor();
        islandCard1.movePawnOnIsland(list);
        islandCard1.setTower(Tower.BLACK);
        assertEquals(islandCard1.countInfluence(list, Tower.BLACK), 4);
        assertEquals(islandCard1.countInfluence(list, Tower.WHITE), 3);
        assertEquals(islandCard1.countInfluence(list, Tower.NONE), 3);
        assertEquals(islandCard1.countInfluence(list, Tower.GREY), 3);

    }

}
