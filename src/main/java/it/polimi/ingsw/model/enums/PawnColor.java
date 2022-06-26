package it.polimi.ingsw.model.enums;

import java.util.Arrays;
import java.util.List;

/**
 * The PawnColors
 */
public enum PawnColor {
    YELLOW,
    BLUE,
    GREEN,
    RED,
    PINK,
    NONE;

    private static List<PawnColor> validValues;

    /**
     *
     * @return a list of the valid PawnColor (NONE is excluded)
     */
    public static List<PawnColor> getValidValues() {
        if (validValues == null) {
            validValues = Arrays.asList(YELLOW, BLUE, PINK, RED, GREEN);
        }
        return validValues;
    }
}


