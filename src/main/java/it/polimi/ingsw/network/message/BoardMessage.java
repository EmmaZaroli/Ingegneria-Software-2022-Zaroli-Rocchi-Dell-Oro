package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.SchoolBoard;

public class BoardMessage extends Message {

    private final SchoolBoard schoolBoard;

    public BoardMessage(String nickname, MessageType messageType, SchoolBoard schoolBoard) {
        super(nickname, messageType);
        this.schoolBoard = schoolBoard;
    }

    public SchoolBoard getSchoolBoard() {
        return this.schoolBoard;
    }
}
