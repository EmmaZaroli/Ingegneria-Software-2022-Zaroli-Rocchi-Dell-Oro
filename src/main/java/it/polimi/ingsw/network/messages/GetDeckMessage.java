package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;

import java.util.List;

public class GetDeckMessage extends Message {

    private List<AssistantCard> deck;

    public GetDeckMessage(String nickname, MessageType messageType, List<AssistantCard> assistants) {
        super(nickname, messageType);
        deck.addAll(assistants);
    }

    public List<AssistantCard> getDeck() {
        return deck;
    }
}
