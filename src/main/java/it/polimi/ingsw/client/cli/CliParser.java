package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.List;
import java.util.Optional;

/**
 * Parser for Cli inputs
 */
public class CliParser {

    /**
     *
     * @param input the string read from input
     * @return the corresponding PawnColor, NONE if it's not a valid input
     */
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

    /**
     *
     * @param card input string
     * @param deck list of the character cards on the table
     * @return the index of the character card, if present
     */
    public Optional<Integer> indexCharacterCard(String card, List<ViewCharacterCard> deck){
        card = card.toUpperCase();
        Optional<Character> choice = fromStringToCard(card);
        if(choice.isEmpty()) return Optional.empty();
        for(int i=0;i<deck.size();i++){
            if(deck.get(i).getCharacter().equals(choice.get())) return Optional.of(i);
        }
        return Optional.empty();
    }

    /**
     *
     * @param card input string
     * @return the corresponding character card
     */
    private Optional<Character> fromStringToCard(String card) {
        switch (card) {
            case "CHARACTER_1" -> {
                return Optional.of(Character.CHARACTER_ONE);
            }
            case "CHARACTER_2" -> {
                return Optional.of(Character.CHARACTER_TWO);
            }
            case "CHARACTER_4" -> {
                return Optional.of(Character.CHARACTER_FOUR);
            }
            case "CHARACTER_6" -> {
                return Optional.of(Character.CHARACTER_SIX);
            }
            case "CHARACTER_7" -> {
                return Optional.of(Character.CHARACTER_SEVEN);
            }
            case "CHARACTER_8" -> {
                return Optional.of(Character.CHARACTER_EIGHT);
            }
            case "CHARACTER_9" -> {
                return Optional.of(Character.CHARACTER_NINE);
            }
            case "CHARACTER_11" -> {
                return Optional.of(Character.CHARACTER_ELEVEN);
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    /**
     * @param input the input string
     * @param islandCount number of island still on the table
     * @return 0-11 island index, 12 schoolBoard, 13 invalid input
     */
    public static int isIslandOrSchoolBoard(String input, int islandCount) {
        if (input.equalsIgnoreCase("schoolboard")) return 12;
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
