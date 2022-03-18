package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.utils.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

//TODO check access modifiers
//TODO order methods by access modifier: public, protected, private
//TODO sooner or later we will have to remove unused methods
public class Game {
    private final Player[] players;
    private final Table table;

    private GamePhase gamePhase;
    private int playedCount;

    private int currentPlayer;
    //TODO this value is used but never initialized
    private int firstPlayerInRound;
    private SchoolBoard currentPlayerBoard;

    private int movedPawns;


    public Game(Player[] players) {
        this.players = players;
        this.table = players.length == 3 ? new Table(PlayerCountIcon.THREE) : new Table(PlayerCountIcon.TWO_FOUR);

        Bag bag = this.table.getBag();
        for (Player c : this.players) {
            c.getBoard().addStudentToEntrance(bag.drawStudents(7));   //TODO gameParams
        }

        this.currentPlayer = 0;
        this.gamePhase = GamePhase.PLANNING;
    }

    private boolean isTurnComplete() {
        return this.playedCount == this.players.length;
    }

    //TODO someone should call this method
    private void planning() {
        this.table.fillClouds();

        this.playedCount = 0;
    }

    //TODO how to check this method is called exclusively by the currentPlayer? (Same for following methods)
    public void playAssistant(int assistantIndex) throws IllegalActionException, IllegalAssistantException {
        if (this.gamePhase != GamePhase.PLANNING) {
            throw new IllegalActionException();
        }
        if (!this.canPlayAssistant(players[currentPlayer].getAssistant(assistantIndex))) {
            throw new IllegalAssistantException();
        }
        players[currentPlayer].playAssistant(assistantIndex);

        this.playerHasEndedPlanning();
    }

    private void playerHasEndedPlanning() {
        this.playedCount++;

        if (!this.isTurnComplete()) {
            this.currentPlayer = pickNextPlayer();
            this.currentPlayerBoard = players[this.currentPlayer].getBoard();
        } else {
            this.playedCount = 0;
            this.gamePhase = pickNextPhase();
        }
    }

    private void playerHasEndedAction() {
        this.gamePhase = this.pickNextPhase();
        if (this.gamePhase == GamePhase.ACTION_END) {
            this.playedCount++;

            if (!this.isTurnComplete()) {
                //TODO I'm repeating this snippet too many times, move into pickNextPlayer?
                this.currentPlayer = pickNextPlayer();
                this.currentPlayerBoard = players[this.currentPlayer].getBoard();
            } else {
                this.gamePhase = GamePhase.PLANNING;
            }
        }
    }

    private GamePhase pickNextPhase() {
        return switch (this.gamePhase) {
            case PLANNING -> GamePhase.ACTION_MOVE_STUDENTS;
            case ACTION_MOVE_STUDENTS -> GamePhase.ACTION_MOVE_MOTHER_NATURE;
            case ACTION_MOVE_MOTHER_NATURE -> GamePhase.ACTION_CHOOSE_CLOUD;
            case ACTION_CHOOSE_CLOUD, ACTION_END -> GamePhase.ACTION_END;
        };
    }

    public void moveStudentToDiningRoom(PawnColor pawn) throws IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_MOVE_STUDENTS) {
            throw new IllegalActionException();
        }
        this.currentPlayerBoard.moveStudentFromEntranceToDiningRoom(pawn);
        this.checkProfessorsStatus(pawn);
        this.movedPawn();
    }

    public void moveStudentToIsle(PawnColor pawn, int isle) {
        this.table.movePawnOnIsland(pawn, isle);
        this.movedPawn();
    }

    private void movedPawn() {
        this.movedPawns++;
        //TODO
        /* if (this.movedPawns == params) {
            this.movedPawns = 0;
            this.playerHasEndedAction();
        } */
    }

    //Checks if the current player has the highest number of students of the given color in his dining room
    //If so, proceeds to move the professor to the player's professor table
    private void checkProfessorsStatus(PawnColor color) {
        //It will also check the current player with itself, but this should not cause problems
        for (Player p : players) {
            players[currentPlayer].tryStealProfessor(color, p);
        }
    }

    public void moveMotherNature(int steps) throws NotAllowedMotherNatureMovementException, IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_MOVE_MOTHER_NATURE) {
            throw new IllegalActionException();
        }
        if (steps < 1 || steps > this.players[currentPlayer].getDiscardPileHead().motherNatureMovement()) {
            throw new NotAllowedMotherNatureMovementException();
        }
        this.table.moveMotherNature(steps);

        this.buildTowers();

        this.playerHasEndedAction();
    }

    private void buildTowers() {
        if (this.table.canBuildTower(currentPlayerBoard.getTowerColor())) {
            Pair result = this.table.buildTower(currentPlayerBoard.getTowerColor());
            Arrays.stream(this.players)
                    .filter(x -> x.getSchoolBoard().getTowerColor() == result.tower())
                    .forEach(x -> x.getSchoolBoard().addTowers(result.size()));
        }
    }

    public void pickStudentsFromCloud(int cloudIndex) throws IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_CHOOSE_CLOUD) {
            throw new IllegalActionException();
        }
        //TODO check params validity
        List<PawnColor> studentsFromCloud = this.table.takeStudentsFromCloud(cloudIndex);
        currentPlayerBoard.addStudentToEntrance(studentsFromCloud);

        this.playerHasEndedAction();
    }

    private boolean canPlayAssistant(AssistantCard assistant) {
        //If assistant is different from every other played assistantCard
        if (isAssistantDifferentFromOthers(assistant)) return true;

        //If assistant is equal to another played assistantCard, check if in the player's deck exist at least one card
        // different from every other one
        for (AssistantCard ac : players[currentPlayer].getAssistantDeck()) {
            if (!isAssistantDifferentFromOthers(ac)) return false;
        }
        return true;
    }

    //Returns true if assistant is different from every other assistants already played in this turn
    private boolean isAssistantDifferentFromOthers(AssistantCard assistant) {
        for (int i = firstPlayerInRound; i != currentPlayer; i = (i + 1) % players.length) {
            if (players[i].getDiscardPileHead().equals(assistant)) return false;
        }
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
                Optional<Player> nextPlayer = Arrays.stream(players)
                        .filter((Player p) ->
                                p.getDiscardPileHead().value() >= players[currentPlayer].getDiscardPileHead().value())
                        .sorted(Comparator.comparing(p -> ((p.getDiscardPileHead().value()))))
                        .findFirst();

                if (nextPlayer.isEmpty()) nextPlayer = Optional.of(players[0]);
                
                for (int i = 0; i < players.length; i++) {
                    //TODO are we sure == is ok?
                    if (players[i] == nextPlayer.get())
                        return i;
                }
        }
        return 0;
    }
}
