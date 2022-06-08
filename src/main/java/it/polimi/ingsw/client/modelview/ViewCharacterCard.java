package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCharacterCard {
    private final Character character;
    private int price;
    private final boolean isWithSetUpAction;
    private List<PawnColor> students;
    private boolean hasCoin;
    private boolean isActivable = true;
    private boolean isActive = false;

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

    public Character getCharacter() {
        return character;
    }

    public int getPrice() {
        return price;
    }

    public boolean isWithSetUpAction() {
        return isWithSetUpAction;
    }

    public List<PawnColor> getStudents() {
        return students;
    }

    public int getStudentsNumber(PawnColor color){
        return (int) students.stream().filter(x -> x.equals(color)).count();
    }

    public int getStudentsNumber(){
        return students.size();
    }

    public Map<PawnColor, Integer> getStudentsCardinality(){
        Map<PawnColor, Integer> res = new HashMap<>();
        for(PawnColor color: PawnColor.values()){
            res.put(color, getStudentsNumber(color));
        }
        return res;
    }

    public boolean hasCoin() {
        return hasCoin;
    }

    public boolean isActivable() {
        return isActivable;
    }

    public boolean isActive() {
        return isActive;
    }

    private ViewCharacterCard deepClone(){
        return new ViewCharacterCard(this.character, this.price, this.isWithSetUpAction, this.students, this.hasCoin, this.isActivable, this.isActive);
    }

    public ViewCharacterCard withPrice(int price) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.price = price;
        return retVal;
    }

    public ViewCharacterCard withStudents(List<PawnColor> students) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.students = students;
        return retVal;
    }

    public ViewCharacterCard withHasCoin(boolean hasCoin) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.hasCoin = hasCoin;
        return retVal;
    }

    public ViewCharacterCard withIsActivable(boolean isActivable) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.isActivable = isActivable;
        return retVal;
    }

    public ViewCharacterCard withIsActive(boolean isActive) {
        ViewCharacterCard retVal = this.deepClone();
        retVal.isActive = isActive;
        return retVal;
    }


}
