package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.LinkedList;
import java.util.List;

public class CharacterCardWithSetUpAction extends CharacterCard {
    private final List<PawnColor> students;

    protected CharacterCardWithSetUpAction(int initialPrice, Character character) {
        super(initialPrice, character);
        students = new LinkedList<>();
    }

    public List<PawnColor> getStudents() {
        return (List<PawnColor>) ((LinkedList<PawnColor>) students).clone();
    }

    public void addStudent(PawnColor color) {
        students.add(color);
    }

    public void addStudent(List<PawnColor> colors) {
        this.students.addAll(colors);
    }

    public void removeStudent(PawnColor color) {
        students.remove(color);
    }
}
