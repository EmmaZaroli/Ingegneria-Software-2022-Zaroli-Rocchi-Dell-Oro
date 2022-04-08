package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExpertGameController extends GameController {
    private CharacterCardFactory cardFactory = new CharacterCardFactory();
    private EffectFactory effectFactory = new EffectFactory();
    private TableController table;

    public ExpertGameController(ExpertPlayer[] players) {
        super(players);
        drawCharactersCards();
    }

    @Override
    protected void init(Player[] players) {
        this.game = new ExpertGame(players);
        this.table = new ExpertTableController(getGame().getTable());
    }

    private void drawCharactersCards() {
        int numberCard;
        List<Character> Characters = new ArrayList<Character>();
        Random r = new Random();
        Characters.addAll((Arrays.stream(Character.values()).toList()));
        for (int i = 0; i < 3; i++) {
            numberCard = r.nextInt(Characters.size());
            getGame().getCharacterCards()[i] = cardFactory.getCharacterCard(Characters.get(numberCard));
            getGame().getEffects()[i] = effectFactory.getEffect(Characters.get(numberCard));
            Characters.remove(Characters.get(numberCard));
        }
    }

    @Override
    public void update(Message message) {
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
        if (getGameParameters().hasAlreadyActivateCharacterCard())
            return false;
        return getGame().getPlayers()[game.getCurrentPlayer()].getCoins() >
                getGame().getCharacterCards()[characterIndex].getCurrentPrice();
    }

    public void activateCharacterAbility(int characterIndex) {
        if (getGame().getCharacterCards()[characterIndex] instanceof CharacterCardWithSetUpAction)
            activateSetupEffect(characterIndex);
        else
            activateStandardEffect(characterIndex);
        getGame()
                .getPlayers()[game.getCurrentPlayer()]
                .decreaseCoins((getGame()).getCharacterCards()[characterIndex].getCurrentPrice());
        getGameParameters().setAlreadyActivateCharacterCard(true);
    }

    private void activateSetupEffect(int effectIndex) {
        ((SetupEffect) getGame().getEffects()[effectIndex])
                .setupEffect(table, (CharacterCardWithSetUpAction) getGame().getCharacterCards()[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex) {
        ((StandardEffect) getGame().getEffects()[effectIndex]).activateEffect(getGameParameters());
    }

    private void activateReverseEffect(int effectIndex) {
        ((StandardEffect) getGame().getEffects()[effectIndex]).reverseEffect(getGameParameters());
    }

    private void effect1(CharacterCardWithSetUpAction character, PawnColor color, int islandIndex) {
        character.removeStudent(color);
        table.movePawnOnIsland(color, islandIndex);
        character.addStudent(table.drawStudents(1));
    }

    private void effect7(CharacterCardWithSetUpAction character, List<PawnColor> colorsFromCard, List<PawnColor> colorsFromEntrance) {
        for (PawnColor c : colorsFromCard) {
            character.removeStudent(c);
        }
        for (PawnColor c : colorsFromEntrance) {
            game.getPlayers()[game.getCurrentPlayer()].getBoard().removeStudentFromEntrance(c);
        }

        character.addStudent(colorsFromEntrance);
        game.getPlayers()[game.getCurrentPlayer()].getBoard().addStudentsToEntrance(colorsFromCard);
    }

    private void effect11(CharacterCardWithSetUpAction character, PawnColor color) {
        character.removeStudent(color);
        game.getPlayers()[game.getCurrentPlayer()].getBoard().addStudentToDiningRoom(color);
        character.addStudent(table.drawStudents(1));
    }

    //TODO call at the end of the turn
    //activate reverseEffect for all card, should not generate problems
    public void reverseEffect() {
        for (Effect e : ((ExpertGame) game).getEffects()) {
            if (e instanceof StandardEffect)
                ((StandardEffect) e).reverseEffect(getGameParameters());
        }
    }

    @Override
    protected void playerHasEndedAction() {
        reverseEffect();
        getGameParameters().setAlreadyActivateCharacterCard(false);
        super.playerHasEndedAction();
    }

    public ExpertGameParameters getGameParameters() {
        return (ExpertGameParameters) super.game.getParameters();
    }

    public ExpertGame getGame() {
        return (ExpertGame) this.game;
    }
}
