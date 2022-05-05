package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class SchoolBoardMessage extends Message {

    private final SchoolBoard schoolBoard;

    public SchoolBoardMessage(String nickname, MessageType messageType, SchoolBoard schoolBoard) {
        super(nickname, messageType);
        this.schoolBoard = schoolBoard;
    }

    public SchoolBoard getSchoolBoard() {
        return this.schoolBoard;
    }
}
