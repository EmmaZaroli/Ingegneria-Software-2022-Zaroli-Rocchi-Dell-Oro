package it.polimi.ingsw.network.messages;

public class NicknameProposalMessage extends Message {

    public NicknameProposalMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }
}
