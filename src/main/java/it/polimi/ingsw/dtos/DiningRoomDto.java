package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;

/**
 * DiningRoom Dto
 */
public class DiningRoomDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 101L;

    private final EnumMap<PawnColor, Integer> students;

    /**
     * Construct a diningRoomDto from a DiningRoom
     * @param origin the schoolBoard
     */
    public DiningRoomDto(SchoolBoard origin) {
        this.students = new EnumMap<>(PawnColor.class);
        for (PawnColor p : PawnColor.getValidValues()) {
            this.students.put(p, origin.getStudentsInDiningRoom(p));
        }
    }

    /**
     *
     * @param color the pawnColor
     * @return the number of pawnColor of that color in the diningRoom
     */
    public int getStudentsInDiningRoom(PawnColor color) {
        return this.students.get(color);
    }
}
