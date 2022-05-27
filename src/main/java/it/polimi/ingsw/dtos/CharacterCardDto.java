package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.io.Serializable;
import java.util.*;

public class CharacterCardDto implements Serializable {
    private static final long serialVersionUID = 107L;
    private final Character character;
    private int price;
    private final boolean isWithSetUpAction;
    private List<PawnColor> students;

    public CharacterCardDto(CharacterCard origin) {
        this.character = origin.getCharacter();
        this.price = origin.getCurrentPrice();
        this.isWithSetUpAction = origin instanceof CharacterCardWithSetUpAction;
        if (this.isWithSetUpAction)
            this.students = ((CharacterCardWithSetUpAction) origin).getStudents();
        else
            this.students = new ArrayList<>();
    }

    public CharacterCardDto(Character character, int price, boolean isWithSetUpAction, List<PawnColor> students) {
        this.character = character;
        this.price = price;
        this.isWithSetUpAction = isWithSetUpAction;
        this.students = students;
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

    public CharacterCardDto deepClone() {
        return new CharacterCardDto(this.character, this.price, this.isWithSetUpAction, new ArrayList<>(this.students));
    }

    public CharacterCardDto with(int price) {
        CharacterCardDto retVal = this.deepClone();
        retVal.price = price;
        return retVal;
    }

    public CharacterCardDto with(List<PawnColor> students) {
        CharacterCardDto retVal = this.deepClone();
        retVal.students = students;
        return retVal;
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
}