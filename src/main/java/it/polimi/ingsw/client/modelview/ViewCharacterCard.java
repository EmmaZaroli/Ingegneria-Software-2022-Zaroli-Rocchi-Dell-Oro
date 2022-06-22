package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the CharacterCards
 * Used by the View
 */
public class ViewCharacterCard {
    private final Character character;
    private int price;
    private final boolean isWithSetUpAction;
    private List<PawnColor> students;
    private boolean hasCoin;
    private boolean isActivable;
    private boolean isActive;

    public ViewCharacterCard(Character character, int price, boolean isWithSetUpAction, List<PawnColor> students, boolean hasCoin, boolean isActivable, boolean isActive) {
        this.character = character;
        this.price = price;
        this.isWithSetUpAction = isWithSetUpAction;
        this.students = students;
        this.hasCoin = hasCoin;
        this.isActivable = isActivable;
        this.isActive = isActive;
    }

    public ViewCharacterCard(CharacterCardDto origin, boolean isActivable, boolean isActive){
        this(origin.getCharacter(),
                origin.getPrice(),
                origin.isWithSetUpAction(),
                origin.getStudents(),
                origin.hasCoin(),
                isActivable,
                isActive);
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
     * @return the card's price
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
     * @return the list of students on the card
     */
    public List<PawnColor> getStudents() {
        return students;
    }

    /**
     *
     * @param color the PawnColor
     * @return the number of student of that color on the card
     */
    public int getStudentsNumber(PawnColor color){
        return (int) students.stream().filter(x -> x.equals(color)).count();
    }

    /**
     *
     * @return the number of students on the card
     */
    public int getStudentsNumber(){
        return students.size();
    }

    /**
     *
     * @return a map that maps the PawnColor to the number of occurrences of that color on the card
     */
    public Map<PawnColor, Integer> getStudentsCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsNumber(color));
        }
        return res;
    }

    /**
     *
     * @return true if the card has a coin
     */
    public boolean hasCoin() {
        return hasCoin;
    }

    /**
     *
     * @return true if the card can be activated
     */
    public boolean isActivable() {
        return isActivable;
    }

    /**
     *
     * @return true if the card is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     *
     * @return a copy of the ViewCharacterCard instance
     */
    private ViewCharacterCard deepClone(){
        return new ViewCharacterCard(this.character, this.price, this.isWithSetUpAction, this.students, this.hasCoin, this.isActivable, this.isActive);
    }

    /**
     *
     * @param price the new price
     * @return the updated clone of the ViewCharacterCard
     */
    public ViewCharacterCard withPrice(int price) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.price = price;
        return retVal;
    }

    /**
     *
     * @param students the new list of students
     * @return the updated clone of the ViewCharacterCard
     */
    public ViewCharacterCard withStudents(List<PawnColor> students) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.students = students;
        return retVal;
    }

    /**
     *
     * @param hasCoin is true if the card has a coin
     * @return the updated clone of the ViewCharacterCard
     */
    public ViewCharacterCard withHasCoin(boolean hasCoin) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.hasCoin = hasCoin;
        return retVal;
    }

    /**
     *
     * @param isActivable is true if the card can be activated
     * @return the updated clone of the ViewCharacterCard
     */
    public ViewCharacterCard withIsActivable(boolean isActivable) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.isActivable = isActivable;
        return retVal;
    }

    /**
     *
     * @param isActive is true is the card is active
     * @return the updated clone of the ViewCharacterCard
     */
    public ViewCharacterCard withIsActive(boolean isActive) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.isActive = isActive;
        return retVal;
    }


}
