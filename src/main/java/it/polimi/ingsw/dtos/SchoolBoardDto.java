package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

//TODO substitute in code
public class SchoolBoardDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 100L;
    private final Tower towerColor;
    private final ArrayList<PawnColor> entrance;
    private final DiningRoomDto diningRoom;
    private final HashSet<PawnColor> professorTable;
    private final int towers;

    public SchoolBoardDto(SchoolBoard origin) {
        this.diningRoom = new DiningRoomDto(origin);
        this.entrance = new ArrayList<>(origin.getEntrance());
        this.professorTable = new HashSet<>(origin.getProfessors());
        this.towerColor = origin.getTowerColor();
        this.towers = origin.getTowersCount();
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
}
