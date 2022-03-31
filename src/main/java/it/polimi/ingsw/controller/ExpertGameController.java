package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CharacterCardFactory;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.RandomHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExpertGameController extends GameController {

    private CharacterCard[] characterCards;
    private CharacterCardFactory cardFactory = new CharacterCardFactory();
    private Effect[] effects;

    public ExpertGameController(Player[] players) {
        //TODO how can i create an ExpertTableController and ExpertPlayer
        super(players);
        drawCharactersCards();
    }

    private void drawCharactersCards() {
        int numberCard;
        List<Character> Characters = new ArrayList<Character>();
        Random r = new Random();
        Characters.addAll((Arrays.stream(Character.values()).toList()));
        for (int i = 0; i < 3; i++) {
            numberCard = r.nextInt(Characters.size());
            characterCards[i] = cardFactory.getCharacterCard(Characters.get(numberCard));
            Characters.remove(Characters.get(numberCard));
        }
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

    public void canActivateCharacterAbility(CharacterCard character) {
        //TODO throw exception if the card doesn't exist on the table
        //if (getPlayers()[getCurrentPlayer()].getCoins()>character.getCurrentPrice())
        activateCharacterAbility(character);
    }

    public void activateCharacterAbility(CharacterCard character) {
    }
}
