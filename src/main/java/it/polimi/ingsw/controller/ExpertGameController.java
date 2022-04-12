package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.*;

public class ExpertGameController extends GameController {
    private CharacterCardFactory cardFactory = new CharacterCardFactory();
    private EffectFactory effectFactory = new EffectFactory();

    public ExpertGameController(ExpertPlayer[] players) {
        super(players);
        drawCharactersCards();
    }

    @Override
    protected void init(Player[] players) {
        this.game = new ExpertGame(players);
        this.tableController = new ExpertTableController((ExpertTable)(game.getTable()));
        LinkedList<PawnColor> students = new LinkedList<>();
        for (Player c : game.getPlayers()) {
            for(int i = 0; i < game.getParameters().getInitialStudentsCount(); i++) {
                students.add(tableController.getBag().drawStudent());
            }
            c.getBoard().addStudentsToEntrance(students);
        }
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
        return getGame().getPlayers()[game.getCurrentPlayer()].getCoins() >
                getGame().getCharacterCards()[characterIndex].getCurrentPrice();
    }

    public void activateCharacterAbility(int characterIndex) {
        if(getGame().getCharacterCards()[characterIndex] instanceof CharacterCardWithSetUpAction)
            activateSetupEffect(characterIndex);
        else
            activateStandardEffect(characterIndex);
        getGame()
                .getPlayers()[game.getCurrentPlayer()]
                .decreaseCoins((getGame()).getCharacterCards()[characterIndex].getCurrentPrice());
        getGameParameters().setAlreadyActivateCharacterCard(true);
    }

    private void activateSetupEffect(int effectIndex){
        ((SetupEffect) getGame().getEffects()[effectIndex])
                .setupEffect(tableController, (CharacterCardWithSetUpAction) getGame().getCharacterCards()[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex){
        ((StandardEffect)getGame().getEffects()[effectIndex]).activateEffect(getGameParameters());
    }

    private void activateReverseEffect(int effectIndex){
        ((StandardEffect)getGame().getEffects()[effectIndex]).reverseEffect(getGameParameters());
    }

    private void effect1(CharacterCardWithSetUpAction character, PawnColor color, int islandIndex){
        character.removeStudent(color);
        tableController.movePawnOnIsland(color, islandIndex);
        character.addStudent(tableController.drawStudents(1));
    }

    private void effect7(CharacterCardWithSetUpAction character, List<PawnColor> colorsFromCard, List<PawnColor> colorsFromEntrance){
        for(PawnColor c : colorsFromCard){
            character.removeStudent(c);
        }
        for(PawnColor c : colorsFromEntrance){
            game.getPlayers()[game.getCurrentPlayer()].getBoard().removeStudentFromEntrance(c);
        }

        character.addStudent(colorsFromEntrance);
        game.getPlayers()[game.getCurrentPlayer()].getBoard().addStudentsToEntrance(colorsFromCard);
    }

    private void effect11(CharacterCardWithSetUpAction character, PawnColor color){
        character.removeStudent(color);
        game.getPlayers()[game.getCurrentPlayer()].getBoard().addStudentToDiningRoom(color);
        character.addStudent(tableController.drawStudents(1));
    }

    //TODO call at the end of the turn
    //activate reverseEffect for all card, should not generate problems
    public void reverseEffect(){
        for(Effect e : ((ExpertGame)game).getEffects()){
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
        return (ExpertGameParameters) super.game.getParameters();
    }

    public ExpertGame getGame() {
        return (ExpertGame) this.game;
    }
}
