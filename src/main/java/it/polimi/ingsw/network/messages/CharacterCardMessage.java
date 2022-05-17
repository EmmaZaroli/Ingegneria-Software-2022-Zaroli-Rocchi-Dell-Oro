package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class CharacterCardMessage extends Message {

    private final CharacterCard characterCard;
    private final Object[] parameters;


    public CharacterCardMessage(String nickname, MessageType messageType, CharacterCard characterCard, Object[] parameters) {
        super(nickname, messageType);
        this.characterCard = characterCard;
        this.parameters = parameters;
    }

    public CharacterCard getCharacterCard() {
        return this.characterCard;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
