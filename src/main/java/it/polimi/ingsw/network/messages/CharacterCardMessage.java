package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;

public class CharacterCardMessage extends Message {

    private final CharacterCard characterCard;


    public CharacterCardMessage(String nickname, MessageType messageType, CharacterCard characterCard) {
        super(nickname, messageType);
        this.characterCard = characterCard;
    }

    public CharacterCard getCharacterCard() {
        return this.characterCard;
    }
}
