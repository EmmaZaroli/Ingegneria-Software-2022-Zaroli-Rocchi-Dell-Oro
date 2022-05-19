package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.CharacterCardMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.RandomHelper;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

import static it.polimi.ingsw.model.enums.GamePhase.ACTION_MOVE_STUDENTS;
import static it.polimi.ingsw.model.enums.GamePhase.PLANNING;

public class ExpertGameController extends GameController {
    private Effect[] effects;

    public ExpertGameController(ExpertGame game, ExpertTableController tableController, VirtualView[] virtualViews) {
        super(game, tableController, virtualViews);
        drawCharactersCards();
        activateSetupEffect();
    }

    @Override
    protected void init(Player[] players) {
        //TODO what is this for?
        /*
        LinkedList<PawnColor> students = new LinkedList<>();
        for (Player c : game.getPlayers()) {
            for (int i = 0; i < game.getParameters().getInitialStudentsCount(); i++) {
                students.add(tableController.getBag().drawStudent());
            }
            c.getBoard().addStudentsToEntrance(students);
        }*/

        fillClouds();
        for (Player player : game.getPlayers()) {
            player.getBoard().addStudentsToEntrance(tableController.drawStudents());
        }
        this.game.setCurrentPlayer(0);
        this.game.setGamePhase(PLANNING);
    }

    public Effect[] getEffects() {
        return effects.clone();
    }

    //TODO only for test
    public void setEffects(Effect[] effects) {
        this.effects = effects;
    }

    public void addEffects(Effect[] effects) {
        this.effects = effects;
    }

    private void drawCharactersCards() {
        int numberCard;
        List<Character> characters = new ArrayList<>(Arrays.asList(Character.values()));
        CharacterCard[] cards = new CharacterCard[3];
        Effect[] newEffects = new Effect[3];
        for (int i = 0; i < 3; i++) {
            numberCard = RandomHelper.getInstance().getInt(characters.size());
            cards[i] = CharacterCardFactory.getCharacterCard(characters.get(numberCard));
            newEffects[i] = EffectFactory.getEffect(characters.get(numberCard));
            characters.remove(characters.get(numberCard));
        }
        getGame().setCharacterCards(cards);
        addEffects(newEffects);
    }

    @Override
    public void update(Message message) {
        if (message.getType().equals(MessageType.CHARACTER_CARD)) {
            CharacterCard card = ((CharacterCardMessage) message).getCharacterCard();
            Pair<Boolean, Integer> pair = isCardOnTable(card);
            if (!(boolean) pair.first()) {
                game.throwException(new IllegalCharacterException());
            }
            int index = pair.second();
            if (canActivateCharacterAbility(index) && areParametersOk((CharacterCardMessage) message)) {
                activateCharacterAbility(index, ((CharacterCardMessage)message).getParameters());
            } else {
                //TODO reply with error
            }
        } else {
            super.update(message);
        }
    }

    private boolean areParametersOk(CharacterCardMessage message){
        return switch (message.getCharacterCard().getCharacter()){
            case CHARACTER_ONE -> areParametersOkCharacter1(message.getCharacterCard(), message.getParameters());
            case CHARACTER_SEVEN -> areParametersOkCharacter7(message.getCharacterCard(), message.getParameters());
            case CHARACTER_NINE -> areParametersOkCharacter9(message.getParameters());
            case CHARACTER_ELEVEN -> areParametersOkCharacter11(message.getCharacterCard(), message.getParameters());
            default -> true;
        };
    }

    private boolean areParametersOkCharacter1(CharacterCard card, Object[] parameters){
        if(parameters.length != 2)
            return false;
        if(!(parameters[0] instanceof PawnColor && parameters[1] instanceof UUID))
            return false;
        if(!(card instanceof CharacterCardWithSetUpAction cardWithSetUpAction))
            return false;
        if(!(cardWithSetUpAction.getStudents().contains(parameters[0])))
            return false;
        for(IslandCard island : getGame().getTable().getIslands()){
            if(island.getUuid().equals(parameters[1]))
                return true;
        }
        return false;
    }

