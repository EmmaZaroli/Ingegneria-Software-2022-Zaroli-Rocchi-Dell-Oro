package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class IslandCardTest extends TestCase {

    IslandCard islandCard1 = new IslandCard(UUID.randomUUID());

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
    void size() {
        islandCard1.incrementSize();
        Assertions.assertEquals(2, islandCard1.getSize());
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
}
