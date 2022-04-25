package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.FullCloudException;
import it.polimi.ingsw.controller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.Table;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.utils.Pair;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import java.util.*;


class TableControllerTest extends TestCase {

    Table table2Player = new Table(2);
    Table table3Player = new Table(3);
    TableController tableController2Player = new TableController(table2Player);
    TableController tableController3Player = new TableController(table3Player);

    @Test
    void init() {

        //checking cloud and island size depending on the number of player
        Assertions.assertEquals(2, table2Player.getCloudTiles().size());
        Assertions.assertEquals(3, table3Player.getCloudTiles().size());
        Assertions.assertEquals(12, table2Player.getIslands().size());

        //checking the correctness of the position of mother nature and the students on the islands
        int only_one_empty_island = 0;
        int emptyIslandIndex = 0;
        Assertions.assertTrue(table2Player.getIslands().get(table2Player.getIslandWithMotherNature()).getStudentsFromIsland().isEmpty());
        System.out.println(table2Player.getIslandWithMotherNature());
        for (int i = 0; i < table2Player.getIslands().size(); i++) {
            String content;
            if (table2Player.getIslandWithMotherNature() == i) {
                content = "has mother nature";
            } else
                content = !table2Player.getIslands().get(i).getStudentsFromIsland().isEmpty() ? "has student" : "is empty";
            if (content.equals("is empty")) {
                only_one_empty_island++;
                emptyIslandIndex = i;
            }
            System.out.println("island: " + i + "  " + content);
        }
        Assertions.assertEquals(1, only_one_empty_island);
        Assertions.assertEquals(6, Math.abs(table2Player.getIslandWithMotherNature() - emptyIslandIndex));

        //checking if all the paws have been correctly assigned
        EnumMap<PawnColor, Integer> ColorCount = new EnumMap<>(PawnColor.class);
        for (PawnColor p : PawnColor.getValidValues())
            ColorCount.put(p, 0);
        for (int i = 0; i < table2Player.getIslands().size(); i++) {
            if (i != emptyIslandIndex && i != table2Player.getIslandWithMotherNature()) {
                List<PawnColor> keys = new ArrayList<>(table2Player.getIslands().get(i).getStudentsFromIsland());
                Assertions.assertEquals(1, keys.size());
                ColorCount.merge(keys.get(0), 1, Integer::sum);
            }
        }
        for (PawnColor p : PawnColor.getValidValues()) {
            Assertions.assertEquals(2, (int) ColorCount.get(p));
        }
    }

    @Test
    void drawStudents() {
        Assertions.assertEquals(7, tableController2Player.drawStudents().size());
        Assertions.assertEquals(9, tableController3Player.drawStudents().size());
        Assertions.assertEquals(3, tableController2Player.drawStudents(3).size());
    }

    @Test
    void fillClouds() throws FullCloudException {
        boolean thrown = false;
        try {
            tableController2Player.fillClouds();
        } catch (FullCloudException e) {
            thrown = true;
        }
        Assertions.assertFalse(thrown);
        try {
            tableController2Player.fillClouds();
        } catch (FullCloudException e) {
            thrown = true;
        }
        Assertions.assertTrue(thrown);
        tableController3Player.fillClouds();
    }

    @Test
    void takeProfessors() {
        for (PawnColor p : PawnColor.getValidValues()) {
            Assertions.assertTrue(tableController2Player.takeProfessor(p));
            Assertions.assertFalse(tableController2Player.takeProfessor(p));
        }
    }

    @Test
    void moveMotherNature() {
        int move = 2;
        int motherNature = table2Player.getIslandWithMotherNature();
        tableController2Player.moveMotherNature(move);
        Assertions.assertEquals(table2Player.getIslandWithMotherNature(), ((move + motherNature) % 12));
    }

    @Test
    void movePawnOnIsland() {
        boolean thrown = false;
        UUID uuid = table3Player.getIslands().get(0).getUuid();
        try {
            tableController3Player.movePawnOnIsland(PawnColor.RED, uuid);
        } catch (WrongUUIDException e) {
            thrown = true;
        }
        Assertions.assertFalse(thrown);
        uuid = java.util.UUID.randomUUID();
        try {
            tableController3Player.movePawnOnIsland(PawnColor.RED, uuid);
        } catch (WrongUUIDException e) {
            thrown = true;
        }
        Assertions.assertTrue(thrown);
    }


    @Test
    void takeStudentsFromCloud() {
        boolean thrown = false;
        UUID uuid = table3Player.getCloudTiles().get(0).getUuid();
        try {
            tableController3Player.takeStudentsFromCloud(uuid);
        } catch (WrongUUIDException e) {
            thrown = true;
        }
        Assertions.assertFalse(thrown);
        uuid = java.util.UUID.randomUUID();
        try {
            tableController3Player.takeStudentsFromCloud(uuid);
        } catch (WrongUUIDException e) {
            thrown = true;
        }
        Assertions.assertTrue(thrown);
    }


