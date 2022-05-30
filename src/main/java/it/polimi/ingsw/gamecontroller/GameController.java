package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.exceptions.*;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.network.DisconnectionListener;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.AssistantPlayedMessage;
import it.polimi.ingsw.network.messages.CloudMessage;
import it.polimi.ingsw.network.messages.MoveMotherNatureMessage;
import it.polimi.ingsw.network.messages.MoveStudentMessage;
import it.polimi.ingsw.persistency.DataDumper;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

import static it.polimi.ingsw.model.enums.GamePhase.ACTION_MOVE_STUDENTS;
import static it.polimi.ingsw.model.enums.GamePhase.PLANNING;


//TODO add observables/observers
public class GameController implements DisconnectionListener, MessageListener {
    protected Game game;
    protected TableController tableController;
    protected VirtualView[] virtualViews;
    private Timer timer = new Timer();

    public GameController(Game game, TableController tableController, VirtualView[] virtualViews) {
        this.game = game;
        this.tableController = tableController;
        this.virtualViews = virtualViews;
        this.init(game.getPlayers());
    }

    protected void init(Player[] players) {
        fillClouds();
        for (Player player : game.getPlayers()) {
            player.getBoard().addStudentsToEntrance(tableController.drawStudents());
        }
        this.game.changePlayer(0);
        this.game.setGamePhase(PLANNING);

        //setting observers
        for (VirtualView virtualView : virtualViews) {
            game.addObserver(virtualView);
            for (Player player : players) {
                player.addObserver(virtualView);
                player.getBoard().addObserver(virtualView);
            }
            tableController.table.addObserver(virtualView);
        }
    }

    private void checkMessage(Message message) throws WrongPlayerException {
        if (!message.getNickname().equals(game.getPlayers()[game.getCurrentPlayer()].getNickname())) {
            throw new WrongPlayerException();
        }
    }

    private void tryMoveMotherNature(MoveMotherNatureMessage message) {
        try {
            moveMotherNature(message.getSteps());
        } catch (NotAllowedMotherNatureMovementException | IllegalActionException e) {
            game.throwException(e);
        }
    }

    //TODO eliminate this method
    public void update(Object m) {
        Message message = (Message) m;
        try {
            checkMessage(message);
            switch (game.getGamePhase()) {
                case PLANNING:
                    planning(message);
                    break;
                case ACTION_MOVE_STUDENTS:
                    moveStudent(message);
                    break;
                case ACTION_MOVE_MOTHER_NATURE:
                    if (message.getType().equals(MessageType.ACTION_MOVE_MOTHER_NATURE)) {
                        tryMoveMotherNature((MoveMotherNatureMessage) message);
                    } else game.throwException(new IllegalActionException());
                    break;
                case ACTION_CHOOSE_CLOUD:
                    if (message.getType().equals(MessageType.ACTION_CHOOSE_CLOUD)) {
                        try {
                            pickStudentsFromCloud(((CloudMessage) message).getCloud().getUuid());
                        } catch (EmptyCloudException | IllegalActionException | WrongUUIDException e) {
                            game.throwException(e);
                        }
                    } else game.throwException(new IllegalActionException());
                    break;
                case ACTION_END:
                    //should not go here, the player doesn't do anything in this phase
                    game.throwException(new IllegalActionException());
                    break;
            }
        } catch (WrongPlayerException e) {
            game.throwException(e);
        }

    }

    private void planning(Message message) {
        if (message.getType().equals(MessageType.ACTION_PLAY_ASSISTANT)) {
            try {
                this.playAssistant(((AssistantPlayedMessage) message).getAssistantCard().value());
            } catch (IllegalActionException | IllegalAssistantException e) {
                game.throwException(e);
            }
        } else {
            game.throwException(new IllegalActionException());
        }
    }

    protected void fillClouds() {
        try {
            this.tableController.fillClouds();
        } catch (FullCloudException e) {
            game.throwException(e);
        }
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
            changePlayer();
        } else {
            this.game.setPlayedCount(0);
            this.game.setGamePhase(pickNextPhase());
            changePlayer();
        }

