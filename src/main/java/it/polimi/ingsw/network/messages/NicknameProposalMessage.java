package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class NicknameProposalMessage extends Message {

    public NicknameProposalMessage(String nickname) {
        super(nickname, MessageType.NICKNAME_PROPOSAL);
    }
}
