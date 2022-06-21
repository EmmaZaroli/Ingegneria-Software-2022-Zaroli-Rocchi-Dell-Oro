package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.PrintStream;
import java.util.List;

/**
 * Class for printing Character Cards
 */
public class PrinterCharacterCards {

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String FULL_CIRCLE = "●";
    private static final PrintStream out = System.out;

    /**
     * Prints the character cards
     * The outer line of the cards is yellow if they're active
     * @param cards the character cards on the table
     */
    public void print(List<ViewCharacterCard> cards) {
        space(40);
        for (int i = 0; i < 3; i++) {
            if(cards.get(i).isActive()) out.print(ANSI_YELLOW+"   ______________    "+ANSI_RESET);
            else out.print("   ______________    ");
        }
        out.println();

        space(40);
        characterNames(cards);
        out.println();

        space(40);
        for (int i = 0; i < 3; i++) {
            if(cards.get(i).isActive()) out.print(ANSI_YELLOW+"  |   "+ANSI_RESET+"price: "+cards.get(i).getPrice() +ANSI_YELLOW+"   |   "+ANSI_RESET);
            else out.print("  |   price: "+cards.get(i).getPrice() +"   |   ");
        }
        out.println();

        space(40);
        for (int i = 0; i < 3; i++) {
            printStudents(cards.get(i));
        }
        out.println();

        space(40);
        for (int i = 0; i < 3; i++) {
            if(cards.get(i).isActive()) out.print(ANSI_YELLOW+"  |______________|   "+ANSI_RESET);
            else  out.print("  |______________|   ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Prints the names of the character cards
     * @param cards the character cards on the table
     */
    private void characterNames(List<ViewCharacterCard> cards){
        for (int i = 0; i < 3; i++) {
            ViewCharacterCard card = cards.get(i);
            if(card.getCharacter().equals(Character.CHARACTER_ELEVEN)) {
                if(card.isActive()) out.print(ANSI_YELLOW+"  | "+ANSI_RESET +fromCardToString(card.getCharacter())+ ANSI_YELLOW+" |   "+ANSI_RESET);
                else out.print("  | " +fromCardToString(card.getCharacter())+ " |   ");
            }
            else{
                if(card.isActive()) out.print(ANSI_YELLOW+"  | "+ANSI_RESET +fromCardToString(card.getCharacter()) +ANSI_YELLOW+"  |   "+ANSI_RESET);
                else out.print("  | "+fromCardToString(card.getCharacter()) +"  |   ");
            }
        }
    }


    /**
     *
     * @param character the character
     * @return the name of the character as string
     */
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

    /**
     * Prints, if there are any, the students on each card
     * @param card the character cards on the table
     */
    private void printStudents(ViewCharacterCard card){
        if(!card.isWithSetUpAction()) {
            if(card.isActive()) out.print(ANSI_YELLOW+"  |              |   "+ANSI_RESET);
            else out.print("  |              |   ");
            return;
        }
        int align = card.getStudents().size()/2;
        if(card.isActive()) out.print(ANSI_YELLOW+"  |"+ANSI_RESET);
        else out.print("  |");
        space(7-align);
        for(PawnColor student : card.getStudents()){
            out.print(assignColor(student));
        }
        space(14-(7-align+card.getStudents().size()));
        if(card.isActive()) out.print(ANSI_YELLOW+"|   "+ANSI_RESET);
        else out.print("|   ");
    }

    /**
     *
     * @param space number of spaces to leave on the left
     */
    private void space(int space){
        for(int i=0;i<space;i++){
            out.print(" ");
        }
    }

    /**
     *
     * @param studentColor the PawnColor
     * @return a string corresponding to the constant to which the color is assigned
     */
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
