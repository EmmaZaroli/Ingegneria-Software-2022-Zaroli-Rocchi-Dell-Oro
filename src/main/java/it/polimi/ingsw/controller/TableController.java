package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.FullCloudException;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.Table;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

public class TableController {
    protected Table table;

    public TableController(Table table) {
        this.table = table;
    }

    //method to call the first time to fill the Entrance in SchoolBoard
    public List<PawnColor> drawStudents() {
        List<PawnColor> studentsDrawn = new ArrayList<>();
        if (table.getPlayersNumber() == 2) {
            for (int i = 0; i < 7; i++) {
                studentsDrawn.add(table.getBag().drawStudent());
            }
        } else {
            for (int i = 0; i < 9; i++) {
                studentsDrawn.add(table.getBag().drawStudent());
            }
        }
        return studentsDrawn;
    }

    public List<PawnColor> drawStudents(int n) {
        List<PawnColor> students = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            students.add(table.getBag().drawStudent());
        }
        return students;
    }

    public void fillClouds() throws FullCloudException {
        List<PawnColor> studentsDrawn = new LinkedList<>();
        for (CloudTile cloudTile : table.getCloudTiles()) {
            if (!cloudTile.takeStudentsFromCloud().isEmpty()) throw new FullCloudException();
        }
        for (CloudTile cloud : table.getCloudTiles()) {
            if (table.getPlayersNumber() == 2) {
                for (int i = 0; i < 3; i++) {
                    studentsDrawn.add(table.getBag().drawStudent());
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    studentsDrawn.add(table.getBag().drawStudent());
                }
            }
            cloud.addStudents(studentsDrawn);
            studentsDrawn.clear();
        }
    }


    public void movePawnOnIsland(PawnColor student, UUID uuid) throws WrongUUIDException {
        table.getIsland(uuid).movePawnOnIsland(student);
    }

    //take professor
    public boolean takeProfessor(PawnColor professor) {
        if (table.getProfessors().contains(professor)) {
            table.getProfessors().remove(professor);
            return true;
        }
        return false;
    }

    public void moveMotherNature(int move) {
        table.setIslandWithMotherNature((table.getIslandWithMotherNature() + move) % table.getIslands().size());
    }

    public int countInfluenceOnIsland(Set<PawnColor> playerProfessors, Tower towerColor) {
        return table.getIslands().get(table.getIslandWithMotherNature()).countInfluence(playerProfessors, towerColor);
    }

    public boolean canBuildTower(Tower towerColor) {
        Tower towerOnIsland = table.getIslands().get(table.getIslandWithMotherNature()).getTower();
        return (towerOnIsland.equals(Tower.NONE) || !towerOnIsland.equals(towerColor));
    }

    public Pair<Tower, Integer> buildTower(Tower towerColor) {
        Tower towerOnIsland = table.getIslands().get(table.getIslandWithMotherNature()).getTower();
        Pair<Tower, Integer> pair = new Pair<>(towerOnIsland, table.getIslands().get(table.getIslandWithMotherNature()).getSize());
        table.getIslands().get(table.getIslandWithMotherNature()).setTower(towerColor);
        tryUnifyIslands(towerColor);
        return pair;
    }

    private void tryUnifyIslands(Tower towerColor) {
        //right
        IslandCard islandLeft = table.getIslands().get(Math.floorMod(table.getIslandWithMotherNature() - 1, table.getIslands().size()));
        if (islandLeft.getTower().equals(towerColor)) {
            table.getIslands().get(table.getIslandWithMotherNature()).movePawnOnIsland(islandLeft.getStudentsFromIsland());
            table.getIslands().remove(Math.floorMod(table.getIslandWithMotherNature() - 1, table.getIslands().size()));
            if (table.getIslandWithMotherNature() != 0)
                table.setIslandWithMotherNature(Math.floorMod(table.getIslandWithMotherNature() - 1, table.getIslands().size()));
            table.getIslands().get(table.getIslandWithMotherNature()).incrementSize();
        }
        //left
        IslandCard islandRight = table.getIslands().get(Math.floorMod(table.getIslandWithMotherNature() + 1, table.getIslands().size()));
        if (islandRight.getTower().equals(towerColor)) {
            table.getIslands().get(table.getIslandWithMotherNature()).movePawnOnIsland(islandRight.getStudentsFromIsland());
            table.getIslands().remove(Math.floorMod(table.getIslandWithMotherNature() + 1, table.getIslands().size()));
            if (table.getIslandWithMotherNature() == table.getIslands().size()) {
                table.setIslandWithMotherNature(table.getIslandWithMotherNature() - 1);
            }
            table.getIslands().get(table.getIslandWithMotherNature()).incrementSize();
        }

    }

    public List<PawnColor> takeStudentsFromCloud(UUID uuid) throws WrongUUIDException {
        for (CloudTile cloud : table.getCloudTiles()) {
            if (cloud.getUuid().equals(uuid)) {
                return cloud.takeStudentsFromCloud();
            }
        }
        throw new WrongUUIDException();
    }

    public int countIslands() {
        return this.table.countIsland();
    }

    public Bag getBag() {
        return this.table.getBag();
    }
}
