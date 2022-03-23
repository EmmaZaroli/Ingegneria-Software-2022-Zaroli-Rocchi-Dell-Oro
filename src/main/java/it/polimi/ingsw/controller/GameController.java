package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.PlayerCountIcon;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.IllegalAssistantException;
import it.polimi.ingsw.model.exceptions.NotAllowedMotherNatureMovementException;
import it.polimi.ingsw.utils.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GameController {
    private final Player[] players;
    private final TableController table;

    private GamePhase gamePhase;
    private int playedCount;

    private int currentPlayer;
    //TODO this value is used but never initialized
    private int firstPlayerInRound;
    private SchoolBoard currentPlayerBoard;

    private int movedPawns;

    //TODO manage game over logics
    public GameController(Player[] players) {
        this.players = players;
        this.table = players.length == 3 ? new TableController(PlayerCountIcon.THREE) : new TableController(PlayerCountIcon.TWO_FOUR);

        for (Player c : this.players) {
            c.getBoard().addStudentToEntrance(table.drawStudents());
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
        checkTournGameOver();
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
        //first try to check if it's still available on the table, if so it's useless to do the second check
        if (this.table.takeProfessor(color)) {
            players[currentPlayer].getBoard().addProfessor(color);
        } else {
            //It will also check the current player with itself, but this should not cause problems
            for (Player p : players) {
                players[currentPlayer].tryStealProfessor(color, p);
            }
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

        this.checkInfluence();

        this.playerHasEndedAction();
    }

    private void checkInfluence() {
        int maxInfluence = 0;
        int currentInfluence = 0;
        Player maxInfluencePlayer = players[currentPlayer]; //default condition, it shouldn't matter

        for (Player p : players) {
            currentInfluence = table.countInfluenceOnIsland(p.getBoard().getProfessors(), p.getBoard().getTowerColor());

            if (currentInfluence > maxInfluence) {
                maxInfluencePlayer = p;
                maxInfluence = currentInfluence;
            }
        }

        //if the maxInfluence is 0, none of the player has a professor
        if (maxInfluence != 0) this.buildTowers(maxInfluencePlayer);
    }

    //Builds the tower of the player with max influence
    private void buildTowers(Player player) {
        if (this.table.canBuildTower(player.getBoard().getTowerColor())) {
            Pair result = this.table.buildTower(player.getBoard().getTowerColor());
            Arrays.stream(this.players)
                    .filter(x -> x.getSchoolBoard().getTowerColor() == result.tower())
                    .forEach(x -> x.getSchoolBoard().addTowers(result.size()));
        }
        checkImmediateGameOver();
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
            case ACTION_MOVE_STUDENTS, ACTION_MOVE_MOTHER_NATURE, ACTION_CHOOSE_CLOUD:
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
            case ACTION_END:
                return currentPlayer;
        }
        return 0;
    }

    public boolean isImmediateGameOver(){
        //check if any player has build his last tower
        for(Player p : players){
            if(p.getBoard().getTowers() == 0)
                return true;
        }

        //check if only 3 island group remain on the table
        if(table.howManyIsland() == 3)
            return true;

        return false;
    }

    public boolean isTournGameOver(){
        //check if the last student has been drawn from the bag
        if(table.getBag().isEmpty())
            return true;

        //check if any player has run out of assistant card
        for(Player p : players){
            if(p.isDeckEmpty())
                return true;
        }

        return false;
    }

    public int winner(){
        //TODO maybe throw an exception if the game is not over?
        int min = 0;
        boolean flag = false;
        for(int i = 0; i < players.length; i++){
            if(players[i].getBoard().getTowers() < players[min].getBoard().getTowers())
                min = i;
            if(players[i].getBoard().getTowers() == players[min].getBoard().getTowers()){
                if(players[i].getBoard().howManyProfessors() > players[min].getBoard().howManyProfessors())
                    min = i;
            }
        }
        //return player with the minimum number of towers
        return min;
    }

    public void checkImmediateGameOver(){
        if(!isImmediateGameOver())
            return;

        //TODO what to do after the game has ended
    }

    //TODO think about a better function name
    public void checkTournGameOver(){
        if(!isTournGameOver())
            return;

        //TODO what to do after the game has ended
    }
}
