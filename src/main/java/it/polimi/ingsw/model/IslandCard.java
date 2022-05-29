package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Island card.
 */
public class IslandCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;
    private final UUID uuid;
    private final ArrayList<PawnColor> students;
    private Tower tower;
    private boolean hasMotherNature;
    private final List<Integer> indices;

    /**
     * @return true if the island has mother nature, false otherwise
     */
    public boolean isHasMotherNature() {
        return hasMotherNature;
    }

    /**
     * @param hasMotherNature
     */
    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    /**
     * Instantiates a new Island card.
     *
     * @param uuid
     */
    public IslandCard(UUID uuid, int index) {
        this.uuid = uuid;
        this.students = new ArrayList<>();
        tower = Tower.NONE;
        indices = new ArrayList<>();
        indices.add(index);
    }

    /**
     * @return the uuid of the islandCard
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Add a Pawn on the islandCard
     *
     * @param student to add
     */
    public void movePawnOnIsland(PawnColor student) {
        students.add(student);
    }

    /**
     * Add a list of Pawn on the islandCard
     *
     * @param outsideStudents the list of students to add
     */
    protected void movePawnOnIsland(List<PawnColor> outsideStudents) {
        students.addAll(outsideStudents);
    }

    /**
     * return the list of Pawn on the islandCard
     *
     * @return the students from islandCard
     */
    public List<PawnColor> getStudentsFromIsland() {
        return (List) students.clone();
    }

    /**
     * Count influence on the islandCard of a player
     *
     * @param professors list of professor that the player has
     * @param towerColor the tower's color of the player
     * @return the total influence of that player in the islandCard
     */

    public int countInfluence(Set<PawnColor> professors, Tower towerColor) {
        int influence = 0;
        for (PawnColor p : students) {
            if (professors.contains(p)) {
                influence++;
            }
        }
        if (towerColor.equals(tower)) {
            influence += getSize();
        }
        return influence;
    }

    /**
     * number of islandCard connected together
     *
     * @return the size
     */
    public int getSize() {
        return indices.size();
    }

    /**
     * return the color of the tower of the player that currently controls the island
     *
     * @return the color of the tower
     */
    public Tower getTower() {
        return tower;
    }

    /**
     * Sets the color of the tower of the player that now controls the island
     *
     * @param tower
     */
    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public int getStudentsNumber(PawnColor color){
        return (int) students.stream().filter(x -> x.equals(color)).count();
    }

    public int getStudentsNumber(){
        return students.size();
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void addIndex(int index){
        this.indices.add(index);
    }

    public void addIndex(List<Integer> indices){
        this.indices.addAll(indices);
    }

    public void unifyWith(IslandCard islandCard){
        this.movePawnOnIsland(islandCard.getStudentsFromIsland());
        this.addIndex(islandCard.getIndices());
    }

    public Map<PawnColor, Integer> getStudentsCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsNumber(color));
        }
        return res;
    }
}
