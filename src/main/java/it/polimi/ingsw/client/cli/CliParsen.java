package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.enums.PawnColor;

public class CliParsen {

    String character = "CHARACTER";

    public static PawnColor checkIfStudent(String input) {
        switch (input.toUpperCase()) {
            case "YELLOW":
                return PawnColor.YELLOW;
            case "BLUE":
                return PawnColor.BLUE;
            case "RED":
                return PawnColor.RED;
            case "PINK":
                return PawnColor.PINK;
            case "GREEN":
                return PawnColor.GREEN;
            default:
                return PawnColor.NONE;
        }
    }

    public boolean checkIfCard(String input) {
            input = input.toUpperCase();
            if(!input.contains(character)) return true;
            return false;
    }

    /**
     * @param input
     * @param islandCount number of island still on the table
     * @return 0-11 island index, 12 schoolboard, 13 invalid input
     */
    public static int isIslandOrSchoolBoard(String input, int islandCount) {
        if (input.toLowerCase().equals("schoolboard")) return 12;
        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 13;
        }
        if (number > 0 && number <= islandCount) return number - 1;
        return 13;
    }

}
