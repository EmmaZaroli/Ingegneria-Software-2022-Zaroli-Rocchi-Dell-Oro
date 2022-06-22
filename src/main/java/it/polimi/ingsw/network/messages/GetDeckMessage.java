package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Message which contains the list of AssistantCard of a player
 */
public class GetDeckMessage extends Message {

    private final List<AssistantCard> deck = new ArrayList<>();

    public GetDeckMessage(String nickname, MessageType messageType, List<AssistantCard> assistants) {
        super(nickname, messageType);
        deck.addAll(assistants);
    }

    public List<AssistantCard> getDeck() {
        return deck;
    }
}
