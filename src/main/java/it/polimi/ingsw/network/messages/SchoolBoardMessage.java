package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message which contains the schoolBoard. Used to send the updated Board to the client.
 */
public class SchoolBoardMessage extends Message {

    private final SchoolBoardDto schoolBoard;

    public SchoolBoardMessage(String nickname, MessageType messageType, SchoolBoard schoolBoard) {
        super(nickname, messageType);
        this.schoolBoard = new SchoolBoardDto(schoolBoard);
    }

    public SchoolBoardDto getSchoolBoard() {
        return this.schoolBoard;
    }
}
