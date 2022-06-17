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
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.model.enums.GamePhase.*;


public class GameController implements DisconnectionListener, MessageListener {
    protected Game game;
    protected TableController tableController;
    protected VirtualView[] virtualViews;
    private Timer timer = new Timer();
    private final Logger logger = Logger.getLogger(getClass().getName());

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
            logger.log(Level.WARNING,"",e);
        }
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
                    } else  logger.log(Level.WARNING,"Illegal Action");;
                    break;
                case ACTION_CHOOSE_CLOUD:
                    if (message.getType().equals(MessageType.ACTION_CHOOSE_CLOUD)) {
                        try {
                            pickStudentsFromCloud(((CloudMessage) message).getCloud().getUuid());
                        } catch (EmptyCloudException | IllegalActionException | WrongUUIDException e) {
                            logger.log(Level.WARNING,"",e);
                        }
                    } else  logger.log(Level.WARNING,"Illegal Action");
                    break;
                case ACTION_END:
                    //should not go here, the player doesn't do anything in this phase
                    logger.log(Level.WARNING,"Illegal Action");
                    break;

                case GAME_OVER:
                    //TODO do we want to throw an exception or just ignore it?
                    break;
            }
        } catch (WrongPlayerException e) {
            logger.log(Level.WARNING,"",e);
        }

    }

    private void planning(Message message) {
        if (message.getType().equals(MessageType.ACTION_PLAY_ASSISTANT)) {
            try {
                this.playAssistant(((AssistantPlayedMessage) message).getAssistantCard());
            } catch (IllegalActionException | IllegalAssistantException e) {
                logger.log(Level.WARNING,"",e);
            }
        } else {
            logger.log(Level.WARNING,"Invalid Message");
        }
    }

    protected void fillClouds() {
        try {
            this.tableController.fillClouds();
        } catch (FullCloudException e) {
            logger.log(Level.SEVERE,"",e);
            game.throwException(e);
        }
    }

    private void playAssistant(AssistantCard assistantCard) throws IllegalActionException, IllegalAssistantException{
        int index;

        for(index = 0; index < game.getPlayer(game.getCurrentPlayer()).getAssistantDeck().size(); index++){
            if(game.getPlayer(game.getCurrentPlayer()).getAssistant(index).equals(assistantCard)){
                playAssistant(index);
                return;
            }
        }
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
            if (isAssistantDifferentFromOthers(ac)) return false;
        }
        return true;
    }

    private void playerHasEndedPlanning() {
        do{
            this.game.setPlayedCount(game.getPlayedCount() + 1);
            if (!this.isRoundComplete()) {
                changePlayer();
            } else {
                this.game.setPlayedCount(0);
                this.game.setGamePhase(pickNextPhase());
                changePlayer();
            }
        }
        while (!game.getPlayer(game.getCurrentPlayer()).isOnline());


        //DataDumper.getInstance().saveGame(game);
    }

    private void moveStudent(Message message) {
        switch (message.getType()) {
            case ACTION_MOVE_STUDENTS_ON_ISLAND:
                try {
                    moveStudentOnIsland(((MoveStudentMessage) message).getStudentColor(), ((MoveStudentMessage) message).getIslandCard().getUuid());
                } catch (WrongUUIDException e) {
                    logger.log(Level.WARNING,"",e);
                }
                break;
            case ACTION_MOVE_STUDENTS_ON_BOARD:
                try {
                    moveStudentToDiningRoom(((MoveStudentMessage) message).getStudentColor());
                } catch (IllegalActionException e) {
                    logger.log(Level.WARNING,"",e);
                }
                break;
            default:
                logger.log(Level.WARNING,"Invalid Message");
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

        //if two player have same max influence, no tower is build
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
        for (int i = game.getFirstPlayerInPlanning(); i != game.getCurrentPlayer(); i = Math.floorMod(i + 1 , game.getPlayersCount())) {
            if(game.getPlayers()[i].getDiscardPileHead() != null)
                if (game.getPlayers()[i].getDiscardPileHead().equals(assistant))
                    return false;
        }
        return true;
    }

    protected boolean isRoundComplete() {
        return this.game.getPlayedCount() == this.game.getPlayersCount();
    }

    void movedPawn() {
        if((game.getMovedPawns() + 1 ) != (game.getPlayersCount() + 1))
            game.movePawn();
        else {
            this.game.setMovedPawns(0);
            this.playerHasEndedAction();
        }
    }

    protected GamePhase pickNextPhase() {
        return switch (this.game.getGamePhase()) {
            case PLANNING -> ACTION_MOVE_STUDENTS;
            case ACTION_MOVE_STUDENTS -> GamePhase.ACTION_MOVE_MOTHER_NATURE;
            case ACTION_MOVE_MOTHER_NATURE -> GamePhase.ACTION_CHOOSE_CLOUD;
            case ACTION_CHOOSE_CLOUD, ACTION_END -> GamePhase.ACTION_END;
            case GAME_OVER -> GamePhase.GAME_OVER;
        };
    }

    protected void changePlayer() {
        int nextPlayer = pickNextPlayer();
        game.changePlayer(nextPlayer);
    }

    private class PlayerOrderComparator implements Comparator<Player> {
        @Override
        public int compare(Player o1, Player o2) {
            if(o1.getDiscardPileHead() == null && o2.getDiscardPileHead() == null)
                return 0;
            if(o1.getDiscardPileHead() == null && o2.getDiscardPileHead() != null)
                return 1;
            if(o1.getDiscardPileHead() != null && o2.getDiscardPileHead() == null)
                return -1;
            if(o1.getDiscardPileHead().equals(o2.getDiscardPileHead())){
                for(int i = game.getFirstPlayerInPlanning(); i != Math.floorMod(game.getFirstPlayerInPlanning() - 1, game.getPlayersCount()); Math.floorMod(i + 1, game.getPlayersCount())){
                    if(game.getPlayer(i).getNickname().equals(o1.getNickname()))
                        return -1;
                    if(game.getPlayer(i).getNickname().equals(o2.getNickname()))
                        return 1;
                }
            }
            return o1.getDiscardPileHead().value() - o2.getDiscardPileHead().value();
        }
    }

    private int pickNextPlayer() {
        switch (game.getGamePhase()) {
            case PLANNING:
                return Math.floorMod(game.getFirstPlayerInPlanning() + game.getPlayedCount(), game.getPlayersCount());
            case ACTION_MOVE_STUDENTS, ACTION_MOVE_MOTHER_NATURE, ACTION_CHOOSE_CLOUD, ACTION_END:
                List<Player> playersSorted = Arrays.stream(game.getPlayers())
                        .sorted(new PlayerOrderComparator())
                        .toList();

                Player nextPlayer = playersSorted.get(game.getPlayedCount());

                for (int i = 0; i < game.getPlayers().length; i++) {
                    if (game.getPlayer(i).getNickname().equals(nextPlayer.getNickname()))
                        return i;
                }
                break;
        }
        return game.getCurrentPlayer();
    }

    protected void playerHasEndedAction() {
        this.game.setGamePhase(this.pickNextPhase());
        if (this.game.getGamePhase() == GamePhase.ACTION_END) {
            do{
                this.game.setPlayedCount(game.getPlayedCount() + 1);
                if (!this.isRoundComplete()) {
                    this.game.setGamePhase(ACTION_MOVE_STUDENTS);
                } else {
                    endOfRound();
                }
                changePlayer();
            }
            while (!game.getPlayer(game.getCurrentPlayer()).isOnline());
        }
        //TODO move this
        //DataDumper.getInstance().saveGame(game);
    }

    public void endOfRound(){
        if(!game.isGameOver())
            checkRoundGameOver();
        if(!game.isGameOver()){
            this.fillClouds();
            this.game.setPlayedCount(0);
            this.game.setGamePhase(PLANNING);

            List<Player> playersSorted = Arrays.stream(game.getPlayers())
                    .sorted(new PlayerOrderComparator())
                    .toList();

            int index;
            for (index = 0; index < game.getPlayers().length; index++) {
                if (game.getPlayer(index).getNickname().equals(playersSorted.get(0).getNickname())){
                    this.game.setFirstPlayerInPlanning(index);
                    return;
                }
            }
        }
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

        //DataDumper.getInstance().removeGameFromMemory(game.getGameId());
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

        //DataDumper.getInstance().removeGameFromMemory(game.getGameId());
    }

    private List<String> whoIsWinning() {
        List<String> winners = new ArrayList<>();

        //check number of tower left
        List<Player> playerSortedByTower = Arrays.stream(game.getPlayers()).
                sorted(Comparator.comparingInt(p -> p.getBoard().getTowersCount())).toList();
        if (playerSortedByTower.get(0).getBoard().getTowersCount() < playerSortedByTower.get(1).getBoard().getTowersCount()) {
            winners.add(playerSortedByTower.get(0).getNickname());
            return winners;
        }

        List<Player> playerSortedByProfessors;
        if(game.getParameters().getPlayersNumber().getPlayersNumber() == 3
                && playerSortedByTower.get(1).getBoard().getTowersCount() == playerSortedByTower.get(2).getBoard().getTowersCount())
            playerSortedByProfessors = playerSortedByTower.subList(0, 3);
        else
            playerSortedByProfessors = playerSortedByTower.subList(0, 2);
        playerSortedByProfessors.stream().
                sorted(Comparator.comparing(Player::getProfessorsCount).reversed()).toList();

        if (playerSortedByProfessors.get(0).getBoard().getProfessorsCount() > playerSortedByProfessors.get(1).getBoard().getProfessorsCount()) {
            winners.add(playerSortedByTower.get(1).getNickname());
            return winners;
        }


        // it arrives here only if there are 2 or 3 player with the same number of tower and professors
        winners.add(playerSortedByTower.get(0).getNickname());
        winners.add(playerSortedByTower.get(1).getNickname());
        if(game.getParameters().getPlayersNumber().getPlayersNumber() == 3
                && playerSortedByProfessors.get(1).getBoard().getProfessorsCount() == playerSortedByProfessors.get(2).getBoard().getProfessorsCount())
            winners.add(playerSortedByTower.get(2).getNickname());
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
        }, 5000); //TODO parameterize this
    }

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


}
