package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.WrongUUIDException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.messages.CharacterCardMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.utils.RandomHelper;

import java.util.*;

public class ExpertGameController<T> extends GameController<T> {

    private CharacterCard[] characterCards;
    private Effect[] effects;

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
        for (int i = 0; i < 3; i++) {
            numberCard = RandomHelper.getInstance().getInt(characters.size());
            characterCards[i] = CharacterCardFactory.getCharacterCard(characters.get(numberCard));
            effects[i] = EffectFactory.getEffect(characters.get(numberCard));
            characters.remove(characters.get(numberCard));
        }
        getGame().addCharacterCards(cards);
    }

    @Override
    public void update(T m) {
        Message message = (Message) m;
        if (message.getType().equals(MessageType.CHARACTER_CARD)) {
            CharacterCard card = ((CharacterCardMessage) message).getCharacterCard();
            //TODO it definitely needs fixing
            int index = 0;
            int flag = 0;
            for (int i = 0; i < getGame().getCharacterCards().length; i++) {
                if (getGame().getCharacterCards()[i].getCharacter().equals(card.getCharacter())) {
                    index = i;
                    flag = 1;
                }
            }
            if (flag == 0) {
                // TODO throw card not found exception
            }
            canActivateCharacterAbility(index);
            activateCharacterAbility(index);
        } else {
            super.update(m);
            //TODO how to check if the player need to take a coin ?
        }
    }

    //TODO do something about this function
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
        ((SetupEffect) effects[effectIndex])
                .setupEffect(tableController, (CharacterCardWithSetUpAction) getGame().getCharacterCards()[effectIndex]);
    }

    private void activateStandardEffect(int effectIndex) {
        ((StandardEffect) effects[effectIndex]).activateEffect(getGameParameters());
    }

    private void activateReverseEffect(int effectIndex) {
        ((StandardEffect) effects[effectIndex]).reverseEffect(getGameParameters());
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
        for (Effect e : effects) {
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
