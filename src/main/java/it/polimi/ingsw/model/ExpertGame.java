package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Character;
import it.polimi.ingsw.model.enums.PawnColor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ExpertGame extends Game {

    private CharacterCard[] characterCards;


    public ExpertGame(ExpertPlayer[] players, ExpertTable table, ExpertGameParameters parameters) {
        super(players, table, parameters);
    }

    public CharacterCard[] getCharacterCards() {
        return characterCards.clone();
    }

    public Optional<CharacterCard> getCharacterCard(Character character){
        return Arrays.stream(characterCards).filter(c -> c.getCharacter().equals(character)).findFirst();
    }

    @Override
    public ExpertPlayer[] getPlayers() {
        return (ExpertPlayer[]) super.getPlayers();
    }

    @Override
    public ExpertTable getTable() {
        return (ExpertTable) super.getTable();
    }

    @Override
    public ExpertGameParameters getParameters() {
        return (ExpertGameParameters) super.getParameters();
    }

    public void setCharacterCards(CharacterCard[] cards) {
        this.characterCards = cards;
    }

    public void useCharacterCard(CharacterCard c) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    x.setUsed();
                    notifyCharacterCard(x, new Object[0]);
                });
        notifyExpertParameters(getParameters());
    }

    public void addStudent(CharacterCard c, PawnColor student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).addStudent(student);
                });
    }

    public void addStudent(CharacterCard c, List<PawnColor> student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).addStudent(student);
                });
    }

    public void removeStudent(CharacterCard c, PawnColor student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).removeStudent(student);
                });
    }

    public void addCoin(ExpertPlayer player) {
        player.addCoin();
        notifyPlayerCoins(player.getCoins());
    }

    public void decreaseCoins(ExpertPlayer player, int n) {
        player.decreaseCoins(n);
        notifyPlayerCoins(player.getCoins());
    }
}
