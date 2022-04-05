package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
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
        if (Message.type().equals("EffectCard")) {
            canActivateCharacterAbility(Message.character());
            activateCharacterAbility(Message.character());
        }
        else
            super(Message)

         */
    }

    //TODO do something aboiut this function
    public boolean canActivateCharacterAbility(int characterIndex) {
        //TODO throw exception if the card doesn't exist on the table
        if(getGameParameters().hasAlreadyActivateCharacterCard())
            return false;
        return ((ExpertPlayer)getCurrentPlayer()).getCoins() > characterCards[characterIndex].getCurrentPrice();
    }

    public void activateCharacterAbility(int characterIndex) {
        if(characterCards[characterIndex] instanceof CharacterCardWithSetUpAction)
            activateSetupEffect(characterIndex);
        else
            activateStandardEffect(characterIndex);
        ((ExpertPlayer)getCurrentPlayer()).decreaseCoins(characterCards[characterIndex].getCurrentPrice());
        getGameParameters().setAlreadyActivateCharacterCard(true);
    }

    private void activateSetupEffect(int effectIndex){
        ((SetupEffect)effects[effectIndex]).setupEffect(table, (CharacterCardWithSetUpAction) characterCards[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex){
        ((StandardEffect)effects[effectIndex]).activateEffect(getGameParameters());
    }

    private void activateReverseEffect(int effectIndex){
        ((StandardEffect)effects[effectIndex]).reverseEffect(getGameParameters());
    }

    private void effect1(CharacterCardWithSetUpAction character, PawnColor color, int islandIndex){
        character.removeStudent(color);
        table.movePawnOnIsland(color, islandIndex);
        character.addStudent(table.drawStudents(1));
    }

    private void effect7(CharacterCardWithSetUpAction character, List<PawnColor> colorsFromCard, List<PawnColor> colorsFromEntrance){
        for(PawnColor c : colorsFromCard){
            character.removeStudent(c);
        }
        for(PawnColor c : colorsFromEntrance){
            currentPlayerBoard.removeStudentFromEntrance(c);
        }

        character.addStudent(colorsFromEntrance);
        currentPlayerBoard.addStudentsToEntrance(colorsFromCard);
    }

    private void effect11(CharacterCardWithSetUpAction character, PawnColor color){
        character.removeStudent(color);
        currentPlayerBoard.addStudentToDiningRoom(color);
        character.addStudent(table.drawStudents(1));
    }

    //TODO call at the end of the turn
    //activate reverseEffect for all card, should not generate problems
    public void reverseEffect(){
        for(Effect e : effects){
            if(e instanceof StandardEffect)
                ((StandardEffect) e).reverseEffect(getGameParameters());
        }
    }

    @Override
    protected void playerHasEndedAction(){
        reverseEffect();
        getGameParameters().setAlreadyActivateCharacterCard(false);
        super.playerHasEndedAction();
    }

    public ExpertGameParameters getGameParameters(){
        return (ExpertGameParameters) super.getGameParameters();
    }
}
