package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CharacterCardTest {
    List<CharacterCard> deck = new ArrayList<>();

    @Test
    void allCharacterCard() {
        for (Character c : Character.values()) {
            deck.add(CharacterCardFactory.getCharacterCard(c));
        }
        System.out.println(deck.get(0).getCharacter());
        Assertions.assertEquals(1, deck.get(0).getCurrentPrice());
        deck.get(0).setUsed();
        Assertions.assertTrue(deck.get(0).hasCoin());
        Assertions.assertEquals(2, deck.get(0).getCurrentPrice());
    }

    @Test
    void characterCardWithSetUpAction() {
        List<PawnColor> l1 = new ArrayList<>();
        CharacterCardWithSetUpAction c = (CharacterCardWithSetUpAction) CharacterCardFactory.getCharacterCard(Character.CHARACTER_SEVEN);
        l1.add(PawnColor.BLUE);
        l1.add(PawnColor.PINK);
        c.addStudent(l1);
        c.addStudent(PawnColor.RED);
        c.removeStudent(PawnColor.RED);
        int i = 0;
        if (c.getStudents().contains(PawnColor.RED)) {
            i--;
        }
        Assertions.assertEquals(0, i);
    }
}
