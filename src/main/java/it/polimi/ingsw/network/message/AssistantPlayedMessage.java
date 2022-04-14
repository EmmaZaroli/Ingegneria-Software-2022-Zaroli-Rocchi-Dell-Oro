package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.AssistantCard;

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
