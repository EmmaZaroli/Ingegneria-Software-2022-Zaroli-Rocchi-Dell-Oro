package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.applications.enums.NicknameStatus;

public class NicknameResponseMessage extends Message {
    private final NicknameStatus nicknameStatus;

    public NicknameResponseMessage(String nickname, MessageType messageType, NicknameStatus nicknameStatus) {
        super(nickname, messageType);
        this.nicknameStatus = nicknameStatus;
    }
}
