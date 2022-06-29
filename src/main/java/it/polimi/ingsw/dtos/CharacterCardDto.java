package it.polimi.ingsw.dtos;

import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * CharacterCard Dto
 */
public class CharacterCardDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 107L;
    private final Character character;
    private int price;
    private final boolean isWithSetUpAction;
    private List<PawnColor> students;
    private final boolean hasCoin;

    /**
     * Construct the characterCardDto from a characterCard
     * @param origin the characterCard
     */
    public CharacterCardDto(CharacterCard origin) {
        this.character = origin.getCharacter();
        this.price = origin.getCurrentPrice();
        this.isWithSetUpAction = origin instanceof CharacterCardWithSetUpAction;
        if (this.isWithSetUpAction)
            this.students = ((CharacterCardWithSetUpAction) origin).getStudents();
        else
            this.students = new ArrayList<>();
        this.hasCoin = origin.hasCoin();
    }

    /**
     * Construct the characterCardDto from a viewCharacterCard
     * @param origin the viewCharacterCard
     */
    public CharacterCardDto(ViewCharacterCard origin) {
        this.character = origin.getCharacter();
        this.price = origin.getPrice();
        this.isWithSetUpAction = origin.isWithSetUpAction();
        if (this.isWithSetUpAction)
            this.students = origin.getStudents();
        else
            this.students = new ArrayList<>();
        this.hasCoin = origin.hasCoin();
    }

    public CharacterCardDto(Character character, int price, boolean isWithSetUpAction, List<PawnColor> students,boolean hasCoin) {
        this.character = character;
        this.price = price;
        this.isWithSetUpAction = isWithSetUpAction;
        this.students = students;
        this.hasCoin = hasCoin;
    }

    /**
     *
     * @return the Character
     */
    public Character getCharacter() {
        return character;
    }

    /**
     *
     * @return the price of the card
     */
    public int getPrice() {
        return price;
    }

    /**
     *
     * @return true if the card is with a set-up action
     */
    public boolean isWithSetUpAction() {
        return isWithSetUpAction;
    }

    /**
     *
     * @return ,if present, the list of pawnColor on the card
     */
    public List<PawnColor> getStudents() {
        return students;
    }

    /**
     *
     * @return a clone of this instance
     */
    public CharacterCardDto deepClone() {
        return new CharacterCardDto(this.character, this.price, this.isWithSetUpAction, new ArrayList<>(this.students),this.hasCoin);
    }

    /**
     *
     * @param price the new price
     * @return a clone of this instance with the updated price
     */
    public CharacterCardDto with(int price) {
        CharacterCardDto retVal = this.deepClone();
        retVal.price = price;
        return retVal;
    }

    /**
     *
     * @param students the new list of pawnColors on the card
     * @return a clone of this instance with the updated pawnColors
     */
    public CharacterCardDto with(List<PawnColor> students) {
        CharacterCardDto retVal = this.deepClone();
        retVal.students = students;
        return retVal;
    }

    /**
     *
     * @param color the pawnColor
     * @return the number of pawnColor of that color
     */
    public int getStudentsNumber(PawnColor color){
        return (int) students.stream().filter(x -> x.equals(color)).count();
    }

    /**
     *
     * @return the number of pawnColor on the card
     */
    public int getStudentsNumber(){
        return students.size();
    }

    //TODO remove
    public Map<PawnColor, Integer> getStudentsCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsNumber(color));
        }
        return res;
    }

    /**
     *
     * @return true if the card has a coin, false otherwise
     */
    public boolean hasCoin() {
        return hasCoin;
    }
}
