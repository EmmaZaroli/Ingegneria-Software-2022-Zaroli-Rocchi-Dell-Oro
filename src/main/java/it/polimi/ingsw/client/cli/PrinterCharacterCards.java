package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.enums.Character;

import java.io.PrintStream;
import java.util.List;

public class PrinterCharacterCards {

    //TODO
    private final String CHARACTER_ONE = "1";
    private final String CHARACTER_TWO = "2";
    private final String CHARACTER_FOUR = "4";
    private final String CHARACTER_SIX = "6";
    private final String CHARACTER_SEVEN = "7";
    private final String CHARACTER_EIGHT = "8";
    private final String CHARACTER_NINE = "9";
    private final String CHARACTER_ELEVEN = "11";

    public void print(List<CharacterCardDto> cards) {
        PrintStream out = System.out;
        for (int i = 0; i < 3; i++) {
            out.print("   __________    ");
        }
        out.println();
        for (int i = 0; i < 3; i++) {
            CharacterCardDto card = cards.get(i);
            out.print("  |" + card.getPrice() + fromCardToString(card.getCharacter()) + "|    ");
        }
        out.println();
        for (int i = 0; i < 3; i++) {
            out.print("  |            |    ");
        }
        out.println();
        for (int i = 0; i < 3; i++) {
            out.print("  |__________|    ");
        }
        System.out.println();
    }

    private String fromCardToString(Character character) {
        switch (character) {
            case CHARACTER_ONE -> {
                return CHARACTER_ONE;
            }
            case CHARACTER_TWO -> {
                return CHARACTER_TWO;
            }
            case CHARACTER_FOUR -> {
                return CHARACTER_FOUR;
            }
            case CHARACTER_SIX -> {
                return CHARACTER_SIX;
            }
            case CHARACTER_SEVEN -> {
                return CHARACTER_SEVEN;
            }
            case CHARACTER_EIGHT -> {
                return CHARACTER_EIGHT;
            }
            case CHARACTER_NINE -> {
                return CHARACTER_NINE;
            }
            case CHARACTER_ELEVEN -> {
                return CHARACTER_ELEVEN;
            }
            default -> {
                return " ";
            }

        }
    }
}
