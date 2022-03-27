package it.polimi.ingsw.controller;

public class EffectFactory {
    /*
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
        return new CharacterCardWithSetUpAction(1, Character.CHARACTER_ONE, ((bag, students) -> {
            List<PawnColor> ris = bag.drawStudents(4);
            for (PawnColor c : ris) {
                students.put(c, students.get(c) + 1);
            }
        }));
    }

    private Effect get2() {
        return new StandardCharacterCard(2, Character.CHARACTER_TWO, parameters -> {
            parameters.setTakeProfessorEvenIfSameStudents(true);
        }, parameters -> {
            parameters.setTakeProfessorEvenIfSameStudents(false);
        });
    }

    private Effect get4() {
        return new StandardCharacterCard(1, Character.CHARACTER_FOUR, parameters -> {
            parameters.setMotherNatureExtraMovements(2);
        }, parameters -> {
            parameters.setMotherNatureExtraMovements(0);
        });
    }

    private Effect get6() {
        return new StandardCharacterCard(3, Character.CHARACTER_SIX, parameters -> {
            parameters.setTowersCountInInfluence(false);
        }, parameters -> {
            parameters.setTowersCountInInfluence(true);
        });
    }

    private Effect get7() {
        return new CharacterCardWithSetUpAction(1, Character.CHARACTER_SEVEN, ((bag, students) -> {
            List<PawnColor> ris = bag.drawStudents(6);
            for (PawnColor c : ris) {
                students.put(c, students.get(c) + 1);
            }
        }));
    }

    private Effect get8() {
        return new StandardCharacterCard(2, Character.CHARACTER_EIGHT, parameters -> {
            parameters.setExtraInfluence(2);
        }, parameters -> {
            parameters.setExtraInfluence(0);
        });
    }

    private Effect get9() {
        return new StandardCharacterCard(3, Character.CHARACTER_NINE, parameters -> {
            //TODO how can we have here the color that the player has choosen?
        }, parameters -> {
            parameters.setColorWithNoInfluence(PawnColor.NONE);
        });
    }

    private Effect get11() {
        return new Effect() {
            @Override
            public void activateEffect(Object... args) {

            }

            @Override
            public void reverseEffect(Object... args) {

            }
        }
        return new CharacterCardWithSetUpAction(2, Character.CHARACTER_ELEVEN, ((bag, students) -> {
            List<PawnColor> ris = bag.drawStudents(4);
            for (PawnColor c : ris) {
                students.put(c, students.get(c) + 1);
            }
        }));
    }*/
}
