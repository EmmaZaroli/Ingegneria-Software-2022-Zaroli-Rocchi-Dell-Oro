package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalAssistantException;
import it.polimi.ingsw.controller.exceptions.NotAllowedMotherNatureMovementException;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.controller.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.PlayerCountIcon;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.persistency.DataDumper;
import it.polimi.ingsw.utils.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

//TODO the whole controller (and model) must be serializable
public class GameController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID gameUUID = UUID.randomUUID();

    private final Player[] players;
    //had to remove final from table because it would have been impossible to make it ExpertTable in ExpertGameController
    protected TableController table;
    private GamePhase gamePhase;
    private int playedCount;
    private GameParameters parameters;

    private int currentPlayer;
    //TODO this value is used but never initialized
    private int firstPlayerInRound;
    protected SchoolBoard currentPlayerBoard;
    private int movedPawns;


    //TODO who send us the players?
    public GameController(Player[] players) {
        this.players = players;
        this.table = players.length == 3 ? new TableController(PlayerCountIcon.THREE) : new TableController(PlayerCountIcon.TWO_FOUR);
        for (Player c : this.players) {
            c.getBoard().addStudentsToEntrance(table.drawStudents());
        }
        this.currentPlayer = 0;
        this.parameters = new GameParameters();
    }

    // starts the game thread
    //@Override
    public void run() {
        currentPlayer = 0;
        this.gamePhase = GamePhase.PLANNING;
    }

    public void MessageReceiver(/*Message*/) {
        switch (gamePhase) {
            case PLANNING:
                //check(Message) ->message.nickname.equals(currentPlayer)
                //planning();
                break;
            case ACTION_MOVE_STUDENTS:
                //check(Message) ->message.nickname.equals(currentPlayer)
                moveStudent(/*Message*/);
                break;
            case ACTION_MOVE_MOTHER_NATURE:
                //check(Message) ->message.nickname.equals(currentPlayer)
                //moveMotherNature(Message.steps());
                break;
            case ACTION_CHOOSE_CLOUD:
                //check(Message) ->message.nickname.equals(currentPlayer)
                //pickStudentsFromCloud(Message.cloudindex());
                break;
            case ACTION_END:
                //should not go here, the player doesn't do anything in this phase
                break;
        }
    }

    /*private void planning(Message) {
        switch (Message):
            case fillClouds:
                this.fillClouds(); //only one time possible
            case playAssistant:
                this.playAssistant(Message.index())
    }
    */

    private void fillClouds() {
        this.table.fillClouds();
        this.playedCount = 0;
    }


    private void playAssistant(int assistantIndex) throws IllegalActionException, IllegalAssistantException {
        if (this.gamePhase != GamePhase.PLANNING) {
            throw new IllegalActionException();
        }
        if (!this.canPlayAssistant(players[currentPlayer].getAssistant(assistantIndex))) {
            throw new IllegalAssistantException();
        }
        players[currentPlayer].playAssistant(assistantIndex);

        this.playerHasEndedPlanning();
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

    private void playerHasEndedPlanning() {
        this.playedCount++;

        if (!this.isTurnComplete()) {
            this.currentPlayer = pickNextPlayer();
            this.currentPlayerBoard = players[this.currentPlayer].getBoard();
        } else {
            this.playedCount = 0;
            this.gamePhase = pickNextPhase();
            this.currentPlayer = pickNextPlayer();
        }

        DataDumper.getInstance().saveGame(this);
    }

    private void moveStudent(/*Message*/) {
        /* switch(message) {
            case OnIsland:
                moveStudentOnIsland(message.pawn());
                break;
            case OnDiningRoom:
                moveStudentToDiningRoom(message.pawn());
         */
    }

    public void moveStudentToDiningRoom(PawnColor pawn) throws IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_MOVE_STUDENTS) {
            throw new IllegalActionException();
        }
        this.currentPlayerBoard.moveStudentFromEntranceToDiningRoom(pawn);
        this.checkProfessorsStatus(pawn);
        this.movedPawn();
    }

    public void tryStealProfessor(PawnColor color, Player player) {
        if (!players[currentPlayer].getBoard().isThereProfessor(color) &&
                player.getBoard().isThereProfessor(color) &&
                players[currentPlayer].getBoard().getStudentsInDiningRoom(color) > player.getBoard().getStudentsInDiningRoom(color)) {
            try {
                player.getBoard().removeProfessor(color);
            } catch (Exception e) {
                e.printStackTrace();
            }
            players[currentPlayer].getBoard().addProfessor(color);
        }
    }

    public void moveStudentToIsle(PawnColor pawn, int isle) {
        this.table.movePawnOnIsland(pawn, isle);
        this.movedPawn();
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
        int currentInfluence;
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
            Pair<Tower, Integer> result = this.table.buildTower(player.getBoard().getTowerColor());
            Arrays.stream(this.players)
                    .filter(x -> x.getBoard().getTowerColor() == result.first())
                    .forEach(x -> {
                        try {
                            x.getBoard().addTowers(result.second());
                        } catch (Exception e) {
                            //TODO this is not the proper way of handling exceptions
                            e.printStackTrace();
                        }
                    });
            for (int i = 0; i < result.second(); i++) {
                player.getBoard().removeTower();
                checkImmediateGameOver();
            }
        }
    }

    public void pickStudentsFromCloud(int cloudIndex) throws IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_CHOOSE_CLOUD) {
            throw new IllegalActionException();
        }
        //TODO check params validity
        List<PawnColor> studentsFromCloud = this.table.takeStudentsFromCloud(cloudIndex);
        currentPlayerBoard.addStudentsToEntrance(studentsFromCloud);
        this.playerHasEndedAction();
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
                tryStealProfessor(color, p);
            }
        }
    }

    //Returns true if assistant is different from every other assistants already played in this turn
    private boolean isAssistantDifferentFromOthers(AssistantCard assistant) {
        for (int i = firstPlayerInRound; i != currentPlayer; i = (i + 1) % players.length) {
            if (players[i].getDiscardPileHead().equals(assistant)) return false;
        }
        return true;
    }

    private boolean isTurnComplete() {
        return this.playedCount == this.players.length;
    }

    private void movedPawn() {
        this.movedPawns++;
        if (this.movedPawns == players.length + 1) {
            this.movedPawns = 0;
            this.playerHasEndedAction();
        }
    }

    public int winner() {
        //TODO maybe throw an exception if the game is not over?
        int min = 0;
        boolean flag = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getBoard().getTowersCount() < players[min].getBoard().getTowersCount())
                min = i;
            if (players[i].getBoard().getTowersCount() == players[min].getBoard().getTowersCount()) {
                if (players[i].getBoard().countProfessors() > players[min].getBoard().countProfessors())
                    min = i;
            }
        }
        //return player with the minimum number of towers
        return min;
    }

    private GamePhase pickNextPhase() {
        return switch (this.gamePhase) {
            case PLANNING -> GamePhase.ACTION_MOVE_STUDENTS;
            case ACTION_MOVE_STUDENTS -> GamePhase.ACTION_MOVE_MOTHER_NATURE;
            case ACTION_MOVE_MOTHER_NATURE -> GamePhase.ACTION_CHOOSE_CLOUD;
            case ACTION_CHOOSE_CLOUD, ACTION_END -> GamePhase.ACTION_END;
        };
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
                        .min(Comparator.comparing(p -> ((p.getDiscardPileHead().value()))));

                if (nextPlayer.isEmpty()) nextPlayer = Optional.of(players[0]);

                for (int i = 0; i < players.length; i++) {
                    if (players[i].getNickname().equals(nextPlayer.get().getNickname()))
                        return i;
                }
            case ACTION_END:
                return currentPlayer;
        }
        return 0;
    }


    protected void playerHasEndedAction() {
        this.gamePhase = this.pickNextPhase();
        if (this.gamePhase == GamePhase.ACTION_END) {
            this.playedCount++;
            if (!this.isTurnComplete()) {
                //TODO I'm repeating this snippet too many times, move into pickNextPlayer?
                this.currentPlayer = pickNextPlayer();
                this.currentPlayerBoard = players[this.currentPlayer].getBoard();
                this.gamePhase = GamePhase.ACTION_MOVE_STUDENTS;
            } else {
                this.gamePhase = GamePhase.PLANNING;
            }
        }
        DataDumper.getInstance().saveGame(this);
        checkTurnGameOver();
    }


    public void checkImmediateGameOver() {
        if (!isImmediateGameOver())
            return;

        //TODO what to do after the game has ended
        DataDumper.getInstance().removeGameFromMemory(this.gameUUID);
    }

    //TODO think about a better function name
    public void checkTurnGameOver() {
        if (!isTurnGameOver())
            return;

        //TODO what to do after the game has ended

        DataDumper.getInstance().removeGameFromMemory(this.gameUUID);
    }

    public boolean isImmediateGameOver() {
        //check if any player has build his last tower
        for (Player p : players) {
            if (p.getBoard().getTowersCount() == 0)
                return true;
        }

        //check if only 3 island group remain on the table
        return table.howManyIsland() == 3;
    }

    public boolean isTurnGameOver() {
        //check if the last student has been drawn from the bag
        if (table.getBag().isEmpty())
            return true;

        //check if any player has run out of assistant card
        for (Player p : players) {
            if (p.isDeckEmpty())
                return true;
        }

        return false;
    }

    protected int getCurrentPlayerIndex() {
        return currentPlayer;
    }

    protected Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    protected Player[] getPlayers() {
        return players;
    }

    public GameParameters getGameParameters() {
        return parameters;
    }

    public UUID getGameId() {
        return this.gameUUID;
    }
}
