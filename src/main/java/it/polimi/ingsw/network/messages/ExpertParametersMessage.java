package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.dtos.ExpertParametersDto;
import it.polimi.ingsw.model.ExpertGameParameters;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 * Message which contains the updated expertParameters
 */
public class ExpertParametersMessage extends Message {

    private final ExpertParametersDto expertParameters;

    public ExpertParametersMessage(String nickname, ExpertGameParameters parameters) {
        super(nickname, MessageType.UPDATE_EXPERT_PARAMETERS);
        this.expertParameters = new ExpertParametersDto(parameters);
    }

    /**
     *
     * @return the ExpertGameParameters
     */
    public ExpertParametersDto getExpertParameters() {
        return expertParameters;
    }
}