package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PlayerCountIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExpertGameController extends GameController {
    private CharacterCard[] characterCards;
    private CharacterCardFactory cardFactory = new CharacterCardFactory();
    private EffectFactory effectFactory = new EffectFactory();
    private Effect[] effects;

    public ExpertGameController(Player[] players) {
        //TODO how can i create an ExpertTableController and ExpertPlayer
        super(players);
        this.table = players.length == 3 ? new ExpertTableController(PlayerCountIcon.THREE) : new ExpertTableController(PlayerCountIcon.TWO_FOUR);
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
            effects[i] = effectFactory.getEffect(Characters.get(numberCard));
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

    public boolean canActivateCharacterAbility(int characterIndex) {
        //TODO throw exception if the card doesn't exist on the table
        return ((ExpertPlayer)getCurrentPlayer()).getCoins() > characterCards[characterIndex].getCurrentPrice();
    }

    public void activateCharacterAbility(int characterIndex) {
        if(characterCards[characterIndex] instanceof CharacterCardWithSetUpAction)
            activateSetupEffect(characterIndex);
        else
            activateStandardEffect(characterIndex);
    }

    private void activateSetupEffect(int effectIndex){
        ((SetupEffect)effects[effectIndex]).activateEffect(table, (CharacterCardWithSetUpAction) characterCards[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex){
        ((StandardEffect)effects[effectIndex]).activateEffect((ExpertGameParameters) getGameParameters());
    }

    private void activateReverseEffect(int effectIndex){
        ((StandardEffect)effects[effectIndex]).reverseEffect((ExpertGameParameters) getGameParameters());
    }
}
