package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * SchoolBoard Dto
 */
public class SchoolBoardDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 100L;
    private final Tower towerColor;
    private final ArrayList<PawnColor> entrance;
    private final DiningRoomDto diningRoom;
    private final HashSet<PawnColor> professorTable;
    private final int towers;
    private final UUID uuid;

    /**
     * Construct the schoolBoardDto from a schoolBoard
     * @param origin the schoolBoard
     */
    public SchoolBoardDto(SchoolBoard origin) {
        this.diningRoom = new DiningRoomDto(origin);
        this.entrance = new ArrayList<>(origin.getEntrance());
        this.professorTable = new HashSet<>(origin.getProfessors());
        this.towerColor = origin.getTowerColor();
        this.towers = origin.getTowersCount();
        this.uuid = origin.getUuid();
    }

    /**
     *
     * @return the Tower
     */
    public Tower getTowerColor() {
        return towerColor;
    }

    /**
     *
     * @return the list of pawnColor in the entrance
     */
    public List<PawnColor> getEntrance() {
        return entrance;
    }

    /**
     *
     * @return the diningRoom
     */
    public DiningRoomDto getDiningRoom() {
        return diningRoom;
    }

    /**
     *
     * @return the set of professors in the schoolBoard
     */
    public Set<PawnColor> getProfessorTable() {
        return professorTable;
    }

    /**
     *
     * @return the number of towers still on the schoolBoard
     */
    public int getTowers() {
        return towers;
    }

    /**
     *
     * @param color the pawnColor
     * @return true if on the schoolBoard there's a professor with the same color as the @param color
     */
    public boolean isThereProfessor(PawnColor color) {
        return professorTable.contains(color);
    }

    /**
     *
     * @return
     */
    public int getTowersCount() {
        return this.towers;
    }

    /**
     *
     * @param color the pawnColor
     * @return the number of students of that color in the entrance
     */
    public int getStudentsInEntrance(PawnColor color){
        return (int) entrance.stream().filter(x -> x.equals(color)).count();
    }

    /**
     *
     * @return maps each pawnColor with the number of occurrences in the entrance
     */
    public Map<PawnColor, Integer> getStudentsInEntranceCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsInEntrance(color));
        }
        return res;
    }

    /**
     *
     * @return the uuid
     */
    public UUID getUuid(){
        return this.uuid;
    }
}
