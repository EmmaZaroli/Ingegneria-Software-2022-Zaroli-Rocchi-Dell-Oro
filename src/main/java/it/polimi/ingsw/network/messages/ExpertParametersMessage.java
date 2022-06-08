package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.ExpertParametersDto;
import it.polimi.ingsw.model.ExpertGameParameters;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class ExpertParametersMessage extends Message {

    private final ExpertParametersDto expertParameters;

    public ExpertParametersMessage(String nickname, ExpertGameParameters parameters) {
        super(nickname, MessageType.UPDATE_EXPERT_PARAMETERS);
        this.expertParameters = new ExpertParametersDto(parameters);
    }

    public ExpertParametersMessage(String nickname, ExpertParametersDto parameters) {
        super(nickname, MessageType.UPDATE_EXPERT_PARAMETERS);
        this.expertParameters = parameters;
    }

    public ExpertParametersDto getExpertParameters() {
        return expertParameters;
    }
}