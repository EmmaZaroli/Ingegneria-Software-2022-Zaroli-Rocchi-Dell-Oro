package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class NicknameProposalMessage extends Message {

    public NicknameProposalMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }
}
