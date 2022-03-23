package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.enums.Character;

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
        return new CharacterCard(1, Character._1, game -> {

        });
    }

    private CharacterCard get2() {
        return new CharacterCard(2, Character._2, game -> {

        });
    }

    private CharacterCard get4() {
        return new CharacterCard(1, Character._4, game -> {

        });
    }

    private CharacterCard get6() {
        return new CharacterCard(3, Character._6, game -> {

        });
    }

    private CharacterCard get7() {
        return new CharacterCard(1, Character._7, game -> {

        });
    }

    private CharacterCard get8() {
        return new CharacterCard(2, Character._8, game -> {

        });
    }

    private CharacterCard get9() {
        return new CharacterCard(3, Character._9, game -> {

        });
    }

    private CharacterCard get11() {
        return new CharacterCard(2, Character._11, game -> {

        });
    }
}
