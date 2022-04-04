package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CharacterCardWithSetUpAction;
import it.polimi.ingsw.model.ExpertGameParameters;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.List;

public class EffectFactory {

    public Effect getEffect(Character character) {
        return switch (character) {
            case CHARACTER_ONE -> get1();
            case CHARACTER_TWO -> get2();
            case CHARACTER_FOUR -> get4();
            case CHARACTER_SIX -> get6();
            case CHARACTER_SEVEN -> get7();
            case CHARACTER_EIGHT -> get8();
            case CHARACTER_NINE -> get9();
            case CHARACTER_ELEVEN -> get11();
        };
    }

    
    private Effect get1() {
        return new SetupEffect() {
            @Override
            public void activateEffect(TableController table, CharacterCardWithSetUpAction character) {
                List<PawnColor> ris = table.drawStudents(4);
                character.addStudent(ris);
            }
        };
    }

    private Effect get2() {
        return new StandardEffect() {
            @Override
            public void activateEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setTakeProfessorEvenIfSameStudents(true);
            }

            @Override
            public void reverseEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setTakeProfessorEvenIfSameStudents(false);
            }
        };
    }

    private Effect get4() {
        return new StandardEffect() {
            @Override
            public void activateEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setMotherNatureExtraMovements(2);
            }

            @Override
            public void reverseEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setMotherNatureExtraMovements(0);
            }
        };
    }

    private Effect get6() {
        return new StandardEffect() {
            @Override
            public void activateEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setTowersCountInInfluence(false);
            }

            @Override
            public void reverseEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setTowersCountInInfluence(true);
            }
        };
    }

    private Effect get7() {
        return new SetupEffect() {
            @Override
            public void activateEffect(TableController table, CharacterCardWithSetUpAction character) {
                List<PawnColor> ris = table.drawStudents(6);
                character.addStudent(ris);
            }
        };
    }

    private Effect get8() {
        return new StandardEffect() {
            @Override
            public void activateEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setExtraInfluence(2);
            }

            @Override
            public void reverseEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setExtraInfluence(0);
            }
        };
    }

    private Effect get9() {
        return new StandardEffect() {
            @Override
            public void activateEffect(ExpertGameParameters parameters, Object... args) {
                PawnColor color = (PawnColor) args[0];
                parameters.setColorWithNoInfluence(color);
            }

            @Override
            public void reverseEffect(ExpertGameParameters parameters, Object... args) {
                parameters.setColorWithNoInfluence(PawnColor.NONE);
            }
        };
    }

    private Effect get11() {
        return new SetupEffect() {
            @Override
            public void activateEffect(TableController table, CharacterCardWithSetUpAction character) {
                List<PawnColor> ris = table.drawStudents(4);
                character.addStudent(ris);
            }
        };
    }
}