    // tests that cover the unification of the islands
    @Nested
    class ControlIslandTestNest {

        public int motherNature = table3Player.getIslandWithMotherNature();

        @BeforeEach
        void positioning() {

            table3Player.getIslands().get(motherNature).movePawnOnIsland(PawnColor.BLUE);
            table3Player.getIslands().get(motherNature).movePawnOnIsland(PawnColor.RED);
        }

        @Test
        void countInfluence() {
            table3Player.getIslands().get(motherNature).setTower(Tower.BLACK);
            Set<PawnColor> player_1 = new HashSet<>();
            player_1.addAll(Arrays.asList(PawnColor.RED, PawnColor.BLUE));
            Set<PawnColor> player_2 = new HashSet<>();
            player_1.addAll(Arrays.asList(PawnColor.GREEN, PawnColor.YELLOW));
            Assertions.assertEquals(3, tableController3Player.countInfluenceOnIsland(player_1, Tower.BLACK));
            Assertions.assertEquals(0, tableController3Player.countInfluenceOnIsland(player_2, Tower.WHITE));
        }

        @Test
        void RandomLeftUnification() {
            System.out.println("- left unification: ");
            table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).setTower(Tower.BLACK);
            List<PawnColor> island1 = new ArrayList<>(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            tableController3Player.moveMotherNature(1);
            island1.addAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            Assertions.assertTrue(tableController3Player.canBuildTower(Tower.BLACK));

            System.out.println(table3Player.getIslandWithMotherNature());
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            Pair pair = tableController3Player.buildTower(Tower.BLACK);

            System.out.println("After Unification: ");
            Assertions.assertEquals(Tower.NONE, pair.first());
            Assertions.assertEquals(1, pair.second());
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            Assertions.assertTrue(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland().containsAll(island1));
            Assertions.assertTrue(island1.containsAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland()));
            Assertions.assertEquals(11, table3Player.getIslands().size());
        }

        @Test
        void RandomRightUnification() {
            System.out.println("- right unification: ");
            table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).setTower(Tower.BLACK);
            List<PawnColor> island1 = new ArrayList<>(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            tableController3Player.moveMotherNature(11);
            island1.addAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            Assertions.assertTrue(tableController3Player.canBuildTower(Tower.BLACK));

            System.out.println(table3Player.getIslandWithMotherNature());
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            Pair pair = tableController3Player.buildTower(Tower.BLACK);

            System.out.println("After Unification: ");
            Assertions.assertEquals(Tower.NONE, pair.first());
            Assertions.assertEquals(1, pair.second());
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            Assertions.assertTrue(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland().containsAll(island1));
            Assertions.assertTrue(island1.containsAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland()));
            Assertions.assertEquals(11, table3Player.getIslands().size());
        }

        @Test
        void forceBorderlineUnification() {
            //force borderline unification 0-11
            System.out.println("- border line unification: ");
            tableController3Player.moveMotherNature(11 - table3Player.getIslandWithMotherNature());
            table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).setTower(Tower.BLACK);
            List<PawnColor> island1 = new ArrayList<>(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            tableController3Player.moveMotherNature(1);
            island1.addAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            Assertions.assertTrue(tableController3Player.canBuildTower(Tower.BLACK));

            System.out.println(table3Player.getIslandWithMotherNature());
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            Pair pair = tableController3Player.buildTower(Tower.BLACK);

            System.out.println("After Unification: ");
            Assertions.assertEquals(Tower.NONE, pair.first());
            Assertions.assertEquals(1, pair.second());
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            Assertions.assertTrue(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland().containsAll(island1));
            Assertions.assertTrue(island1.containsAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland()));
            Assertions.assertEquals(11, table3Player.getIslands().size());
        }

        @Test
        void EachSideUnification() {
            //forced on critical 10-11-0
            System.out.println("- each side unification: ");

            tableController3Player.moveMotherNature(10 - table3Player.getIslandWithMotherNature());
            table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).setTower(Tower.BLACK);
            List<PawnColor> island1 = new ArrayList<>(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            tableController3Player.moveMotherNature(2);
            table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).setTower(Tower.BLACK);
            island1.addAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            tableController3Player.moveMotherNature(11);
            island1.addAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland());
            Assertions.assertTrue(tableController3Player.canBuildTower(Tower.BLACK));

            System.out.println(table3Player.getIslandWithMotherNature());
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            tableController3Player.buildTower(Tower.BLACK);
            System.out.println("After Unification: ");
            for (int i = 0; i < table3Player.getIslands().size(); i++) {
                IslandCard island = table3Player.getIslands().get(i);
                System.out.println("isola: " + i + "   torre: " + island.getTower() + "  with students: " + island.getStudentsFromIsland());
            }
            Assertions.assertTrue(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland().containsAll(island1));
            Assertions.assertTrue(island1.containsAll(table3Player.getIslands().get(table3Player.getIslandWithMotherNature()).getStudentsFromIsland()));
            Assertions.assertEquals(10, table3Player.getIslands().size());
        }

    }
}