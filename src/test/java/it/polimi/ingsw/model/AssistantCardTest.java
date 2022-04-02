package it.polimi.ingsw.model;

import it.polimi.ingsw.model.AssistantCard;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

class AssistantCardTest extends TestCase {

    @Test
    void equalCards() {
        AssistantCard c1 = new AssistantCard(1, 2);
        AssistantCard c2 = new AssistantCard(1, 2);
        assertTrue(c1.equals(c2));
    }
}

