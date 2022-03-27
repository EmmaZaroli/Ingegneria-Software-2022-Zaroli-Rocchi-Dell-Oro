package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.List;

public class CharacterCardFactory {
    public CharacterCard getCharacterCard(Character character) {
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

    private CharacterCard get1() {
        return new CharacterCardWithSetUpAction(1, Character.CHARACTER_ONE, ((bag, students) -> {
            List<PawnColor> ris = bag.drawStudents(4);
            for (PawnColor c : ris) {
                students.put(c, students.get(c) + 1);
            }
        }));
    }

    private CharacterCard get2() {
        return new StandardCharacterCard(2, Character.CHARACTER_TWO, parameters -> {
            parameters.setTakeProfessorEvenIfSameStudents(true);
        }, parameters -> {
            parameters.setTakeProfessorEvenIfSameStudents(false);
        });
    }

    private CharacterCard get4() {
        return new StandardCharacterCard(1, Character.CHARACTER_FOUR, parameters -> {
            parameters.setMotherNatureExtraMovements(2);
        }, parameters -> {
            parameters.setMotherNatureExtraMovements(0);
        });
    }

    private CharacterCard get6() {
        return new StandardCharacterCard(3, Character.CHARACTER_SIX, parameters -> {
            parameters.setTowersCountInInfluence(false);
        }, parameters -> {
            parameters.setTowersCountInInfluence(true);
        });
    }

    private CharacterCard get7() {
        return new CharacterCardWithSetUpAction(1, Character.CHARACTER_SEVEN, ((bag, students) -> {
            List<PawnColor> ris = bag.drawStudents(6);
            for (PawnColor c : ris) {
                students.put(c, students.get(c) + 1);
            }
        }));
    }

    private CharacterCard get8() {
        return new StandardCharacterCard(2, Character.CHARACTER_EIGHT, parameters -> {
            parameters.setExtraInfluence(2);
        }, parameters -> {
            parameters.setExtraInfluence(0);
        });
    }

    private CharacterCard get9() {
        return new StandardCharacterCard(3, Character.CHARACTER_NINE, parameters -> {
            //TODO how can we have here the color that the player has choosen?
        }, parameters -> {
            parameters.setColorWithNoInfluence(PawnColor.NONE);
        });
    }

    private CharacterCard get11() {
        return new CharacterCardWithSetUpAction(2, Character.CHARACTER_ELEVEN, ((bag, students) -> {
            List<PawnColor> ris = bag.drawStudents(4);
            for (PawnColor c : ris) {
                students.put(c, students.get(c) + 1);
            }
        }));
    }
}
