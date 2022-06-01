package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.PrintStream;
import java.util.List;

public class PrinterCharacterCards {

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String COIN = ANSI_YELLOW+"¢"+ANSI_RESET;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String FULL_CIRCLE = "●";


    public void print(List<CharacterCardDto> cards) {
        PrintStream out = System.out;
        space(46);
        for (int i = 0; i < 3; i++) {
            if(cards.get(i).HasCoin()) out.print(COIN);
            else out.print(" ");
            space(12);
        }
        out.println();
        space(40);
        for (int i = 0; i < 3; i++) {
            out.print("   ______________    ");
        }
        out.println();
        space(40);
        for (int i = 0; i < 3; i++) {
            CharacterCardDto card = cards.get(i);
            if(card.getCharacter().equals(Character.CHARACTER_ELEVEN)) out.print("  | " +fromCardToString(card.getCharacter())+ " |   ");
            else out.print("  | " +fromCardToString(card.getCharacter()) +"  |   ");
        }
        out.println();
        space(40);
        for (int i = 0; i < 3; i++) {
            out.print("  |   price: "+cards.get(i).getPrice() +"   |   ");
        }
        out.println();
        space(40);
        for (int i = 0; i < 3; i++) {
            printStudents(cards.get(i));
        }
        out.println();
        space(40);
        for (int i = 0; i < 3; i++) {
            out.print("  |______________|   ");
        }
        System.out.println();
        System.out.println();
    }

    private String fromCardToString(Character character) {
        switch (character) {
            case CHARACTER_ONE -> {
                return "CHARACTER_1";
            }
            case CHARACTER_TWO -> {
                return "CHARACTER_2";
            }
            case CHARACTER_FOUR -> {
                return "CHARACTER_4";
            }
            case CHARACTER_SIX -> {
                return "CHARACTER_6";
            }
            case CHARACTER_SEVEN -> {
                return "CHARACTER_7";
            }
            case CHARACTER_EIGHT -> {
                return "CHARACTER_8";
            }
            case CHARACTER_NINE -> {
                return "CHARACTER_9";
            }
            case CHARACTER_ELEVEN -> {
                return "CHARACTER_11";
            }
            default -> {
                return "  ";
            }

        }
    }

    private void printStudents(CharacterCardDto card){
        if(!card.isWithSetUpAction()) {
            System.out.print("  |              |   ");
            return;
        }
        int align = card.getStudents().size()/2;
        System.out.print("  |");
        space(7-align);
        for(PawnColor student : card.getStudents()){
            System.out.print(assignColor(student));
        }
        space(14-(7-align+card.getStudents().size()));
        System.out.print("|   ");
    }

    private void space(int space){
        for(int i=0;i<space;i++){
            System.out.print(" ");
        }
    }

    private static String assignColor(PawnColor studentColor) {
        String assignedColor;
        switch (studentColor) {
            case RED -> assignedColor = ANSI_RED;
            case BLUE -> assignedColor = ANSI_BLUE;
            case GREEN -> assignedColor = ANSI_GREEN;
            case YELLOW -> assignedColor = ANSI_YELLOW;
            case PINK -> assignedColor = ANSI_PURPLE;
            default -> assignedColor = ANSI_WHITE;
        }
        return assignedColor + FULL_CIRCLE + ANSI_RESET;
    }
}
