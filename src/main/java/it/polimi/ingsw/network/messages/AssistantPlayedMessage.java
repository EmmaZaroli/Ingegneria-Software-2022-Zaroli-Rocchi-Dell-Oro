package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 *  Message which contains the assistantCard played
 */
public class AssistantPlayedMessage extends Message {

    private final AssistantCard assistantCard;

    public AssistantPlayedMessage(String nickname, MessageType messageType, AssistantCard played) {
        super(nickname, messageType);
        this.assistantCard = played;
    }

    /**
     *
     * @return the AssistantCard
     */
    public AssistantCard getAssistantCard() {
        return this.assistantCard;
    }

}
