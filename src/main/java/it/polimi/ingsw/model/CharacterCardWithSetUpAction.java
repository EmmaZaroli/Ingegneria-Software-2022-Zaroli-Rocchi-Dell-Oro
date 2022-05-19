package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Character card with set up action.
 */
public class CharacterCardWithSetUpAction extends CharacterCard {
    private final List<PawnColor> students;

    /**
     * Instantiates a new Character card with set up action.
     *
     * @param initialPrice the initial price
     * @param character    the character
     */
    protected CharacterCardWithSetUpAction(int initialPrice, Character character) {
        super(initialPrice, character);
        students = new LinkedList<>();
    }

    /**
     * @return the list of students on the card
     */
    public List<PawnColor> getStudents() {
        return (List<PawnColor>) ((LinkedList<PawnColor>) students).clone();
    }

    /**
     * Add a student on the card
     *
     * @param color the color of the student
     */
    protected void addStudent(PawnColor color) {
        students.add(color);
    }

    /**
     * Add a list of students on the card
     *
     * @param colors the colors of the students
     */
    protected void addStudent(List<PawnColor> colors) {
        this.students.addAll(colors);
    }

    /**
     * Remove student from the card
     *
     * @param color the color of the student to remove
     */
    protected void removeStudent(PawnColor color) {
        students.remove(color);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
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
