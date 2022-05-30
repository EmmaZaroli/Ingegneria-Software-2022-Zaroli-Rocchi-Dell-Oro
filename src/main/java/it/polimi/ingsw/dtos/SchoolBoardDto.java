package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class SchoolBoardDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 100L;
    private final Tower towerColor;
    private final ArrayList<PawnColor> entrance;
    private final DiningRoomDto diningRoom;
    private final HashSet<PawnColor> professorTable;
    private final int towers;
    private final UUID uuid;

    public SchoolBoardDto(SchoolBoard origin) {
        this.diningRoom = new DiningRoomDto(origin);
        this.entrance = new ArrayList<>(origin.getEntrance());
        this.professorTable = new HashSet<>(origin.getProfessors());
        this.towerColor = origin.getTowerColor();
        this.towers = origin.getTowersCount();
        this.uuid = origin.getUuid();
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public ArrayList<PawnColor> getEntrance() {
        return entrance;
    }

    public DiningRoomDto getDiningRoom() {
        return diningRoom;
    }

    public HashSet<PawnColor> getProfessorTable() {
        return professorTable;
    }

    public int getTowers() {
        return towers;
    }

    public boolean isThereProfessor(PawnColor color) {
        return professorTable.contains(color);
    }

    public int getTowersCount() {
        return this.towers;
    }

    public int getStudentsInEntrance(PawnColor color){
        return (int) entrance.stream().filter(x -> x.equals(color)).count();
    }

    public int getStudentsInEntrance(){
        return entrance.size();
    }

    public Map<PawnColor, Integer> getStudentsInEntranceCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsInEntrance(color));
        }
        return res;
    }

    public UUID getUuid(){
        return this.uuid;
    }
}
