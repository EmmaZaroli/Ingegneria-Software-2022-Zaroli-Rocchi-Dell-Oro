package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

public class CharacterCardFactory {
    public CharacterCard getCharacterCard(Character character) {
        return switch (character) {
            case _1 -> get1();
            case _2 -> get2();
            case _4 -> get4();
            case _6 -> get6();
            case _7 -> get7();
            case _8 -> get8();
            case _9 -> get9();
            case _11 -> get11();
        };
    }

    private CharacterCard get1() {
        return new CharacterCard(1, Character._1, parameters -> {

        }, parameters -> {

        });
    }

    private CharacterCard get2() {
        return new CharacterCard(2, Character._1, parameters -> {
            parameters.setTakeProfessorEvenIfSameStudents(true);
        }, parameters -> {
            parameters.setTakeProfessorEvenIfSameStudents(false);
        });
    }

    private CharacterCard get4() {
        return new CharacterCard(1, Character._1, parameters -> {
            parameters.setMotherNatureExtraMovements(2);
        }, parameters -> {
            parameters.setMotherNatureExtraMovements(0);
        });
    }

    private CharacterCard get6() {
        return new CharacterCard(3, Character._1, parameters -> {
            parameters.setTowersCountInInfluence(false);
        }, parameters -> {
            parameters.setTowersCountInInfluence(true);
        });
    }

    private CharacterCard get7() {
        return new CharacterCard(1, Character._1, parameters -> {

        }, parameters -> {

        });
    }

    private CharacterCard get8() {
        return new CharacterCard(2, Character._1, parameters -> {
            parameters.setExtraInfluence(2);
        }, parameters -> {
            parameters.setExtraInfluence(0);
        });
    }

    private CharacterCard get9() {
        return new CharacterCard(3, Character._1, parameters -> {
            //TODO how can we have here the color that the player has choosen?
        }, parameters -> {
            parameters.setColorWithNoInfluence(PawnColor.NONE);
        });
    }

    private CharacterCard get11() {
        return new CharacterCard(2, Character._1, parameters -> {

        }, parameters -> {

        });
    }
}
