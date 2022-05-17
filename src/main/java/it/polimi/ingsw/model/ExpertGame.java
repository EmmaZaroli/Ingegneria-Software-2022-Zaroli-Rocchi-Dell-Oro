package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PawnColor;

import java.util.Arrays;
import java.util.List;

public class ExpertGame extends Game {

    private CharacterCard[] characterCards;


    public ExpertGame(ExpertPlayer[] players, ExpertTable table, ExpertGameParameters parameters) {
        super(players, table, parameters);
    }

    public CharacterCard[] getCharacterCards() {
        return characterCards.clone();
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

    //TODO call this method when someone uses a character
    public void useCharacterCard(CharacterCard c) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    x.setUsed();
                    notify(x);
                });
    }

    public void addStudent(CharacterCard c, PawnColor student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).addStudent(student);
                    notify(x);
                });
    }

    public void addStudent(CharacterCard c, List<PawnColor> student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).addStudent(student);
                    notify(x);
                });
    }

    public void removeStudent(CharacterCard c, PawnColor student) {
        Arrays.stream(characterCards)
                .filter(x -> x.equals(c))
                .forEach(x -> {
                    ((CharacterCardWithSetUpAction) x).removeStudent(student);
                    notify(x);
                });
    }

    public void addCoin(ExpertPlayer player) {
        player.addCoin();
        notify(player);
    }

    public void decreaseCoins(ExpertPlayer player, int n) {
        player.decreaseCoins(n);
        notify(player);
    }
}