    private boolean areParametersOkCharacter7(CharacterCard card, Object[] parameters){
        if(parameters.length != 2)
            return false;
        if(!(parameters[0] instanceof List<?> && parameters[1] instanceof List<?>))
            return false;
        if(!(card instanceof CharacterCardWithSetUpAction cardWithSetUpAction))
            return false;
        List<PawnColor> colorsFromCard = (List<PawnColor>) parameters[0];
        List<PawnColor> colorsFromEntrance = (List<PawnColor>) parameters[1];
        Map<PawnColor, Integer> cardinalityCard = ((CharacterCardWithSetUpAction) cardWithSetUpAction).getStudentsCardinality();
        Map<PawnColor, Integer> cardinalityEntrance = getGame().getPlayers()[getGame().getCurrentPlayer()].getBoard().getStudentsInEntranceCardinality();
        for(PawnColor color : PawnColor.values()){
            if(colorsFromCard.stream().filter(x -> x==color).count() > cardinalityCard.get(color))
                return false;
        }
        for(PawnColor color : PawnColor.values()){
            if(colorsFromEntrance.stream().filter(x -> x==color).count() > cardinalityEntrance.get(color))
                return false;
        }
        return true;
    }

    private boolean areParametersOkCharacter9(Object[] parameters){
        return (parameters.length == 1) && (parameters[0] instanceof PawnColor);
    }

    private boolean areParametersOkCharacter11(CharacterCard card, Object[] parameters){
        if(parameters.length != 1)
            return false;
        if(!(parameters[0] instanceof PawnColor))
            return false;
        if(!(card instanceof CharacterCardWithSetUpAction cardWithSetUpAction))
            return false;
        return cardWithSetUpAction.getStudents().contains(parameters[0]);
    }


    @Override
    public void moveStudentToDiningRoom(PawnColor pawn) throws IllegalActionException {
        if (this.game.getGamePhase() != ACTION_MOVE_STUDENTS) {
            throw new IllegalActionException();
        }
        if (this.game.getCurrentPlayerBoard().moveStudentFromEntranceToDiningRoom(pawn)) {
            try {
                ((ExpertTableController) tableController).takeCoin();
            } catch (NoCoinsAvailableException e) {
                game.throwException(e);
            }
            getGame().addCoin(getGame().getPlayers()[game.getCurrentPlayer()]);
        }
        this.checkProfessorsStatus(pawn);
        this.movedPawn();
    }

    /**
     * @param card the card that the player selected
     * @return Pair (true,cardIndex) if the card is on the table, (false,null) otherwise
     */
    private Pair<Boolean, Integer> isCardOnTable(CharacterCard card) {
        for (int i = 0; i < getGame().getCharacterCards().length; i++) {
            if (getGame().getCharacterCards()[i].getCharacter().equals(card.getCharacter())) {
                return new Pair<>(true, i);
            }
        }
        return new Pair<>(false, null);
    }

    public boolean canActivateCharacterAbility(int characterIndex) {
        if (getGameParameters().hasAlreadyActivateCharacterCard())
            return false;
        return getGame().getPlayers()[game.getCurrentPlayer()].getCoins() >=
                getGame().getCharacterCards()[characterIndex].getCurrentPrice();
    }

    public void activateCharacterAbility(int characterIndex, Object[] parameters) {
        if (getGame().getCharacterCards()[characterIndex] instanceof CharacterCardWithSetUpAction)
            switch (getGame().getCharacterCards()[characterIndex].getCharacter()){
                case CHARACTER_ONE -> effect1(getGame(), (CharacterCardWithSetUpAction) getGame().getCharacterCards()[characterIndex], (PawnColor) parameters[0], (UUID) parameters[1]);
                case CHARACTER_SEVEN -> effect7(getGame(), (CharacterCardWithSetUpAction) getGame().getCharacterCards()[characterIndex], (List<PawnColor>)parameters[0], (List<PawnColor>)parameters[1]);
                case CHARACTER_ELEVEN -> effect11(getGame(), (CharacterCardWithSetUpAction) getGame().getCharacterCards()[characterIndex], (PawnColor) parameters[0]);
            }
        else {
            activateStandardEffect(characterIndex, parameters);
        }
        int cardPrice = (getGame()).getCharacterCards()[characterIndex].getCurrentPrice();
        getGame().decreaseCoins(getGame().getPlayers()[game.getCurrentPlayer()], cardPrice);
        ((ExpertTableController) tableController).depositCoins(cardPrice);
        getGameParameters().setAlreadyActivateCharacterCard(true);
    }