        DataDumper.getInstance().saveGame(game);
    }

    private void moveStudent(Message message) {
        switch (message.getType()) {
            case ACTION_MOVE_STUDENTS_ON_ISLAND:
                try {
                    moveStudentOnIsland(((MoveStudentMessage) message).getStudentColor(), ((MoveStudentMessage) message).getIslandCard().getUuid());
                } catch (WrongUUIDException e) {
                    game.throwException(e);
                }
                break;
            case ACTION_MOVE_STUDENTS_ON_BOARD:
                try {
                    moveStudentToDiningRoom(((MoveStudentMessage) message).getStudentColor());
                } catch (IllegalActionException e) {
                    game.throwException(e);
                }
                break;
            default:
                game.throwException(new IllegalActionException());
        }
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
                game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(color)
                        > player.getBoard().getStudentsInDiningRoom(color)) {
            player.getBoard().removeProfessor(color);
            game.getCurrentPlayerSchoolBoard().addProfessor(color);
        }
    }

    public void moveStudentOnIsland(PawnColor pawn, UUID uuid) throws WrongUUIDException {
        this.tableController.movePawnOnIsland(pawn, uuid);
        this.game.getCurrentPlayerSchoolBoard().removeStudentFromEntrance(pawn);
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

    protected void checkInfluence() {
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

        //if two player have se same max influence, no tower is build
        if (isInfluenceDraw(maxInfluencePlayer, maxInfluence))
            return;

        //if the maxInfluence is 0, none of the player has a professor
        if (maxInfluence != 0) this.buildTowers(maxInfluencePlayer);
    }

    private boolean isInfluenceDraw(Player player, int influence) {
        for (Player p : game.getPlayers()) {
            if (!p.equals(player) && influence == tableController
                    .countInfluenceOnIsland(p.getBoard().getProfessors(), p.getBoard().getTowerColor())) {
                return true;
            }
        }
        return false;
    }

    //Builds the tower of the player with max influence
    private void buildTowers(Player player) {
        if (this.tableController.canBuildTower(player.getBoard().getTowerColor())) {
            Pair<Tower, Integer> result = this.tableController.buildTower(player.getBoard().getTowerColor());
            Arrays.stream(this.game.getPlayers())
                    .filter(x -> x.getBoard().getTowerColor() == result.first())
                    .forEach(x -> x.getBoard().addTowers(result.second()));
            for (int i = 0; i < result.second(); i++) {
                player.getBoard().removeTower();
                checkImmediateGameOver();
            }
        }
    }

    public void pickStudentsFromCloud(UUID uuid) throws IllegalActionException, EmptyCloudException, WrongUUIDException {
        if (this.game.getGamePhase() != GamePhase.ACTION_CHOOSE_CLOUD) {
            throw new IllegalActionException();
        }
        List<PawnColor> studentsFromCloud = this.tableController.takeStudentsFromCloud(uuid);
        if (studentsFromCloud.isEmpty()) {
            throw new EmptyCloudException();
        }
        game.getCurrentPlayerBoard().addStudentsToEntrance(studentsFromCloud);
        this.playerHasEndedAction();
    }

    //Checks if the current player has the highest number of students of the given color in his dining room
    //If so, proceeds to move the professor to the player's professor table
    void checkProfessorsStatus(PawnColor color) {
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

    void movedPawn() {
        if((game.getMovedPawns() + 1 )!= (game.getPlayersCount() +1))
            game.movePawn();
        else {
            this.game.setMovedPawns(0);
            this.playerHasEndedAction();
        }
    }

    //TODO does the same thing as whoIsWinner(), maybe we can cancel this
    public int winner() {
        //TODO maybe throw an exception if the game is not over?
        Player[] players = game.getPlayers();
        int min = 0;
        for (int i = 0; i < game.getPlayersCount(); i++) {
            if (players[i].getBoard().getTowersCount() < players[min].getBoard().getTowersCount())
                min = i;
            if (players[i].getBoard().getTowersCount() == players[min].getBoard().getTowersCount() &&
                    players[i].getBoard().countProfessors() > players[min].getBoard().countProfessors()) {
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

    private void changePlayer() {
        int nextPlayer = pickNextPlayer();
        game.changePlayer(nextPlayer);

        if (!game.getPlayer(game.getCurrentPlayer()).isOnline())
            changePlayer();
    }

    //TODO hopefully it will become less complex
    private int pickNextPlayer() {
        switch (game.getGamePhase()) {
            case PLANNING:
                return (game.getCurrentPlayer() + 1) % game.getPlayersCount();
            case ACTION_MOVE_STUDENTS, ACTION_MOVE_MOTHER_NATURE, ACTION_CHOOSE_CLOUD, ACTION_END:
                Player nextPlayer = Arrays.stream(game.getPlayers())
                        .sorted(Comparator.comparingInt(p -> p.getDiscardPileHead().value()))
                        .toList().get(game.getPlayedCount());
                //TODO what if two player had played the same card

                for (int i = 0; i < game.getPlayers().length; i++) {
                    if (game.getPlayer(i).getNickname().equals(nextPlayer.getNickname()))
                        return i;
                }
                break;
            default:
                return game.getCurrentPlayer();
        }
        return 0;
    }

    protected void playerHasEndedAction() {
        this.game.setGamePhase(this.pickNextPhase());
        if (this.game.getGamePhase() == GamePhase.ACTION_END) {
            this.game.setPlayedCount(game.getPlayedCount() + 1);
            if (!this.isTurnComplete()) {
                this.game.setGamePhase(ACTION_MOVE_STUDENTS);
                changePlayer();
            } else {
                try {
                    tableController.fillClouds();
                } catch (FullCloudException e) {
                    game.throwException(e);
                }
                this.game.setGamePhase(PLANNING);
            }
        }
        DataDumper.getInstance().saveGame(game);
        checkRoundGameOver();
    }

    public void checkImmediateGameOver() {

        //check if any player has build his last tower
        for (Player p : game.getPlayers()) {
            if (p.getBoard().getTowersCount() == 0) {
                game.callWin(p.getNickname());
            }
        }

        //check if only 3 island group remain on the table
        if (tableController.countIslands() == 3) {
            game.callWin(whoIsWinning());
        }

        //TODO what to do after the game has ended
        DataDumper.getInstance().removeGameFromMemory(game.getGameId());
    }

    public void checkRoundGameOver() {
        //check if bag is empty
        if (tableController.getBag().isEmpty())
            game.callWin(whoIsWinning());

        //check if any player has run out of assistant card
        for (Player p : game.getPlayers()) {
            if (p.isDeckEmpty())
                game.callWin(whoIsWinning());
        }

        //TODO what to do after the game has ended
        DataDumper.getInstance().removeGameFromMemory(game.getGameId());
    }

    private List<String> whoIsWinning() {
        List<String> winners = new ArrayList<>();

        //check number of tower left
        List<Player> playerOrderedByTower = Arrays.stream(game.getPlayers()).sorted(Comparator.comparingInt(p -> p.getBoard().getTowersCount())).toList();
        if (playerOrderedByTower.get(0).getBoard().getTowersCount() < playerOrderedByTower.get(1).getBoard().getTowersCount()) {
            winners.add(playerOrderedByTower.get(0).getNickname());
            return winners;
        }

        List<Player> playerOrderedByProfessors = playerOrderedByTower.subList(0, 2).stream().sorted(Comparator.comparingInt(p -> p.getBoard().countProfessors())).toList();
        if (playerOrderedByProfessors.get(0).getBoard().countProfessors() < playerOrderedByProfessors.get(1).getBoard().countProfessors()) {
            winners.add(playerOrderedByTower.get(1).getNickname());
            return winners;
        }


        // it arrives here only if there are 2 player with the same number of tower and professors
        winners.add(playerOrderedByTower.get(0).getNickname());
        winners.add(playerOrderedByTower.get(1).getNickname());
        return winners;
    }

    @Override
    public void onDisconnect() {
        synchronized (game) {
            //when one player disconnect, this will set every player to their status (online or offline)
            //it is redundant but it should be ok
            for (int i = 0; i < virtualViews.length; i++)
                game.getPlayer(i).setOnline(virtualViews[i].isOnline());
            if (game.howManyPlayersOnline() < 2 && game.isEnoughPlayerOnline())
                notEnoughOnline();
        }
    }

    //called only when, previously a disconnection, there are enough player online, and then there are not
    private void notEnoughOnline() {
        game.setEnoughPlayerOnline(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.callGameOverFromDisconnection();
            }
        }, 120000); //TODO parameterize this
    }

    //TODO make this as listener to virtualview or user
    public void onReconnect() {
        synchronized (game) {
            //when one player reconnect, this will set every player to their status (online or offline)
            //it is redundant but it should be ok
            for (int i = 0; i < virtualViews.length; i++)
                game.getPlayer(i).setOnline(virtualViews[i].isOnline());
            if (game.howManyPlayersOnline() >= 2 && !game.isEnoughPlayerOnline())
                enoughOnline();
        }
    }

    //called only when, previously a reconnection, there weren't enough player online, and then there are
    private void enoughOnline() {
        game.setEnoughPlayerOnline(true);
        timer.cancel();
        timer = new Timer();
    }

    @Override
    public void onMessageReceived(Message message) {
        try {
            checkMessage(message);
            switch (game.getGamePhase()) {
                case PLANNING:
                    planning(message);
                    break;
                case ACTION_MOVE_STUDENTS:
                    moveStudent(message);
                    break;
                case ACTION_MOVE_MOTHER_NATURE:
                    if (message.getType().equals(MessageType.ACTION_MOVE_MOTHER_NATURE)) {
                        tryMoveMotherNature((MoveMotherNatureMessage) message);
                    } else game.throwException(new IllegalActionException());
                    break;
                case ACTION_CHOOSE_CLOUD:
                    if (message.getType().equals(MessageType.ACTION_CHOOSE_CLOUD)) {
                        try {
                            pickStudentsFromCloud(((CloudMessage) message).getCloud().getUuid());
                        } catch (EmptyCloudException | IllegalActionException | WrongUUIDException e) {
                            game.throwException(e);
                        }
                    } else game.throwException(new IllegalActionException());
                    break;
                case ACTION_END:
                    //should not go here, the player doesn't do anything in this phase
                    game.throwException(new IllegalActionException());
                    break;
            }
        } catch (WrongPlayerException e) {
            game.throwException(e);
        }
    }
}
