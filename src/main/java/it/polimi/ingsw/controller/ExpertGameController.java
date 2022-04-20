package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalCharacterException;
import it.polimi.ingsw.controller.exceptions.NoCoinsAvailableException;
import it.polimi.ingsw.controller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.messages.CharacterCardMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.RandomHelper;
import it.polimi.ingsw.utils.RandomHelper;

import java.util.*;

import static it.polimi.ingsw.model.enums.GamePhase.ACTION_MOVE_STUDENTS;

public class ExpertGameController<T> extends GameController<T> {

    public ExpertGameController(ExpertGame game, ExpertTableController tableController) {
        super(game, tableController);
        drawCharactersCards();
    }

    public ExpertGameController(ExpertPlayer[] players) {
        super(players);
        drawCharactersCards();
    }

    @Override
    protected void init(Player[] players) {
        this.game = new ExpertGame(players);
        this.tableController = new ExpertTableController((ExpertTable) (game.getTable()));
        LinkedList<PawnColor> students = new LinkedList<>();
        for (Player c : game.getPlayers()) {
            for (int i = 0; i < game.getParameters().getInitialStudentsCount(); i++) {
                students.add(tableController.getBag().drawStudent());
            }
            c.getBoard().addStudentsToEntrance(students);
        }
    }

    private void drawCharactersCards() {
        int numberCard;
        List<Character> characters = new ArrayList<>(Arrays.asList(Character.values()));
        //TODO parameterize 3
        CharacterCard[] cards = new CharacterCard[3];
        Effect[] effects = new Effect[3];
        for (int i = 0; i < 3; i++) {
            numberCard = RandomHelper.getInstance().getInt(characters.size());
            cards[i] = CharacterCardFactory.getCharacterCard(characters.get(numberCard));
            effects[i] = EffectFactory.getEffect(characters.get(numberCard));
            characters.remove(characters.get(numberCard));
        }
        getGame().addCharacterCards(cards);
        getGame().addEffects(effects);
    }

    @Override
    public void update(T m) {
        Message message = (Message) m;
        if (message.getType().equals(MessageType.CHARACTER_CARD)) {
            CharacterCard card = ((CharacterCardMessage) message).getCharacterCard();
            Pair pair = isCardOnTable(card);
            if (!(boolean) pair.first()) {
                game.throwException(new IllegalCharacterException());
            }
            int index = (int) pair.second();
            canActivateCharacterAbility(index);
            activateCharacterAbility(index);
        } else {
            super.update(m);
        }
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
            getGame().getPlayers()[game.getCurrentPlayer()].addCoin();
        }
        this.checkProfessorsStatus(pawn);
        this.movedPawn();
    }

    /**
     * @param card the card that the player selected
     * @return Pair (true,cardIndex) if the card is on the table, (false,null) otherwise
     */
    private Pair isCardOnTable(CharacterCard card) {
        for (int i = 0; i < getGame().getCharacterCards().length; i++) {
            if (getGame().getCharacterCards()[i].getCharacter().equals(card.getCharacter())) {
                return new Pair(true, i);
            }
        }
        return new Pair(false, null);
    }

    //TODO do something about this function
    public boolean canActivateCharacterAbility(int characterIndex) {
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
        int cardPrice = (getGame()).getCharacterCards()[characterIndex].getCurrentPrice();
        getGame()
                .getPlayers()[game.getCurrentPlayer()]
                .decreaseCoins(cardPrice);
        ((ExpertTableController) tableController).depositCoins(cardPrice);
        getGameParameters().setAlreadyActivateCharacterCard(true);
    }

    private void activateSetupEffect(int effectIndex) {
        ((SetupEffect) getGame().getEffects()[effectIndex])
                .setupEffect(tableController, (CharacterCardWithSetUpAction) getGame().getCharacterCards()[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex) {
        ((StandardEffect) getGame().getEffects()[effectIndex]).activateEffect(getGameParameters());
    }

    private void activateReverseEffect(int effectIndex) {
        ((StandardEffect) getGame().getEffects()[effectIndex]).reverseEffect(getGameParameters());
    }

    private void effect1(CharacterCardWithSetUpAction character, PawnColor color, UUID islandIndex) {
        character.removeStudent(color);
        try {
            tableController.movePawnOnIsland(color, islandIndex);
        } catch (WrongUUIDException e) {
            e.printStackTrace();
        }
        character.addStudent(tableController.drawStudents(1));
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
        character.addStudent(tableController.drawStudents(1));
    }

    //TODO call at the end of the turn
    //activate reverseEffect for all card, should not generate problems
    public void reverseEffect() {
        for (Effect e : getGame().getEffects()) {
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
}
