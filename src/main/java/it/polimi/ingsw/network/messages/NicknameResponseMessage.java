package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;

/**
 * Message used by the server to confirm or discard a NicknameProposalMessage
 */
public class NicknameResponseMessage extends Message {
    private final NicknameStatus nicknameStatus;

    public NicknameResponseMessage(String nickname, NicknameStatus nicknameStatus) {
        super(nickname, MessageType.NICKNAME_RESPONSE);
        this.nicknameStatus = nicknameStatus;
    }

    /**
     *
     * @return the NicknameStatus
     */
    public NicknameStatus getNicknameStatus() {
        return this.nicknameStatus;
    }
}
