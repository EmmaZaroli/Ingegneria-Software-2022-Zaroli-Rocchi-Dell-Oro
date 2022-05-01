package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.exceptions.IllegalActionException;
import it.polimi.ingsw.gamecontroller.exceptions.IllegalCharacterException;
import it.polimi.ingsw.gamecontroller.exceptions.NoCoinsAvailableException;
import it.polimi.ingsw.gamecontroller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.messages.CharacterCardMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.RandomHelper;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

import static it.polimi.ingsw.model.enums.GamePhase.ACTION_MOVE_STUDENTS;

public class ExpertGameController extends GameController {
    private Effect[] effects;

    public ExpertGameController(ExpertGame game, ExpertTableController tableController, VirtualView[] virtualViews) {
        super(game, tableController, virtualViews);
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

    public Effect[] getEffects() {
        return effects.clone();
    }

    public void addEffects(Effect[] effects) {
        this.effects = effects;
    }

    private void drawCharactersCards() {
        int numberCard;
        List<Character> characters = new ArrayList<>(Arrays.asList(Character.values()));
        CharacterCard[] cards = new CharacterCard[3];
        Effect[] effects = new Effect[3];
        for (int i = 0; i < 3; i++) {
            numberCard = RandomHelper.getInstance().getInt(characters.size());
            cards[i] = CharacterCardFactory.getCharacterCard(characters.get(numberCard));
            effects[i] = EffectFactory.getEffect(characters.get(numberCard));
            characters.remove(characters.get(numberCard));
        }
        getGame().addCharacterCards(cards);
        addEffects(effects);
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
            canActivateCharacterAbility(index);
            activateCharacterAbility(index);
        } else {
            super.update(message);
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
        getGame().decreaseCoins(getGame().getPlayers()[game.getCurrentPlayer()], cardPrice);
        ((ExpertTableController) tableController).depositCoins(cardPrice);
        getGameParameters().setAlreadyActivateCharacterCard(true);
    }

    private void activateSetupEffect(int effectIndex) {
        ((SetupEffect) getEffects()[effectIndex])
                .setupEffect((ExpertGame) game, tableController, (CharacterCardWithSetUpAction) getGame().getCharacterCards()[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex) {
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
            e.printStackTrace();
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
}