    private void activateSetupEffect(int effectIndex) {
        ((SetupEffect) getEffects()[effectIndex])
                .setupEffect((ExpertGame) game, tableController, (CharacterCardWithSetUpAction) getGame().getCharacterCards()[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex, Object[] parameters) {
        if(getGame().getCharacterCards()[effectIndex].getCharacter() == Character.CHARACTER_NINE)
            ((StandardEffect) getEffects()[effectIndex]).activateEffect(getGameParameters(), (PawnColor)parameters[0]);
        else
            ((StandardEffect) getEffects()[effectIndex]).activateEffect(getGameParameters());
    }

    private void activateReverseEffect(int effectIndex) {
        ((StandardEffect) getEffects()[effectIndex]).reverseEffect(getGameParameters());
    }

    private void effect1(ExpertGame game, CharacterCardWithSetUpAction character, PawnColor color, UUID islandIndex) {
        game.removeStudent(character, color);
        try {
            tableController.movePawnOnIsland(color, islandIndex);
        } catch (WrongUUIDException e) {
            game.setError(e);
        }
        game.addStudent(character, tableController.drawStudents(1).get(0));
    }

    private void effect7(ExpertGame game, CharacterCardWithSetUpAction character, List<PawnColor> colorsFromCard, List<PawnColor> colorsFromEntrance) {
        for (PawnColor c : colorsFromCard) {
            game.removeStudent(character, c);
        }
        for (PawnColor c : colorsFromEntrance) {
            game.getPlayers()[game.getCurrentPlayer()].getBoard().removeStudentFromEntrance(c);
            game.addStudent(character, c);
        }

        game.getPlayers()[game.getCurrentPlayer()].getBoard().addStudentsToEntrance(colorsFromCard);
    }

    private void effect11(ExpertGame game, CharacterCardWithSetUpAction character, PawnColor color) {
        game.removeStudent(character, color);
        game.getPlayers()[game.getCurrentPlayer()].getBoard().addStudentToDiningRoom(color);
        game.addStudent(character, tableController.drawStudents(1).get(0));
    }

    public void activateSetupEffect(){
        for (int i = 0; i < effects.length; i++) {
            if (effects[i] instanceof SetupEffect effect)
                effect.setupEffect(getGame(), tableController, (CharacterCardWithSetUpAction) getGame().getCharacterCards()[i]);
        }
    }

    //activate reverseEffect for all card, should not generate problems
    public void reverseEffect() {
        for (Effect e : getEffects()) {
            if (e instanceof StandardEffect effect)
                effect.reverseEffect(getGameParameters());
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

    @Override
    public void tryStealProfessor(PawnColor color, Player player) {
        if (!game.getCurrentPlayerSchoolBoard().isThereProfessor(color) &&
                player.getBoard().isThereProfessor(color)){
            if(getGameParameters().isTakeProfessorEvenIfSameStudents()){
                if(game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(color)
                        >= player.getBoard().getStudentsInDiningRoom(color)) {
                    player.getBoard().removeProfessor(color);
                    game.getCurrentPlayerSchoolBoard().addProfessor(color);
                }
            }
            else{
                if(game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(color)
                        > player.getBoard().getStudentsInDiningRoom(color)) {
                    player.getBoard().removeProfessor(color);
                    game.getCurrentPlayerSchoolBoard().addProfessor(color);
                }
            }
        }
    }

    @Override
    public void moveMotherNature(int steps) throws NotAllowedMotherNatureMovementException, IllegalActionException {
        if (this.game.getGamePhase() != GamePhase.ACTION_MOVE_MOTHER_NATURE) {
            throw new IllegalActionException();
        }
        if (steps < 1 || steps > this.game.getPlayers()[game.getCurrentPlayer()].getDiscardPileHead().motherNatureMovement() + getGameParameters().getMotherNatureExtraMovements()) {
            throw new NotAllowedMotherNatureMovementException();
        }
        this.tableController.moveMotherNature(steps);

        this.checkInfluence();

        this.playerHasEndedAction();
    }
}
