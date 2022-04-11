package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalAssistantException;
import it.polimi.ingsw.controller.exceptions.NotAllowedMotherNatureMovementException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.persistency.DataDumper;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

import static it.polimi.ingsw.model.enums.GamePhase.ACTION_MOVE_STUDENTS;
import static it.polimi.ingsw.model.enums.GamePhase.PLANNING;

//TODO the whole controller (and model) must be serializable
public class GameController<T> implements Observer<T> {
    protected Game game;
    protected TableController tableController;

    //TODO we need to receive also the virtualViews and add them as observers of the model's classes
    public GameController(Game game, TableController tableController){
        this.game = game;
        this.tableController = tableController;
    }

    //TODO who send us the players?
    public GameController(Player[] players) {
        this.init(players);
    }

    protected void init(Player[] players) {
        this.game = new Game(players);
        this.tableController = new TableController(game.getTable());
        //TODO what is this part for?
        LinkedList<PawnColor> students = new LinkedList<>();
        for (Player c : game.getPlayers()) {
            for (int i = 0; i < game.getParameters().getInitialStudentsCount(); i++) {
                students.add(tableController.getBag().drawStudent());
            }
            c.getBoard().addStudentsToEntrance(students);
        }
    }

    //TODO move away from here. The game controller controls ONLY the game
    // starts the game thread
    //@Override
    public void run() {
        this.game.setCurrentPlayer(0);
        this.game.setGamePhase(PLANNING);
    }
    @Override
    public void update(T message) {
        switch (game.getGamePhase()) {
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
        this.tableController.fillClouds();
        this.game.setPlayedCount(0);
    }

    private void playAssistant(int assistantIndex) throws IllegalActionException, IllegalAssistantException {
        if (this.game.getGamePhase() != PLANNING) {
            throw new IllegalActionException();
        }
        Player[] players = game.getPlayers();
        if (!this.canPlayAssistant(players[game.getCurrentPlayer()].getAssistant(assistantIndex))) {
            throw new IllegalAssistantException();
        }

        players[game.getCurrentPlayer()]
                .playAssistant(players[game.getCurrentPlayer()].getAssistant(assistantIndex));

        this.playerHasEndedPlanning();
    }

    private boolean canPlayAssistant(AssistantCard assistant) {
        //If assistant is different from every other played assistantCard
        if (isAssistantDifferentFromOthers(assistant)) return true;

        //If assistant is equal to another played assistantCard, check if in the player's deck exist at least one card
        // different from every other one
        for (AssistantCard ac : game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()) {
            if (!isAssistantDifferentFromOthers(ac)) return false;
        }
        return true;
    }

    private void playerHasEndedPlanning() {
        this.game.setPlayedCount(game.getPlayedCount() + 1);

        if (!this.isTurnComplete()) {
            this.game.setCurrentPlayer(pickNextPlayer());
            this.game.setCurrentPlayerBoard(game.getCurrentPlayerSchoolBoard());
        } else {
            this.game.setPlayedCount(0);
            this.game.setGamePhase(pickNextPhase());
            this.game.setCurrentPlayer(pickNextPlayer());
        }

        DataDumper.getInstance().saveGame(game);
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
        if (this.game.getGamePhase() != ACTION_MOVE_STUDENTS) {
            throw new IllegalActionException();
        }
        this.game.getCurrentPlayerBoard().moveStudentFromEntranceToDiningRoom(pawn);
        this.checkProfessorsStatus(pawn);
        this.movedPawn();
    }

    public void tryStealProfessor(PawnColor color, Player player) {
        if (!game.getCurrentPlayerSchoolBoard().isThereProfessor(color) &&
                player.getBoard().isThereProfessor(color) &&
                game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(color) > player.getBoard().getStudentsInDiningRoom(color)) {
            try {
                player.getBoard().removeProfessor(color);
            } catch (Exception e) {
                //TODO Nooooooooooooooooooo
                e.printStackTrace();
            }
            game.getCurrentPlayerSchoolBoard().addProfessor(color);
        }
    }

    public void moveStudentToIsle(PawnColor pawn, int isle) {
        this.tableController.movePawnOnIsland(pawn, isle);
        this.movedPawn();
    }

    public void moveMotherNature(int steps) throws NotAllowedMotherNatureMovementException, IllegalActionException {
        if (this.game.getGamePhase() != GamePhase.ACTION_MOVE_MOTHER_NATURE) {
            throw new IllegalActionException();
        }
        if (steps < 1 || steps > this.game.getPlayers()[game.getCurrentPlayer()].getDiscardPileHead().motherNatureMovement()) {
            throw new NotAllowedMotherNatureMovementException();
        }
        this.tableController.moveMotherNature(steps);

        this.checkInfluence();

        this.playerHasEndedAction();
    }

    private void checkInfluence() {
        int maxInfluence = 0;
        int currentInfluence;
        Player maxInfluencePlayer = game.getPlayers()[game.getCurrentPlayer()]; //default condition, it shouldn't matter

        for (Player p : game.getPlayers()) {
            currentInfluence = tableController.countInfluenceOnIsland(p.getBoard().getProfessors(), p.getBoard().getTowerColor());

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
        if (this.tableController.canBuildTower(player.getBoard().getTowerColor())) {
            Pair<Tower, Integer> result = this.tableController.buildTower(player.getBoard().getTowerColor());
            Arrays.stream(this.game.getPlayers())
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
        if (this.game.getGamePhase() != GamePhase.ACTION_CHOOSE_CLOUD) {
            throw new IllegalActionException();
        }
        //TODO check params validity
        List<PawnColor> studentsFromCloud = this.tableController.takeStudentsFromCloud(cloudIndex);
        game.getCurrentPlayerBoard().addStudentsToEntrance(studentsFromCloud);
        this.playerHasEndedAction();
    }

    //Checks if the current player has the highest number of students of the given color in his dining room
    //If so, proceeds to move the professor to the player's professor table
    private void checkProfessorsStatus(PawnColor color) {
        //first try to check if it's still available on the table, if so it's useless to do the second check
        if (this.tableController.takeProfessor(color)) {
            game.getPlayers()[game.getCurrentPlayer()].getBoard().addProfessor(color);
        } else {
            //It will also check the current player with itself, but this should not cause problems
            for (Player p : game.getPlayers()) {
                tryStealProfessor(color, p);
            }
        }
    }

    //Returns true if assistant is different from every other assistants already played in this turn
    private boolean isAssistantDifferentFromOthers(AssistantCard assistant) {
        for (int i = game.getFirstPlayerInRound(); i != game.getCurrentPlayer(); i = (i + 1) % game.getPlayersCount()) {
            if (game.getPlayers()[i].getDiscardPileHead().equals(assistant)) return false;
        }
        return true;
    }

    private boolean isTurnComplete() {
        return this.game.getPlayedCount() == this.game.getPlayersCount();
    }

    private void movedPawn() {
        //TODO increment instead of get + 1
        game.setMovedPawns(game.getMovedPawns() + 1);
        if (this.game.getMovedPawns() == game.getPlayersCount() + 1) {
            this.game.setMovedPawns(0);
            this.playerHasEndedAction();
        }
    }

    public int winner() {
        //TODO maybe throw an exception if the game is not over?
        //TODO don't call every time game.getPlayers() but save them in a variable
        int min = 0;
        boolean flag = false;
        for (int i = 0; i < game.getPlayersCount(); i++) {
            if (game.getPlayers()[i].getBoard().getTowersCount() < game.getPlayers()[min].getBoard().getTowersCount())
                min = i;
            if (game.getPlayers()[i].getBoard().getTowersCount() == game.getPlayers()[min].getBoard().getTowersCount()) {
                if (game.getPlayers()[i].getBoard().countProfessors() > game.getPlayers()[min].getBoard().countProfessors())
                    min = i;
            }
        }
        //return player with the minimum number of towers
        return min;
    }

    private GamePhase pickNextPhase() {
        return switch (this.game.getGamePhase()) {
            case PLANNING -> ACTION_MOVE_STUDENTS;
            case ACTION_MOVE_STUDENTS -> GamePhase.ACTION_MOVE_MOTHER_NATURE;
            case ACTION_MOVE_MOTHER_NATURE -> GamePhase.ACTION_CHOOSE_CLOUD;
            case ACTION_CHOOSE_CLOUD, ACTION_END -> GamePhase.ACTION_END;
        };
    }

    //TODO hopefully it will become less complex
    private int pickNextPlayer() {
        switch (game.getGamePhase()) {
            case PLANNING:
                return (game.getCurrentPlayer() + 1) % game.getPlayersCount();
            case ACTION_MOVE_STUDENTS, ACTION_MOVE_MOTHER_NATURE, ACTION_CHOOSE_CLOUD:
                Optional<Player> nextPlayer = Arrays.stream(game.getPlayers())
                        .filter((Player p) ->
                                p.getDiscardPileHead().value() >= game.getPlayers()[game.getCurrentPlayer()].getDiscardPileHead().value())
                        .min(Comparator.comparing(p -> ((p.getDiscardPileHead().value()))));

                if (nextPlayer.isEmpty()) nextPlayer = Optional.of(game.getPlayers()[0]);

                for (int i = 0; i < game.getPlayers().length; i++) {
                    if (game.getPlayers()[i].getNickname().equals(nextPlayer.get().getNickname()))
                        return i;
                }
            case ACTION_END:
                return game.getCurrentPlayer();
        }
        return 0;
    }


    protected void playerHasEndedAction() {
        this.game.setGamePhase(this.pickNextPhase());
        if (this.game.getGamePhase() == GamePhase.ACTION_END) {
            this.game.setPlayedCount(game.getPlayedCount() + 1);
            if (!this.isTurnComplete()) {
                //TODO I'm repeating this snippet too many times, move into pickNextPlayer?
                this.game.setCurrentPlayer(pickNextPlayer());
                this.game.setCurrentPlayerBoard(game.getPlayers()[this.game.getCurrentPlayer()].getBoard());
                this.game.setGamePhase(ACTION_MOVE_STUDENTS);
            } else {
                this.game.setGamePhase(PLANNING);
            }
        }
        DataDumper.getInstance().saveGame(game);
        checkTurnGameOver();
    }

    public void checkImmediateGameOver() {
        if (!isImmediateGameOver())
            return;

        //TODO what to do after the game has ended
        DataDumper.getInstance().removeGameFromMemory(game.getGameId());
    }

    //TODO think about a better function name
    public void checkTurnGameOver() {
        if (!isTurnGameOver())
            return;

        //TODO what to do after the game has ended

        DataDumper.getInstance().removeGameFromMemory(game.getGameId());
    }

    public boolean isImmediateGameOver() {
        //check if any player has build his last tower
        for (Player p : game.getPlayers()) {
            if (p.getBoard().getTowersCount() == 0)
                return true;
        }

        //check if only 3 island group remain on the table
        return tableController.countIslands() == 3;
    }

    public boolean isTurnGameOver() {
        //check if the last student has been drawn from the bag
        if (tableController.getBag().isEmpty())
            return true;

        //check if any player has run out of assistant card
        for (Player p : game.getPlayers()) {
            if (p.isDeckEmpty())
                return true;
        }
        return false;
    }


}
