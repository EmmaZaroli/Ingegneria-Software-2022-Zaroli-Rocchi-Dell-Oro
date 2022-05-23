package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class IslandCardTest extends TestCase {

    IslandCard islandCard1 = new IslandCard(UUID.randomUUID(), 0);

    private List<PawnColor> listPawnColor() {
        List<PawnColor> students = new LinkedList<>();
        students.add(PawnColor.BLUE);
        students.add(PawnColor.RED);
        students.add(PawnColor.GREEN);
        return students;
    }

    private Set<PawnColor> setPawnColor() {
        Set<PawnColor> students = new HashSet<>();
        students.add(PawnColor.BLUE);
        students.add(PawnColor.RED);
        students.add(PawnColor.GREEN);
        return students;
    }

    @Test
    void placePawnOnIsland() {
        List<PawnColor> list = listPawnColor();
        islandCard1.movePawnOnIsland(list);
        Assertions.assertEquals(list, islandCard1.getStudentsFromIsland());
        islandCard1.movePawnOnIsland(PawnColor.BLUE);
        list.add(PawnColor.BLUE);
        Assertions.assertEquals(list, islandCard1.getStudentsFromIsland());
    }

    @Test
    void setTower() {
        islandCard1.setTower(Tower.BLACK);
        Assertions.assertEquals(Tower.BLACK, islandCard1.getTower());
    }

    @Test
    void influenceWithoutProfessors() {
        Set<PawnColor> list1 = new HashSet<>();
        islandCard1.setTower(Tower.BLACK);
        Assertions.assertEquals(1, islandCard1.countInfluence(list1, Tower.BLACK));
        list1.add(PawnColor.YELLOW);
        Assertions.assertEquals(1, islandCard1.countInfluence(list1, Tower.BLACK));
        list1.add(PawnColor.PINK);
        Assertions.assertEquals(1, islandCard1.countInfluence(list1, Tower.BLACK));
    }

    @Test
    void influenceWithProfessors() {
        Set<PawnColor> list = setPawnColor();
        islandCard1.movePawnOnIsland(list.stream().toList());
        islandCard1.setTower(Tower.BLACK);
        Assertions.assertEquals(4, islandCard1.countInfluence(list, Tower.BLACK));
        Assertions.assertEquals(3, islandCard1.countInfluence(list, Tower.WHITE));
        Assertions.assertEquals(3, islandCard1.countInfluence(list, Tower.NONE));
        Assertions.assertEquals(3, islandCard1.countInfluence(list, Tower.GREY));
    }

    @Test
    void unifyWithIsland() {
        islandCard1.movePawnOnIsland(listPawnColor());
        Assertions.assertEquals(3, islandCard1.getStudentsNumber());
        Assertions.assertEquals(1, islandCard1.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(1, islandCard1.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(1, islandCard1.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(0, islandCard1.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(0, islandCard1.getStudentsNumber(PawnColor.PINK));

        IslandCard islandCard2 = new IslandCard(UUID.randomUUID(), 1);
        islandCard2.movePawnOnIsland(listPawnColor());
        islandCard2.movePawnOnIsland(PawnColor.PINK);
        Assertions.assertEquals(4, islandCard2.getStudentsNumber());
        Assertions.assertEquals(1, islandCard2.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(1, islandCard2.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(1, islandCard2.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(0, islandCard2.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(1, islandCard2.getStudentsNumber(PawnColor.PINK));

        islandCard1.unifyWith(islandCard2);
        Assertions.assertEquals(7, islandCard1.getStudentsNumber());
        Assertions.assertEquals(2, islandCard1.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(2, islandCard1.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(2, islandCard1.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(0, islandCard1.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(1, islandCard1.getStudentsNumber(PawnColor.PINK));
        Assertions.assertEquals(2, islandCard1.getSize());
        Assertions.assertTrue(islandCard1.getIndices().contains(0));
        Assertions.assertTrue(islandCard1.getIndices().contains(1));

    }
}
