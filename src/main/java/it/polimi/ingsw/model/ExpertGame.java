package it.polimi.ingsw.model;

public class ExpertGame extends Game {

    private CharacterCard[] characterCards;
    private Effect[] effects;

    public ExpertGame(Player[] players) {
        super(players);
    }

    protected void init(Player[] players){
        this.players = players;
        this.table = new ExpertTable(this.players.length);
        this.currentPlayer = 0;
        this.parameters = new ExpertGameParameters();
    }

    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    public Effect[] getEffects() {
        return effects;
    }

    @Override
    public ExpertPlayer[] getPlayers() {
        return (ExpertPlayer[]) super.getPlayers();
    }

    public ExpertTable getTable() {
        return (ExpertTable) getTable();
    }
}
