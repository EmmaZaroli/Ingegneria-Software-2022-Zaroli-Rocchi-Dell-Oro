package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.SchoolBoard;

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
