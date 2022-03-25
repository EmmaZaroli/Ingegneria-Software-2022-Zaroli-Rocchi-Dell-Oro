package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterCardWithSetUpAction extends CharacterCard {
    private final SetupEffect setupEffect;
    private final Map<PawnColor, Integer> students;

    protected CharacterCardWithSetUpAction(int initialPrice, Character character, Effect effect, Effect reverseEffect, SetupEffect setupEffect) {
        super(initialPrice, character, effect, reverseEffect);
        this.setupEffect = setupEffect;
        students = new HashMap<>();
    }

    public void activateSetupEffect(Bag bag) {
        this.setupEffect.activateEffect(bag, students);
    }

    public Map<PawnColor, Integer> getStudents() {
        return (Map<PawnColor, Integer>) ((HashMap) students).clone();
    }

    public void addStudent(PawnColor color) {
        students.put(color, students.get(color) + 1);
    }

    public void addStudent(List<PawnColor> colors) {
        for (PawnColor c : colors)
            addStudent(c);
    }

    public void removeStudents(PawnColor color, int n) {
        students.put(color, students.get(color) - n);
        //TODO gestione nel caso si vada sotto zero
    }

    public void removeStudent(PawnColor color) {
        removeStudents(color, 1);
    }
}
