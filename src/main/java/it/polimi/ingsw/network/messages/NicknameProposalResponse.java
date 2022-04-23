package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.applications.enums.NicknameStatus;

public class NicknameProposalResponse extends Message {
    private final NicknameStatus nicknameStatus;

    public NicknameProposalResponse(String nickname, MessageType messageType, NicknameStatus nicknameStatus) {
        super(nickname, messageType);
        this.nicknameStatus = nicknameStatus;
    }
}
