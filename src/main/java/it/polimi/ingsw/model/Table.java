package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.utils.RandomHelper;

import java.util.*;

public class Table {
    private final ArrayList<PawnColor> professors;
    private final List<IslandCard> islandCards;
    private final Bag bag;
    private final List<CloudTile> cloudTiles;
    private int islandWithMotherNature;
    private final int playerNumber;

    public Table(PlayerCountIcon playerCountIcon) {
        islandCards = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            this.islandCards.add(new IslandCard());
        }
        RandomHelper random = RandomHelper.getInstance();
        int initialPosition = random.getInt(12);
        islandCards.get(initialPosition).setMotherNatureHere(true);
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
            studentsDrawn.addAll(bag.drawStudents(7));
        } else {
            studentsDrawn.addAll(bag.drawStudents(9));
        }
        return studentsDrawn;
    }

    public void fillClouds() {
        List<PawnColor> studentsDrawn = new ArrayList<>();
        for (CloudTile cloud : cloudTiles) {
            if (playerNumber == 2) {
                studentsDrawn = bag.drawStudents(3);
            } else {
                studentsDrawn = bag.drawStudents(4);
            }
            cloud.AddStudents(studentsDrawn);
            studentsDrawn.clear();
        }
    }

    public void movePawnOnIsland(PawnColor student, IslandCard Island) {
        Island.movePawnOnIsland(student);
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
        islandCards.get(islandWithMotherNature).setMotherNatureHere(false);
        islandWithMotherNature = (islandWithMotherNature + move) % islandCards.size();
        islandCards.get(islandWithMotherNature).setMotherNatureHere(true);
    }

    public int countInfluenceOnIsland(List<PawnColor> Playerprofessors, Tower towerColor) {
        int influence = islandCards.get(islandWithMotherNature).countInfluence(Playerprofessors, towerColor);
        return influence;
    }

    public boolean canBuildTower(Tower towerColor) {
        Tower towerOnIsland = islandCards.get(islandWithMotherNature).getTower();
        if (towerOnIsland.equals(Tower.NONE) || !towerOnIsland.equals(towerColor)) {
            return true;
        }
        return false;
    }

    public Pair BuildTower(Tower towerColor) {
        Tower towerOnIsland = islandCards.get(islandWithMotherNature).getTower();
        Pair pair = new Pair(towerOnIsland, islandCards.get(islandWithMotherNature).getSize());
        islandCards.get(islandWithMotherNature).setTower(towerColor);
        tryUnifyIslands(towerColor);
        return pair;
    }

    private void tryUnifyIslands(Tower towerColor) {
        //left
        IslandCard islandLeft = islandCards.get(Math.floorMod(islandWithMotherNature - 1, islandCards.size()));
        if (islandLeft.getTower().equals(towerColor)) {
            islandCards.get(islandWithMotherNature).movePawnsOnIsland(islandLeft.getStudentsFromIsland());
            islandCards.remove(Math.floorMod(islandWithMotherNature - 1, islandCards.size()));
            islandCards.get(islandWithMotherNature).setSize();
        }
        //right
        IslandCard islandRight = islandCards.get(Math.floorMod(islandWithMotherNature + 1, islandCards.size()));
        if (islandLeft.getTower().equals(towerColor)) {
            islandCards.get(islandWithMotherNature).movePawnsOnIsland(islandRight.getStudentsFromIsland());
            islandCards.remove(Math.floorMod(islandWithMotherNature + 1, islandCards.size()));
            islandCards.get(islandWithMotherNature).setSize();
        }
    }

    public List<PawnColor> takeStudentsFromCloud(CloudTile cloud) {
        List<PawnColor> students = cloud.takeStudentsFromCloud();
        return students;
    }

    protected Bag getBag() {
        return this.bag;
    }
}
