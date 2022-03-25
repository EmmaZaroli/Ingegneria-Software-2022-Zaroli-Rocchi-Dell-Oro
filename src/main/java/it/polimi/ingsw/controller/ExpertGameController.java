package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.utils.RandomHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpertGameController extends GameController {

    private List<CharacterCard> characterCards;

    public ExpertGameController(Player[] players) {
        //TODO how can i create an ExpertTableController and ExpertPlayer
        super(players);
        characterCards = new ArrayList<>(drawCharactersCards());
    }

    public List<CharacterCard> drawCharactersCards() {
        int n = 8;
        List<CharacterCard> characters = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            int numberCard = r.nextInt(n);
            //call Factory with parameter Character.values(numberCard)
        }
        return characters;
    }

    @Override
    public void MessageReceiver(/*Message*/) {
        /*
         if (!Message.type().equals("EffectCard")) super(Message)
        else{
            canActivateCharacterAbility(Message.character());
        }
         */
    }

    public void canActivateCharacterAbility(Character character) {
        //TODO throw exception if the card doesn't exist on the table
        //if (players[currentPlayer].getCoins()>character.getCurrentPrice())
        activateCharacterAbility(character);
    }

    public void activateCharacterAbility(Character character) {

    }
}
