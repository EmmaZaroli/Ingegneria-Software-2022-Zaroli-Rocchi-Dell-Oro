package it.polimi.ingsw.model.enums;

import java.util.Arrays;
import java.util.List;

public enum Tower {
    NONE,
    WHITE,
    BLACK,
    GREY;


    private static Tower[] validValues;

    public static Tower[] getValidValues() {
        if (validValues == null) {
            validValues = new Tower[]{WHITE, BLACK, GREY};
        }
        return validValues;
    }
}
