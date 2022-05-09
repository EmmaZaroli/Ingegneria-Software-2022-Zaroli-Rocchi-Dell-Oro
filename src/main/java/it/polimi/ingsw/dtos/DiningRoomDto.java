package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;

public class DiningRoomDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 101L;

    private final EnumMap<PawnColor, Integer> students;

    public DiningRoomDto(SchoolBoard origin) {
        this.students = new EnumMap<PawnColor, Integer>(PawnColor.class);
        for (PawnColor p : PawnColor.getValidValues()) {
            this.students.put(p, origin.getStudentsInDiningRoom(p));
        }
    }

    public int getStudentsInDiningRoom(PawnColor color) {
        return this.students.get(color);
    }
}
