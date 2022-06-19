package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.dtos.CharacterCardDto;
import it.polimi.ingsw.gamecontroller.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.CharacterCardMessage;
import it.polimi.ingsw.persistency.DataDumper;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.RandomHelper;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.model.enums.GamePhase.ACTION_MOVE_STUDENTS;
import static it.polimi.ingsw.model.enums.GamePhase.PLANNING;

public class ExpertGameController extends GameController {
    private Effect[] effects;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public ExpertGameController(ExpertGame game, ExpertTableController tableController, VirtualView[] virtualViews) {
        super(game, tableController, virtualViews);
        drawCharactersCards();
        activateSetupEffect();
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

        //TODO only for test
        cards[0] = CharacterCardFactory.getCharacterCard(Character.CHARACTER_FOUR);
        newEffects[0] = EffectFactory.getEffect(Character.CHARACTER_FOUR);
        getGame().setCharacterCards(cards);
        addEffects(newEffects);
    }

    @Override
    public void onMessageReceived(Message message) {
        if (message.getType().equals(MessageType.ACTION_USE_CHARACTER)) {
            CharacterCardDto card = ((CharacterCardMessage) message).getCharacterCard();
            Optional<Integer> cardIndex = isCardOnTable(card);
            if (cardIndex.isEmpty()) {
                logger.log(Level.WARNING,"Character Card not found");
            }
            int index = cardIndex.get();
            if (canActivateCharacterAbility(index) && areParametersOk((CharacterCardMessage) message, index)) {
                activateCharacterAbility(index, ((CharacterCardMessage) message).getParameters());
            } else {
                logger.log(Level.WARNING,"Incorrect Character Card parameters");
            }
        } else {
            super.onMessageReceived(message);
        }
    }

    private boolean areParametersOk(CharacterCardMessage message, int index) {
        return switch (message.getCharacterCard().getCharacter()) {
            case CHARACTER_ONE -> CharacterCardHelper.areParametersOkCharacter1(message.getCharacterCard(), getGame().getCharacterCards()[index], message.getParameters(), getGame().getTable().getIslands());
            case CHARACTER_SEVEN -> CharacterCardHelper.areParametersOkCharacter7(message.getCharacterCard(), getGame().getCharacterCards()[index], message.getParameters(), getGame().getPlayers()[getGame().getCurrentPlayer()]);
            case CHARACTER_NINE -> CharacterCardHelper.areParametersOkCharacter9(message.getParameters());
            case CHARACTER_ELEVEN -> CharacterCardHelper.areParametersOkCharacter11(message.getCharacterCard(), getGame().getCharacterCards()[index], message.getParameters());
            default -> true;
        };
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
     * @return Optional.of(i) if the card is on the table, Optional.empty() otherwise
     */
    private Optional<Integer> isCardOnTable(CharacterCard card) {
        for (int i = 0; i < getGame().getCharacterCards().length; i++) {
            if (getGame().getCharacterCards()[i].getCharacter().equals(card.getCharacter())) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> isCardOnTable(CharacterCardDto card) {
        for (int i = 0; i < getGame().getCharacterCards().length; i++) {
            if (getGame().getCharacterCards()[i].getCharacter().equals(card.getCharacter())) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public boolean canActivateCharacterAbility(int characterIndex) {
        if (getGameParameters().hasAlreadyActivateCharacterCard())
            return false;
        return getGame().getPlayers()[game.getCurrentPlayer()].getCoins() >=
                getGame().getCharacterCards()[characterIndex].getCurrentPrice();
    }

    public void activateCharacterAbility(int characterIndex, Object[] parameters) {
        if (getGame().getCharacterCards()[characterIndex] instanceof CharacterCardWithSetUpAction)
            switch (getGame().getCharacterCards()[characterIndex].getCharacter()) {
                case CHARACTER_ONE -> effect1(getGame(), (CharacterCardWithSetUpAction) getGame().getCharacterCards()[characterIndex], (PawnColor) parameters[0], (UUID) parameters[1]);
                case CHARACTER_SEVEN -> effect7(getGame(), (CharacterCardWithSetUpAction) getGame().getCharacterCards()[characterIndex], (List<PawnColor>) parameters[0], (List<PawnColor>) parameters[1]);
                case CHARACTER_ELEVEN -> effect11(getGame(), (CharacterCardWithSetUpAction) getGame().getCharacterCards()[characterIndex], (PawnColor) parameters[0]);
            }
        else {
            activateStandardEffect(characterIndex, parameters);
        }
        int cardPrice = (getGame()).getCharacterCards()[characterIndex].getCurrentPrice();
        getGame().decreaseCoins(getGame().getPlayers()[game.getCurrentPlayer()], cardPrice);
        if(getGame().getCharacterCards()[characterIndex].hasCoin())
            ((ExpertTableController) tableController).depositCoins(cardPrice);
        else
            ((ExpertTableController) tableController).depositCoins(cardPrice - 1);
        getGameParameters().setAlreadyActivateCharacterCard(true);
        ((ExpertGame) game).useCharacterCard(getGame().getCharacterCards()[characterIndex]);
    }

    private void activateStandardEffect(int effectIndex, Object[] parameters) {
        if (getGame().getCharacterCards()[effectIndex].getCharacter() == Character.CHARACTER_NINE)
            ((StandardEffect) getEffects()[effectIndex]).activateEffect(getGameParameters(), (PawnColor) parameters[0]);
        else
            ((StandardEffect) getEffects()[effectIndex]).activateEffect(getGameParameters());
    }

    private void effect1(ExpertGame game, CharacterCardWithSetUpAction character, PawnColor color, UUID islandIndex) {
        game.removeStudent(character, color);
        try {
            tableController.movePawnOnIsland(color, islandIndex);
        } catch (WrongUUIDException e) {
            game.throwException(e);
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

    public void activateSetupEffect() {
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
        this.game.setGamePhase(this.pickNextPhase());
        if (this.game.getGamePhase() == GamePhase.ACTION_END) {
            do{
                this.game.setPlayedCount(game.getPlayedCount() + 1);
                reverseEffect();
                getGameParameters().setAlreadyActivateCharacterCard(false);
                //TODO is there a way to put the next line in the model?
                getGame().notifyExpertParameters(getGameParameters());
                if (!super.isRoundComplete()) {
                    this.game.setGamePhase(ACTION_MOVE_STUDENTS);
                } else {
                    endOfRound();
                }
                super.changePlayer();
            }
            while (!game.getPlayer(game.getCurrentPlayer()).isOnline());
            DataDumper.getInstance().saveGame(game);
        }
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
                player.getBoard().isThereProfessor(color)) {
            if (getGameParameters().isTakeProfessorEvenIfSameStudents()) {
                if (game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(color)
                        >= player.getBoard().getStudentsInDiningRoom(color)) {
                    player.getBoard().removeProfessor(color);
                    game.getCurrentPlayerSchoolBoard().addProfessor(color);
                }
            } else {
                if (game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(color)
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
