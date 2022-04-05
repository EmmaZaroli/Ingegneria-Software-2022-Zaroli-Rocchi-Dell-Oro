package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.PlayerCountIcon;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.RandomHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TableController implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private final ArrayList<PawnColor> professors;
    private final ArrayList<IslandCard> islandCards;
    private final Bag bag;
    private final ArrayList<CloudTile> cloudTiles;
    private final int playerNumber;
    private int islandWithMotherNature;

    public TableController(PlayerCountIcon playerCountIcon) {
        islandCards = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            this.islandCards.add(new IslandCard());
        }
        RandomHelper random = RandomHelper.getInstance();
        int initialPosition = random.getInt(12);
        islandWithMotherNature = initialPosition;
        List<PawnColor> initialized = new ArrayList<>(10);
        initialized.addAll((Arrays.stream(PawnColor.values()).toList()));
        initialized.addAll((Arrays.stream(PawnColor.values()).toList()));
        for (int i = 0; i < 12; i++) {
            if (i - initialPosition != 6 && initialPosition - i != 6) {
                int pawnColor = random.getInt(initialized.size());
                islandCards.get((i + initialPosition) % 12).movePawnOnIsland(initialized.get(pawnColor));
                initialized.remove(pawnColor);
            }
        }
        this.bag = new Bag();
        cloudTiles = new ArrayList<>();
        if (playerCountIcon.equals(PlayerCountIcon.THREE)) playerNumber = 3;
        else playerNumber = 2;
        this.cloudTiles.add(new CloudTile(playerCountIcon));
        this.cloudTiles.add(new CloudTile(playerCountIcon));
        if (playerCountIcon.equals(PlayerCountIcon.THREE)) {
            this.cloudTiles.add(new CloudTile(playerCountIcon));
        }
        professors = new ArrayList<>();
        professors.addAll(Arrays.asList(PawnColor.values()));
    }

    //method to call the first time to fill the Entrance in SchoolBoard
    public List<PawnColor> drawStudents() {
        List<PawnColor> studentsDrawn = new ArrayList<>();
        if (playerNumber == 2) {
            for(int i = 0; i < 7; i++) {
                studentsDrawn.add(bag.drawStudent());
            }
        } else {
            for(int i = 0; i < 7; i++) {
                studentsDrawn.add(bag.drawStudent());
            }
        }
        return studentsDrawn;
    }

    public List<PawnColor> drawStudents(int n){
        List<PawnColor> students = new LinkedList<>();
        for(int i = 0; i < n; i++) {
            students.add(bag.drawStudent());
        }
        return students;
    }

    public void fillClouds() {
        List<PawnColor> studentsDrawn = new LinkedList<>();
        for (CloudTile cloud : cloudTiles) {
            if (playerNumber == 2) {
                for(int i = 0; i < 3; i++) {
                    studentsDrawn.add(bag.drawStudent());
                }
            } else {
                for(int i = 0; i < 4; i++) {
                    studentsDrawn.add(bag.drawStudent());
                }
            }
            cloud.addStudents(studentsDrawn);
            studentsDrawn.clear();
        }
    }

    //TODO check if islandIndex exists
    public void movePawnOnIsland(PawnColor student, int islandIndex) {
        islandCards.get(islandIndex).movePawnOnIsland(student);
    }

    //take professor
    public boolean takeProfessor(PawnColor professor) {
        if (professors.contains(professor)) {
            professors.remove(professor);
            return true;
        }
        return false;
    }

    public void moveMotherNature(int move) {
        islandWithMotherNature = (islandWithMotherNature + move) % islandCards.size();
    }

    public int countInfluenceOnIsland(List<PawnColor> playerProfessors, Tower towerColor) {
        return islandCards.get(islandWithMotherNature).countInfluence(playerProfessors, towerColor);
    }

    public boolean canBuildTower(Tower towerColor) {
        Tower towerOnIsland = islandCards.get(islandWithMotherNature).getTower();
        return (towerOnIsland.equals(Tower.NONE) || !towerOnIsland.equals(towerColor));
    }

    public Pair buildTower(Tower towerColor) {
        Tower towerOnIsland = islandCards.get(islandWithMotherNature).getTower();
        Pair pair = new Pair(towerOnIsland, islandCards.get(islandWithMotherNature).getSize());
        islandCards.get(islandWithMotherNature).setTower(towerColor);
        tryUnifyIslands(towerColor);
        return pair;
    }

    private void tryUnifyIslands(Tower towerColor) {
        //right
        IslandCard islandLeft = islandCards.get(Math.floorMod(islandWithMotherNature - 1, islandCards.size()));
        if (islandLeft.getTower().equals(towerColor)) {
            islandCards.get(islandWithMotherNature).movePawnOnIsland(islandLeft.getStudentsFromIsland());
            islandCards.remove(Math.floorMod(islandWithMotherNature - 1, islandCards.size()));
            islandCards.get(islandWithMotherNature).incrementSize();
            islandWithMotherNature -= 1;
        }
        //left
        IslandCard islandRight = islandCards.get(Math.floorMod(islandWithMotherNature + 1, islandCards.size()));
        if (islandLeft.getTower().equals(towerColor)) {
            islandCards.get(islandWithMotherNature).movePawnOnIsland(islandRight.getStudentsFromIsland());
            islandCards.remove(Math.floorMod(islandWithMotherNature + 1, islandCards.size()));
            islandCards.get(islandWithMotherNature).incrementSize();
        }
    }

    public List<PawnColor> takeStudentsFromCloud(CloudTile cloud) {
        return cloud.takeStudentsFromCloud();
    }

    public List<PawnColor> takeStudentsFromCloud(int cloudIndex) {
        return cloudTiles.get(cloudIndex % 2).takeStudentsFromCloud();
    }

    protected Bag getBag() {
        return this.bag;
    }

    public int howManyIsland() {
        return islandCards.size();
    }
}
