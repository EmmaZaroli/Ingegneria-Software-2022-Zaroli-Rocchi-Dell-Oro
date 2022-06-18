package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

import java.util.Optional;

public class AssistantPlayedMessage extends Message {

    private final AssistantCard assistantCard;

    public AssistantPlayedMessage(String nickname, MessageType messageType, AssistantCard played) {
        super(nickname, messageType);
        this.assistantCard = played;
    }

    public AssistantCard getAssistantCard() {
        return this.assistantCard;
    }

}
