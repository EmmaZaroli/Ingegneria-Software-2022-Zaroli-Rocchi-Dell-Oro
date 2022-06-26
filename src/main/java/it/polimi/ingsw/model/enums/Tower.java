package it.polimi.ingsw.model.enums;

/**
 * The Tower's colors
 */
public enum Tower {
    NONE,
    WHITE,
    BLACK,
    GREY;


    private static Tower[] validValues;

    /**
     *
     * @return an array containing the valid Tower's colors (NONE is excluded)
     */
    public static Tower[] getValidValues() {
        if (validValues == null) {
            validValues = new Tower[]{WHITE, BLACK, GREY};
        }
        return validValues;
    }
}
