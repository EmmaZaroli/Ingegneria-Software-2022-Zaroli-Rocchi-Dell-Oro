package it.polimi.ingsw.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum PawnColor {
    YELLOW,
    BLUE,
    GREEN,
    RED,
    PINK,
    NONE;

    static private List<PawnColor> validValues;

    static public List<PawnColor> getValidValues() {
        if (validValues == null) {
            validValues = Arrays.asList(YELLOW, BLUE, PINK, RED, GREEN);
        }
        return validValues;
    }

}


