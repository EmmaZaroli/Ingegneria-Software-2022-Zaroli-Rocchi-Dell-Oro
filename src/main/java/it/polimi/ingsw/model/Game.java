package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.PlayerCountIcon;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//TODO order methods by access modifier: public, protected, private
public class Game {
    private final Player[] players;
    private final Table table;

    private GamePhase gamePhase;
    private int playedCount;

    private int currentPlayer;
    private SchoolBoard currentPlayerBoard;
    private boolean isFirstRound;

    //TODO manage game over logics
    public Game(Player[] players) {
        //TODO build players somewhere
        this.players = players;
        this.table = players.length == 3 ? new Table(PlayerCountIcon.THREE) : new Table(PlayerCountIcon.TWO_FOUR);

        Bag bag = this.table.getBag();
        for (Player c : this.players) {
            c.getBoard().addStudentToEntrance(bag.drawStudents(7));   //TODO gameParams
        }

        this.currentPlayer = 0;
        this.isFirstRound = true;
        this.gamePhase = GamePhase.PLANNING;
    }

    //TODO find consistent name to this method
    protected void foo() {
        switch (this.gamePhase) {
            case PLANNING -> this.planning();
            case ACTION_MOVE_STUDENTS -> {
            } //TODO
            case ACTION_MOVE_MOTHER_NATURE -> {
            }    //TODO
            case ACTION_CHOOSE_CLOUD -> {
            }  //TODO
        }
    }

    private boolean isTurnComplete() {
        return this.playedCount == this.players.length;
    }

    //TODO probably it is better to have an unique function to cycle the players
    private void planning() {
        this.table.fillClouds();

        this.playedCount = 0;

        while (!this.isTurnComplete()) {
            //TODO observer will be notified about this change
            this.currentPlayer = pickNextPlayer();
            this.currentPlayerBoard = players[this.currentPlayer].getBoard();
        }
    }

    //TODO how to check this method is called exclusively by the currentPlayer?
    public void playAssistant(int assistant) {
        if (!this.canPlayAssistant(assistant)) {
            //TODO throw exception
        }
        this.players[this.currentPlayer].playAssistant(assistant);
    }

    //TODO same issues of previous methods (and so on in the following methods)
    //TODO check total moved pawns == allowed number
    public void moveStudentsToDiningRoom(List<PawnColor> pawns) {
        this.currentPlayerBoard.moveStudentFromEntranceToDiningRoom(pawns);
    }

    public void moveStudentsToIsle(List<PawnColor> pawns, int isle) {
        for (PawnColor pc : pawns) {
            //TODO maybe we should standardize the way we select an isle/player ecc... (always an int?)
            this.table.movePawnOnIsland(pc, null);
        }
    }

    public void moveMotherNature(int steps) {
        if (steps > this.players[currentPlayer].getDiscardPileHead().getMotherNatureMovement()) {
            //TODO throw exception or notify an error
        }
        this.table.moveMotherNature(steps);
        //TODO add logic to build towers
        //TODO add logic to return towers to schoolBoard
    }

    public void drawStudentsFromCloud(int cloudNumber) {
        //TODO int better than reference: already written some methods ago...
        List<PawnColor> studentsFromCloud = this.table.takeStudentsFromCloud(null);
        currentPlayerBoard.addStudentToEntrance(studentsFromCloud);
    }

    private boolean canPlayAssistant(int assistant) {
        //TODO
        return true;
    }

    //TODO hopefully it will become less complex
    private int pickNextPlayer() {
        switch (gamePhase) {
            case PLANNING:
                return (currentPlayer + 1) % players.length;
            case ACTION_MOVE_STUDENTS:
            case ACTION_MOVE_MOTHER_NATURE:
            case ACTION_CHOOSE_CLOUD:
                Optional<Player> nextPlayer = Arrays.stream(players).filter((Player p) -> {
                    return p.getDiscardPileHead().getValue() >= players[currentPlayer].getDiscardPileHead().getValue();
                }).sorted((p1, p2) -> ((Integer) (p1.getDiscardPileHead().getValue())).compareTo((Integer) (p2.getDiscardPileHead().getValue()))).findFirst();
                if (!nextPlayer.isPresent())
                    nextPlayer = Optional.of(players[0]);
                for (int i = 0; i < players.length; i++) {
                    if (players[i] == nextPlayer.get())
                        return i;
                }
        }
        return 0;
    }
}
