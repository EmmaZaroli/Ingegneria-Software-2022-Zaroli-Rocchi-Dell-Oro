package it.polimi.ingsw.model;

import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExpertTableTest{
    ExpertTable table2Players = new ExpertTable(PlayersNumber.TWO);
    ExpertTable table3Players = new ExpertTable(PlayersNumber.THREE);

    @Test
    void islandInitialization(){
        List<IslandCard> list = table2Players.getIslands();
        int mnIndex = table2Players.getIslandWithMotherNature();
        int mnOppositeIndex = (mnIndex + 6) % 12;
        for(int i = 0; i < list.size(); i++){
            if(i != mnIndex && i != mnOppositeIndex)
                Assertions.assertEquals(1, list.get(i).getStudentsNumber());
            else{
                Assertions.assertEquals(0, list.get(i).getStudentsNumber());
            }
        }

        list = table3Players.getIslands();
        mnIndex = table3Players.getIslandWithMotherNature();
        mnOppositeIndex = (mnIndex + 6) % 12;
        for(int i = 0; i < list.size(); i++){
            if(i != mnIndex && i != mnOppositeIndex)
                Assertions.assertEquals(1, list.get(i).getStudentsNumber());
            else{
                Assertions.assertEquals(0, list.get(i).getStudentsNumber());
            }
        }
    }

    @Test
    void movingPawnOnIsland(){
        IslandCard island = table2Players.getIslands().get(0);
        int red = island.getStudentsNumber(PawnColor.RED);
        int yellow = island.getStudentsNumber(PawnColor.YELLOW);
        int green = island.getStudentsNumber(PawnColor.GREEN);
        int blue = island.getStudentsNumber(PawnColor.BLUE);
        int pink = island.getStudentsNumber(PawnColor.PINK);
        table2Players.movePawnOnIsland(island, PawnColor.RED);
        table2Players.movePawnOnIsland(island, PawnColor.RED);
        table2Players.movePawnOnIsland(island, PawnColor.RED);
        table2Players.movePawnOnIsland(island, PawnColor.BLUE);
        List<PawnColor> list = new ArrayList<PawnColor>();
        table2Players.movePawnOnIsland(island, list);
        Assertions.assertEquals(red + 3, island.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(blue + 1, island.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink + 0, island.getStudentsNumber(PawnColor.PINK));
        Assertions.assertEquals(green + 0, island.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(yellow + 0, island.getStudentsNumber(PawnColor.YELLOW));
        list.add(PawnColor.BLUE);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.BLUE);
        list.add(PawnColor.RED);
        list.add(PawnColor.GREEN);
        table2Players.movePawnOnIsland(island, list);
        Assertions.assertEquals(red + 4, island.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(blue + 3, island.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink + 0, island.getStudentsNumber(PawnColor.PINK));
        Assertions.assertEquals(green + 1, island.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(yellow + 2, island.getStudentsNumber(PawnColor.YELLOW));

        island = table3Players.getIslands().get(0);
        red = island.getStudentsNumber(PawnColor.RED);
        yellow = island.getStudentsNumber(PawnColor.YELLOW);
        green = island.getStudentsNumber(PawnColor.GREEN);
        blue = island.getStudentsNumber(PawnColor.BLUE);
        pink = island.getStudentsNumber(PawnColor.PINK);
        table3Players.movePawnOnIsland(island, PawnColor.RED);
        table3Players.movePawnOnIsland(island, PawnColor.RED);
        table3Players.movePawnOnIsland(island, PawnColor.RED);
        table3Players.movePawnOnIsland(island, PawnColor.BLUE);
        list = new ArrayList<PawnColor>();
        table3Players.movePawnOnIsland(island, list);
        Assertions.assertEquals(red + 3, island.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(blue + 1, island.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink + 0, island.getStudentsNumber(PawnColor.PINK));
        Assertions.assertEquals(green + 0, island.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(yellow + 0, island.getStudentsNumber(PawnColor.YELLOW));
        list.add(PawnColor.BLUE);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.BLUE);
        list.add(PawnColor.RED);
        list.add(PawnColor.GREEN);
        table3Players.movePawnOnIsland(island, list);
        Assertions.assertEquals(red + 4, island.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(blue + 3, island.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink + 0, island.getStudentsNumber(PawnColor.PINK));
        Assertions.assertEquals(green + 1, island.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(yellow + 2, island.getStudentsNumber(PawnColor.YELLOW));
    }

    @Test
    void gettingIsland(){
        //TODO
    }

    @Test
    void settingTower(){
        IslandCard island = table2Players.getIslands().get(0);
        Assertions.assertEquals(island.getTower(), Tower.NONE);
        island.setTower(Tower.BLACK);
        Assertions.assertEquals(island.getTower(), Tower.BLACK);
        island.setTower(Tower.WHITE);
        island.setTower(Tower.GREY);
        Assertions.assertEquals(island.getTower(), Tower.GREY);

        island = table3Players.getIslands().get(2);
        Assertions.assertEquals(island.getTower(), Tower.NONE);
        island.setTower(Tower.BLACK);
        Assertions.assertEquals(island.getTower(), Tower.BLACK);
        island.setTower(Tower.WHITE);
        island.setTower(Tower.GREY);
        Assertions.assertEquals(island.getTower(), Tower.GREY);
    }

    @Test
    void addingStudents(){
        CloudTile cloud = table2Players.getCloudTiles().get(0);
        int red = cloud.getStudentsNumber(PawnColor.RED);
        int yellow = cloud.getStudentsNumber(PawnColor.YELLOW);
        int green = cloud.getStudentsNumber(PawnColor.GREEN);
        int blue = cloud.getStudentsNumber(PawnColor.BLUE);
        int pink = cloud.getStudentsNumber(PawnColor.PINK);
        List<PawnColor> list = new ArrayList<>();
        table2Players.addStudents(cloud, list);
        Assertions.assertEquals(red, cloud.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(yellow, cloud.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(green, cloud.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(blue, cloud.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink, cloud.getStudentsNumber(PawnColor.PINK));
        list.add(PawnColor.BLUE);
        list.add(PawnColor.BLUE);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.BLUE);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.PINK);
        table2Players.addStudents(cloud, list);
        Assertions.assertEquals(red, cloud.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(yellow + 2, cloud.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(green, cloud.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(blue + 3, cloud.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink + 1, cloud.getStudentsNumber(PawnColor.PINK));

        cloud = table3Players.getCloudTiles().get(0);
        red = cloud.getStudentsNumber(PawnColor.RED);
        yellow = cloud.getStudentsNumber(PawnColor.YELLOW);
        green = cloud.getStudentsNumber(PawnColor.GREEN);
        blue = cloud.getStudentsNumber(PawnColor.BLUE);
        pink = cloud.getStudentsNumber(PawnColor.PINK);
        list = new ArrayList<>();
        table3Players.addStudents(cloud, list);
        Assertions.assertEquals(red, cloud.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(yellow, cloud.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(green, cloud.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(blue, cloud.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink, cloud.getStudentsNumber(PawnColor.PINK));
        list.add(PawnColor.BLUE);
        list.add(PawnColor.BLUE);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.BLUE);
        list.add(PawnColor.YELLOW);
        list.add(PawnColor.PINK);
        table3Players.addStudents(cloud, list);
        Assertions.assertEquals(red, cloud.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(yellow + 2, cloud.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(green, cloud.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(blue + 3, cloud.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(pink + 1, cloud.getStudentsNumber(PawnColor.PINK));
    }

    @Test
    void takingStudentFromCloud(){
        CloudTile cloud = table2Players.getCloudTiles().get(0);
        int red = cloud.getStudentsNumber(PawnColor.RED);
        int yellow = cloud.getStudentsNumber(PawnColor.YELLOW);
        int green = cloud.getStudentsNumber(PawnColor.GREEN);
        int blue = cloud.getStudentsNumber(PawnColor.BLUE);
        int pink = cloud.getStudentsNumber(PawnColor.PINK);
        List<PawnColor> list = table2Players.takeStudentsFromCloud(cloud);
        Assertions.assertEquals(0, cloud.getStudentsNumber());
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.PINK));
        Assertions.assertEquals(red, list.stream().filter(x -> x.equals(PawnColor.RED)).count());
        Assertions.assertEquals(yellow, list.stream().filter(x -> x.equals(PawnColor.YELLOW)).count());
        Assertions.assertEquals(green, list.stream().filter(x -> x.equals(PawnColor.GREEN)).count());
        Assertions.assertEquals(blue, list.stream().filter(x -> x.equals(PawnColor.BLUE)).count());
        Assertions.assertEquals(pink, list.stream().filter(x -> x.equals(PawnColor.PINK)).count());

        cloud = table3Players.getCloudTiles().get(0);
        red = cloud.getStudentsNumber(PawnColor.RED);
        yellow = cloud.getStudentsNumber(PawnColor.YELLOW);
        green = cloud.getStudentsNumber(PawnColor.GREEN);
        blue = cloud.getStudentsNumber(PawnColor.BLUE);
        pink = cloud.getStudentsNumber(PawnColor.PINK);
        list = table3Players.takeStudentsFromCloud(cloud);
        Assertions.assertEquals(0, cloud.getStudentsNumber());
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.RED));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.YELLOW));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.GREEN));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.BLUE));
        Assertions.assertEquals(0, cloud.getStudentsNumber(PawnColor.PINK));
        Assertions.assertEquals(red, list.stream().filter(x -> x.equals(PawnColor.RED)).count());
        Assertions.assertEquals(yellow, list.stream().filter(x -> x.equals(PawnColor.YELLOW)).count());
        Assertions.assertEquals(green, list.stream().filter(x -> x.equals(PawnColor.GREEN)).count());
        Assertions.assertEquals(blue, list.stream().filter(x -> x.equals(PawnColor.BLUE)).count());
        Assertions.assertEquals(pink, list.stream().filter(x -> x.equals(PawnColor.PINK)).count());
    }

    @Test
    void settingIslandWithMotherNature(){
        int index = table2Players.getIslandWithMotherNature();
        List<IslandCard> list = table2Players.getIslands();
        for(int i = 0; i < list.size(); i++){
            if(i != index)
                Assertions.assertFalse(list.get(i).isHasMotherNature());
            else
                Assertions.assertTrue(list.get(i).isHasMotherNature());
        }
        int newIndex = (index + 3) % list.size();
        table2Players.setIslandWithMotherNature(newIndex);
        Assertions.assertEquals(newIndex, table2Players.getIslandWithMotherNature());
        for(int i = 0; i < list.size(); i++){
            if(i != newIndex)
                Assertions.assertFalse(list.get(i).isHasMotherNature());
            else
                Assertions.assertTrue(list.get(i).isHasMotherNature());
        }

        index = table3Players.getIslandWithMotherNature();
        list = table3Players.getIslands();
        for(int i = 0; i < list.size(); i++){
            if(i != index)
                Assertions.assertFalse(list.get(i).isHasMotherNature());
            else
                Assertions.assertTrue(list.get(i).isHasMotherNature());
        }
        newIndex = (index + 3) % list.size();
        table3Players.setIslandWithMotherNature(newIndex);
        Assertions.assertEquals(newIndex, table3Players.getIslandWithMotherNature());
        for(int i = 0; i < list.size(); i++){
            if(i != newIndex)
                Assertions.assertFalse(list.get(i).isHasMotherNature());
            else
                Assertions.assertTrue(list.get(i).isHasMotherNature());
        }
    }

    @Test
    void usingCoins(){
        Assertions.assertEquals(18, table2Players.getCoins());
        table2Players.takeCoin();
        Assertions.assertEquals(17, table2Players.getCoins());
        table2Players.depositCoins(3);
        Assertions.assertEquals(20, table2Players.getCoins());

        Assertions.assertEquals(17, table3Players.getCoins());
        table3Players.takeCoin();
        Assertions.assertEquals(16, table3Players.getCoins());
        table3Players.depositCoins(4);
        Assertions.assertEquals(20, table3Players.getCoins());
    }

}

