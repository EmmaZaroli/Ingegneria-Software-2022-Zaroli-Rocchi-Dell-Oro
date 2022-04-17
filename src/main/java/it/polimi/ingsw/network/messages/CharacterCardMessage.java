package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.CharacterCard;

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
