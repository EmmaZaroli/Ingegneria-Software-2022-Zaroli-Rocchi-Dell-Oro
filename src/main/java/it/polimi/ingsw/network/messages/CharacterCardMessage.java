package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

/**
 *  Message which contains the characterCard and its parameters
 */
public class CharacterCardMessage extends Message {

    private final CharacterCardDto characterCard;
    private final Object[] parameters;

    public CharacterCardMessage(String nickname, MessageType messageType, CharacterCard characterCard, Object[] parameters) {
        super(nickname, messageType);
        this.characterCard = new CharacterCardDto(characterCard);
        this.parameters = parameters;
    }

    public CharacterCardMessage(String nickname, MessageType messageType, ViewCharacterCard characterCard, Object[] parameters) {
        super(nickname, messageType);
        this.characterCard = new CharacterCardDto(characterCard);
        this.parameters = parameters;
    }

    /**
     *
     * @return the CharacterCardDto
     */
    public CharacterCardDto getCharacterCard() {
        return this.characterCard;
    }

    /**
     *
     * @return the Parameters
     */
    public Object[] getParameters() {
        return parameters;
    }
}
