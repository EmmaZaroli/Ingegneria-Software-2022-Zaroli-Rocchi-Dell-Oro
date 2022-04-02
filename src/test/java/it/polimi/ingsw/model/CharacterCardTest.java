package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CharacterCardTest extends TestCase {
    CharacterCardFactory factory = new CharacterCardFactory();
    List<CharacterCard> deck = new ArrayList<>();

    @Test
    void AllCharacterCard() {
        for (Character c : Character.values()) {
            deck.add(factory.getCharacterCard(c));
        }
        System.out.println(deck.get(0).getCharacter());
        assertEquals(1, deck.get(0).getCurrentPrice());
        deck.get(0).setUsed();
        assertTrue(deck.get(0).hasCoin());
        assertEquals(2, deck.get(0).getCurrentPrice());
    }

    @Test
    void CharacterCardWithSetUpAction() {
        List<PawnColor> l1 = new ArrayList<>();
        CharacterCardWithSetUpAction c = (CharacterCardWithSetUpAction) factory.getCharacterCard(Character.CHARACTER_SEVEN);
        l1.add(PawnColor.BLUE);
        l1.add(PawnColor.PINK);
        c.addStudent(l1);
        c.addStudent(PawnColor.RED);
        c.removeStudent(PawnColor.RED);
        int i = 0;
        if (c.getStudents().contains(PawnColor.RED)) {
            i--;
        }
        assertEquals(0, i);
    }
}
