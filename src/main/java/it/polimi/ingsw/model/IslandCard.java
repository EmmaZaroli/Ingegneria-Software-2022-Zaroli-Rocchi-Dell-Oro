package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.observer.Observable;

/**
 * Island card.
 */
public class IslandCard extends Observable<IslandCard> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;
    //TODO add an identifier
    private final UUID uuid;
    private final ArrayList<PawnColor> students;
    private Tower tower;
    private int size;

    /**
     * Instantiates a new Island card.
     *
     * @param uuid
     */
    public IslandCard(UUID uuid) {
        this.uuid = uuid;
        this.students = new ArrayList<>();
        tower = Tower.NONE;
        size = 1;
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
        notify(this);
    }

    /**
     * Add a list of Pawn on the islandCard
     *
     * @param outsideStudents the list of students to add
     */
    public void movePawnOnIsland(List<PawnColor> outsideStudents) {
        students.addAll(outsideStudents);
        notify(this);
    }

    /**
     * return the list of Pawn on the islandCard
     *
     * @return the students from islandCard
     */
    public List<PawnColor> getStudentsFromIsland() {
        return students;
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
            influence += size;
        }
        return influence;
    }

    /**
     * number of islandCard connected together
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Increment size when an islandCard is been connected to another one
     */
    public void incrementSize() {
        this.size++;
        notify(this);
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
        notify(this);
    }
}
