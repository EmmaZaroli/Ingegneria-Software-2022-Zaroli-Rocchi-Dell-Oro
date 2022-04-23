package it.polimi.ingsw.network.messages;

public class NicknameProposalMessage extends Message {

    protected NicknameProposalMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }
}
