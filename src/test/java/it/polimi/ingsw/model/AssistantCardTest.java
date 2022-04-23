package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssistantCardTest {

    @Test
    void equalCards() {
        AssistantCard c1 = new AssistantCard(1, 2);
        AssistantCard c2 = new AssistantCard(1, 2);
        Assertions.assertEquals(c1, c2);
    }
}

