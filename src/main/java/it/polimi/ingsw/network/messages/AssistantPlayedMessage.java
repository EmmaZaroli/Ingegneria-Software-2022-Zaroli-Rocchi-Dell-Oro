package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class AssistantPlayedMessage extends Message {

    private final AssistantCard assistantCard;

    public AssistantPlayedMessage(String nickname, AssistantCard played) {
        super(nickname, MessageType.ASSISTANT_CARD);
        this.assistantCard = played;
    }

    public AssistantCard getAssistantCard() {
        return this.assistantCard;
    }

}
