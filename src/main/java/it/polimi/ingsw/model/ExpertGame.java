package it.polimi.ingsw.model;

public class ExpertGame extends Game {

    private CharacterCard[] characterCards;

    public ExpertGame(ExpertPlayer[] players, ExpertTable table, ExpertGameParameters parameters){
        super(players, table, parameters);
    }

    public ExpertGame(Player[] players) {
        super(players);
    }

    @Override
    protected void init(Player[] players){
        this.players = players;
        this.table = new ExpertTable(this.players.length);
        this.currentPlayer = 0;
        this.parameters = new ExpertGameParameters();
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

    //TODO fix this up, it's not ok
    public void addCharacterCards(CharacterCard[] cards){
        this.characterCards = cards;
    }
}
