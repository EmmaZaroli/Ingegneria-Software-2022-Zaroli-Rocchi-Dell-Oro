package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.gamecontroller.exceptions.FullCloudException;
import it.polimi.ingsw.gamecontroller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

/**
 * A class wrapping the logic for the management of the game table
 */
public class TableController {
    protected Table table;
    private GameParameters parameters;

    /**
     * Creates a new controller for the given table
     *
     * @param table The table to associate to the controller
     */
    public TableController(Table table, GameParameters parameters) {
        this.table = table;
        this.parameters = parameters;
    }

    /**
     * Fills the entrances at the beginning of the game
     */
    public List<PawnColor> drawStudents() {
        List<PawnColor> studentsDrawn = new ArrayList<>();
        if (table.getPlayersNumber() == PlayersNumber.TWO) {
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

    /**
     * Draws n students from the bag
     *
     * @param n The number fo students to draw
     * @return The drawn students
     */
    public List<PawnColor> drawStudents(int n) {
        List<PawnColor> students = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            students.add(table.getBag().drawStudent());
        }
        return students;
    }

    /**
     * Adds the students to the cloud
     *
     * @throws FullCloudException The cloud is not empty
     */
    public void fillClouds() throws FullCloudException {
        List<PawnColor> studentsDrawn = new LinkedList<>();
        /*
        for (CloudTile cloudTile : table.getCloudTiles()) {
            if (!table.takeStudentsFromCloud(cloudTile).isEmpty()) throw new FullCloudException();
        }*/
        for (CloudTile cloud : table.getCloudTiles()) {
            if(cloud.getStudentsNumber() == 0){
                if (table.getPlayersNumber() == PlayersNumber.TWO) {
                    for (int i = 0; i < 3; i++) {
                        studentsDrawn.add(table.getBag().drawStudent());
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        studentsDrawn.add(table.getBag().drawStudent());
                    }
                }
                table.addStudents(cloud, studentsDrawn);
                studentsDrawn.clear();
            }
        }
    }

    /**
     * Moves a pawn on an island
     *
     * @param student The student to place
     * @param uuid    The id of the target island
     * @throws WrongUUIDException When the given id doesn't correspond to an island
     */
    public void movePawnOnIsland(PawnColor student, UUID uuid) throws WrongUUIDException {
        table.movePawnOnIsland(table.getIsland(uuid), student);
    }

    /**
     * Takes a professor from the table
     *
     * @param professor The color of the professor to take
     * @return True if the professor has been taken
     */
    public boolean takeProfessor(PawnColor professor) {
        if (table.getProfessors().contains(professor)) {
            table.getProfessors().remove(professor);
            return true;
        }
        return false;
    }

    /**
     * Moves Mother Nature
     *
     * @param move The number of steps to make
     */
    public void moveMotherNature(int move) {
        table.setIslandWithMotherNature(Math.floorMod(table.getIslandWithMotherNature() + move, table.getIslands().size()));
    }

    /**
     * Calculates the influence on a given island
     *
     * @param playerProfessors The professors on the player's schoolboard
     * @param towerColor       The color of the towers of the current player
     * @return The calculated influence
     */
    public int countInfluenceOnIsland(Set<PawnColor> playerProfessors, Tower towerColor) {
        return table.getIslands().get(table.getIslandWithMotherNature()).countInfluence(playerProfessors, towerColor);
    }

    /**
     * Checks if the current player can build a tower
     *
     * @param towerColor The color of the towers of the player
     * @return True if the player can build a tower
     */
    public boolean canBuildTower(Tower towerColor) {
        Tower towerOnIsland = table.getIslands().get(table.getIslandWithMotherNature()).getTower();
        return (towerOnIsland.equals(Tower.NONE) || !towerOnIsland.equals(towerColor));
    }

    /**
     * Builds a tower on the island with Mother Nature
     *
     * @param towerColor The color of the tower to build
     * @return A tuple containing the color of the tower previously on the island and the number of them
     */
    public Pair<Tower, Integer> buildTower(Tower towerColor) {
        Tower towerOnIsland = table.getIslands().get(table.getIslandWithMotherNature()).getTower();
        Pair<Tower, Integer> pair = new Pair<>(towerOnIsland, table.getIslands().get(table.getIslandWithMotherNature()).getSize());
        table.setTower(table.getIslands().get(table.getIslandWithMotherNature()), towerColor);
        tryUnifyIslands(towerColor);
        return pair;
    }

    /**
     * Tries to unify adjacent island
     *
     * @param towerColor The color of the tower of the current player
     */
    private void tryUnifyIslands(Tower towerColor) {
        //right
        IslandCard islandLeft = table.getIsland(Math.floorMod(table.getIslandWithMotherNature() - 1, table.getIslands().size()));
        if (islandLeft.getTower().equals(towerColor)) {
            table.unifyIslands(table.getIslandWithMotherNature(), Math.floorMod(table.getIslandWithMotherNature() - 1, table.getIslands().size()));
            //islandLeft.setHasMotherNature(false);
            //if (table.getIslandWithMotherNature() != 0)
            //    table.setIslandWithMotherNature(Math.floorMod(table.getIslandWithMotherNature() - 1, table.getIslands().size()));
        }
        //left
        IslandCard islandRight = table.getIsland(Math.floorMod(table.getIslandWithMotherNature() + 1, table.getIslands().size()));
        if (islandRight.getTower().equals(towerColor)) {
            table.unifyIslands(table.getIslandWithMotherNature(), Math.floorMod(table.getIslandWithMotherNature() + 1, table.getIslands().size()));
            //islandRight.setHasMotherNature(false);
            //if (table.getIslandWithMotherNature() == table.getIslands().size())
            //    table.setIslandWithMotherNature(table.getIslandWithMotherNature() - 1);
        }

    }

    /**
     * Takes the students from a cloud
     *
     * @param uuid The id of the cloud
     * @return The students on the selected cloud
     * @throws WrongUUIDException When the cloud does not exist
     */
    public List<PawnColor> takeStudentsFromCloud(UUID uuid) throws WrongUUIDException {
        for (CloudTile cloud : table.getCloudTiles()) {
            if (cloud.getUuid().equals(uuid)) {
                return table.takeStudentsFromCloud(cloud);
            }
        }
        throw new WrongUUIDException();
    }

    /**
     * Returns the number of islands on the table
     *
     * @return The number of islands
     */
    public int countIslands() {
        return this.table.countIsland();
    }

    /**
     * Returns the bag associated to the current game
     *
     * @return The bag
     */
    public Bag getBag() {
        return this.table.getBag();
    }

    public Table getTable() {
        return table;
    }

    public GameParameters getParameters() {
        return parameters;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
