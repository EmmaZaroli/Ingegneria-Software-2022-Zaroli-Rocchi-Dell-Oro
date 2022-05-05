package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;

public class NicknameResponseMessage extends Message {
    private final NicknameStatus nicknameStatus;

    public NicknameResponseMessage(String nickname, MessageType messageType, NicknameStatus nicknameStatus) {
        super(nickname, messageType);
        this.nicknameStatus = nicknameStatus;
    }

    public NicknameStatus getNicknameStatus() {
        return this.nicknameStatus;
    }
}
