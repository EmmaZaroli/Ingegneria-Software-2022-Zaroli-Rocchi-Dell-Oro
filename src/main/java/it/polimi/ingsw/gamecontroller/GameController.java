package it.polimi.ingsw.gamecontroller;

import it.polimi.ingsw.gamecontroller.exceptions.*;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.persistency.DataDumper;
import it.polimi.ingsw.persistency.GameNotFoundException;
import it.polimi.ingsw.servercontroller.MessagesHelper;
import it.polimi.ingsw.utils.ApplicationConstants;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.model.enums.GamePhase.*;
import static it.polimi.ingsw.utils.ApplicationConstants.MINIMUM_ONLINE_PLAYER;

/**
 * GameController
 */
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
    }

    /**
     *
     */
    public void init() {
        fillClouds();
        for (Player player : game.getPlayers()) {
            player.getBoard().addStudentsToEntrance(tableController.drawStudents());
        }
        //setting observers
        setObservers();
        DataDumper.getInstance().saveGame(game);
    }

    /**
     * Add the observers
     */
    public void setObservers() {
        for (VirtualView virtualView : virtualViews) {
            game.addObserver(virtualView);
            for (Player player : game.getPlayers()) {
                player.addObserver(virtualView);
                player.getBoard().addObserver(virtualView);
            }
            tableController.table.addObserver(virtualView);
        }
    }

    /**
     * Check if the message is from the current player
     * @param message the incoming message
     * @throws WrongPlayerException if the message is not from the current player
     */
    private void checkMessage(Message message) throws WrongPlayerException {
        if (!message.getNickname().equals(game.getPlayers()[game.getCurrentPlayer()].getNickname())) {
            throw new WrongPlayerException();
        }
    }

    /**
     * Try to move mother nature
     * @param message the incoming message
     */
    private void tryMoveMotherNature(MoveMotherNatureMessage message) {
        try {
            moveMotherNature(message.getSteps());
        } catch (NotAllowedMotherNatureMovementException | IllegalActionException e) {
            logger.log(Level.WARNING,"",e);
        }
    }

    @Override
    public void onMessageReceived(Message message) {
        synchronized (game){
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
                        } else  logger.log(Level.WARNING,"Illegal Action");
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
                        logger.log(Level.WARNING, "Game is over");
                        break;
                }
            } catch (WrongPlayerException e) {
                logger.log(Level.WARNING,"",e);
            }
        }

    }

    /**
     * The message arrives during the Planning phase, contains the assistantCard to play
     * @param message the incoming message
     */
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

    /**
     * Fill the cloudTiles on the table
     * If one or more than one cloudTiles have still students on them, an error is notified to
     * the players and the game will be closed
     */
    protected void fillClouds() {
        try {
            this.tableController.fillClouds();
        } catch (FullCloudException e) {
            logger.log(Level.SEVERE,"",e);
            game.throwException(e);
        }
    }

    /**
     * Gets the assistantCard index in the deck
     * @param assistantCard the assistantCard to play
     * @throws IllegalActionException if the player is trying to execute an action in the wrong game phase
     * @throws IllegalAssistantException if the assistantCard is equal to another played assistantCard
     * and the player has at least another assistantCard in the deck
     */
    private void playAssistant(AssistantCard assistantCard) throws IllegalActionException, IllegalAssistantException{
        int index;

        for(index = 0; index < game.getPlayer(game.getCurrentPlayer()).getAssistantDeck().size(); index++){
            if(game.getPlayer(game.getCurrentPlayer()).getAssistant(index).equals(assistantCard)){
                playAssistant(index);
                return;
            }
        }
    }

    /**
     * Plays the assistantCard
     * @param assistantIndex the assistantCard's index in the deck
     * @throws IllegalActionException if the player is trying to execute an action in the wrong game phase
     * @throws IllegalAssistantException if the assistantCard is equal to another played assistantCard
     *  and the player has at least another assistantCard in the deck
     */
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

    /**
     *
     * @param assistant the assistantCard
     * @return true if the player can play the assistantCard
     */
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

    /**
     * Called when a player's assistantCard has been successfully played
     * If all the players have played an assistantCard, the game phase is changed
     */
    protected void playerHasEndedPlanning() {
        do{
            this.game.setPlayedCount(game.getPlayedCount() + 1);
            if (this.isRoundComplete()) {
                this.game.setPlayedCount(0);
                this.game.setGamePhase(pickNextPhase());
            }
            changePlayer();
        }
        while (!game.getPlayer(game.getCurrentPlayer()).canPlayThisRound());
        DataDumper.getInstance().saveGame(game);
    }

    /**
     * Moves students on the schoolBoard or on the Island
     * @param message the incoming message
     */
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
                } catch (IllegalActionException| DiningRoomFullException e) {
                    logger.log(Level.WARNING,"",e);
                }
                break;
            default:
                logger.log(Level.WARNING,"Invalid Message");
        }
    }

    /**
     * Moves the PawnColor to the diningRoom
     * @param pawn the PawnColor to move
     * @throws IllegalActionException if the player is trying to execute an action in the wrong game phase
     * @throws DiningRoomFullException if the diningRoom for that PawnColor is full
     */
    public void moveStudentToDiningRoom(PawnColor pawn) throws IllegalActionException, DiningRoomFullException {
        if (this.game.getGamePhase() != ACTION_MOVE_STUDENTS) {
            throw new IllegalActionException();
        }
        if(this.game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(pawn) >= ApplicationConstants.STUDENTS_IN_DININGROOM){
            throw new DiningRoomFullException();
        }
        this.game.getCurrentPlayerBoard().moveStudentFromEntranceToDiningRoom(pawn);
        this.checkProfessorsStatus(pawn);
        this.movedPawn();
    }

    /**
     * Checks if the current player has more PawnColor than the other player
     * If so, the professor of that color is added to the schoolBoard of the current player
     * @param color the PawnColor just moved
     * @param player the other player
     */
    public void tryStealProfessor(PawnColor color, Player player) {
        if (!game.getCurrentPlayerSchoolBoard().isThereProfessor(color) &&
                player.getBoard().isThereProfessor(color) &&
                game.getCurrentPlayerSchoolBoard().getStudentsInDiningRoom(color)
                        > player.getBoard().getStudentsInDiningRoom(color)) {
            player.getBoard().removeProfessor(color);
            game.getCurrentPlayerSchoolBoard().addProfessor(color);
        }
    }

    /**
     * Moves the PawnColor to the islandCard with the same uuid as the one in the message
     * @param pawn the PawnColor to move
     * @param uuid the islandCard's uuid
     * @throws WrongUUIDException if on the table there aren't islandCards with that uuid
     */
    public void moveStudentOnIsland(PawnColor pawn, UUID uuid) throws WrongUUIDException {
        this.tableController.movePawnOnIsland(pawn, uuid);
        this.game.getCurrentPlayerSchoolBoard().removeStudentFromEntrance(pawn);
        this.movedPawn();
    }

    /**
     * Move mother nature
     * After that checks the influence on the islandCard where it landed
     * @param steps motherNature's steps
     * @throws NotAllowedMotherNatureMovementException if the number of motherNature steps isn't valid
     * @throws IllegalActionException if the player is trying to execute an action in the wrong game phase
     */
    public void moveMotherNature(int steps) throws NotAllowedMotherNatureMovementException, IllegalActionException {
        if (this.game.getGamePhase() != GamePhase.ACTION_MOVE_MOTHER_NATURE) {
            throw new IllegalActionException();
        }
        if (steps < 1 || steps > this.game.getPlayers()[game.getCurrentPlayer()].getDiscardPileHead().motherNatureMovement()) {
            throw new NotAllowedMotherNatureMovementException();
        }
        this.tableController.moveMotherNature(steps);

        this.checkInfluence();
        if(game.isGameOver())
            return;

        this.playerHasEndedAction();
    }

    /**
     * Checks the influence on the islandCard with motherNature
     * If every player has zero influence, no tower is built
     * If one player has more influence than the others, his tower is built
     * If two players have the same influence, and it's the max influence, no tower is built
     */
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

    /**
     * Checks if two players have the same max influence
     * @param player the player
     * @param influence the max influence
     * @return true if two players have the same max influence
     */
    private boolean isInfluenceDraw(Player player, int influence) {
        for (Player p : game.getPlayers()) {
            if (!p.equals(player) && influence == tableController
                    .countInfluenceOnIsland(p.getBoard().getProfessors(), p.getBoard().getTowerColor())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds the tower of the payer with max influence
     * @param player the player with max influence
     */
    private void buildTowers(Player player) {
        if (this.tableController.canBuildTower(player.getBoard().getTowerColor())) {
            Pair<Tower, Integer> result = this.tableController.buildTower(player.getBoard().getTowerColor());
            Arrays.stream(this.game.getPlayers())
                    .filter(x -> x.getBoard().getTowerColor() == result.first())
                    .forEach(x -> x.getBoard().addTowers(result.second()));
            for (int i = 0; i < result.second(); i++) {
                player.getBoard().removeTower();
                checkImmediateGameOver();
                if(game.isGameOver())
                    return;
            }
        }
    }

    /**
     * Takes the students from the cloudTile and adds them to the entrance of the current player's schoolBoard
     * @param uuid the cloudTile's uuid
     * @throws IllegalActionException if the player is trying to execute an action in the wrong game phase
     * @throws EmptyCloudException if the cloudTile is empty
     * @throws WrongUUIDException if on the table there aren't cloudTiles with the same uuid as the one passed as a parameter
     */
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

    /**
     * Checks if the current player has the highest number of students of the given color in his dining room
     * If so, proceeds to move the professor to the player's professor table
     * @param color the PawnColor just moved
     */
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

    //

    /**
     *
     * @param assistant the assistantCard
     * @return true if the assistant is different from every other assistants already played in this turn
     */
    private boolean isAssistantDifferentFromOthers(AssistantCard assistant) {
        for (Player p : game.getPlayers()) {
            if(p.getDiscardPileHead() != null && p.isFromActualTurn() && p.getDiscardPileHead().equals(assistant))
                    return false;
        }
        return true;
    }

    /**
     *
     * @return true if every player has finished the actionPhase
     */
    protected boolean isRoundComplete() {
        return this.game.getPlayedCount() == this.game.getPlayersCount();
    }

    /**
     * Sets that the current player has moved a PawnColor
     */
    void movedPawn() {
        if((game.getMovedPawns() + 1 ) != (game.getPlayersCount() + 1))
            game.movePawn();
        else {
            this.game.setMovedPawns(0);
            this.playerHasEndedAction();
        }
    }

    /**
     *
     * @return the new gamePhase
     */
    protected GamePhase pickNextPhase() {
        return switch (this.game.getGamePhase()) {
            case PLANNING -> ACTION_MOVE_STUDENTS;
            case ACTION_MOVE_STUDENTS -> GamePhase.ACTION_MOVE_MOTHER_NATURE;
            case ACTION_MOVE_MOTHER_NATURE -> GamePhase.ACTION_CHOOSE_CLOUD;
            case ACTION_CHOOSE_CLOUD, ACTION_END -> GamePhase.ACTION_END;
            case GAME_OVER -> GamePhase.GAME_OVER;
        };
    }

    /**
     * Change the current player
     */
    protected void changePlayer() {
        int nextPlayer = pickNextPlayer();
        game.changePlayer(nextPlayer);
    }

    private class PlayerOrderComparator implements Comparator<Player> {
        @Override
        public int compare(Player o1, Player o2) {
            if(o1.getDiscardPileHead() == null && o2.getDiscardPileHead() != null)
                return 0;
            if(o1.getDiscardPileHead() == null && o2.getDiscardPileHead() != null)
                return 1;
            if(o1.getDiscardPileHead() != null && o2.getDiscardPileHead() == null)
                return -1;
            if(!o1.isFromActualTurn() && !o2.isFromActualTurn())
                return 0;
            if(!o1.isFromActualTurn() && o2.isFromActualTurn())
                return 1;
            if(o1.isFromActualTurn() && !o2.isFromActualTurn())
                return -1;
            if(o1.getDiscardPileHead().equals(o2.getDiscardPileHead())){
                for(int i = game.getFirstPlayerInPlanning(); i != Math.floorMod(game.getFirstPlayerInPlanning() - 1, game.getPlayersCount()); i = Math.floorMod(i + 1, game.getPlayersCount())){
                    if(game.getPlayer(i).getNickname().equals(o1.getNickname()))
                        return -1;
                    if(game.getPlayer(i).getNickname().equals(o2.getNickname()))
                        return 1;
                }
            }
            return o1.getDiscardPileHead().value() - o2.getDiscardPileHead().value();
        }
    }

    /**
     * If it's the planning phase, the new player is the next in the list of players
     * If it's another phase, the next player is determined by the score on the assistantCard played during the planning phase
     * @return the index of the next current player
     */
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

    /**
     * Called when a player has finished the action phase
     * If there's still at least one player that has to play the action phase, the game phase
     * is set to ACTION_MOVE_STUDENTS and a new current player is picked
     * If every player has played, the endOfRound method is called
     */
    protected void playerHasEndedAction() {
        this.game.setGamePhase(this.pickNextPhase());
        if (this.game.getGamePhase() == GamePhase.ACTION_END) {
            do{
                this.game.setPlayedCount(game.getPlayedCount() + 1);
                if (!this.isRoundComplete()) {
                    this.game.setGamePhase(ACTION_MOVE_STUDENTS);
                } else {
                    endOfRound();
                    if(game.isGameOver())
                        return;
                }
                changePlayer();
            }
            while (!game.getPlayer(game.getCurrentPlayer()).canPlayThisRound() && this.game.getGamePhase() == GamePhase.ACTION_MOVE_STUDENTS);
            if(this.game.getGamePhase() == PLANNING && !game.getPlayer(game.getCurrentPlayer()).canPlayThisRound())
                playerHasEndedPlanning();
            DataDumper.getInstance().saveGame(game);
        }
    }

    /**
     * Ends of the round
     */
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

            for(Player player : game.getPlayers()){
                player.setFromActualTurn(false);
                if(player.isOnline())
                    player.setCanPlayThisRound(true);
            }


            int index;
            for (index = 0; index < game.getPlayers().length; index++) {
                if (game.getPlayer(index).getNickname().equals(playersSorted.get(0).getNickname())){
                    this.game.setFirstPlayerInPlanning(index);
                    return;
                }
            }
        }
    }

    /**
     * Checks if any player has build his last tower or
     * if only three islands group are left on the table
     */
    public void checkImmediateGameOver() {

        //check if any player has build his last tower
        for (Player p : game.getPlayers()) {
            if (p.getBoard().getTowersCount() == 0) {
                game.callWin(p.getNickname());
                return;
            }
        }

        //check if only 3 island group remain on the table
        if (tableController.countIslands() == 3) {
            game.callWin(whoIsWinning());
        }

    }

    /**
     * Checks if the bag is empty or if any player has run out of assistantCard
     */
    public void checkRoundGameOver() {
        //check if bag is empty
        if (tableController.getBag().isEmpty())
            game.callWin(whoIsWinning());

        //check if any player has run out of assistant card
        for (Player p : game.getPlayers()) {
            if (p.isDeckEmpty()){
                game.callWin(whoIsWinning());
                return;
            }

        }

    }

    /**
     * If a player has fewer towers than the other players, wins
     * If two players have the same number of towers, but one has more professors, wins
     * If two players have the same number of towers and professors, they tied
     * @return the nickname of the winner/winners
     */
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

        if (playerSortedByProfessors.get(0).getBoard().getProfessorsCount() < playerSortedByProfessors.get(1).getBoard().getProfessorsCount()) {
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
    public void onDisconnect(Object disconnected) {
        synchronized (game) {
            String disconnectedPlayer = "";
            for(VirtualView view : virtualViews){
                if(view.getClientHandler().isPresent()){
                    if(view.getClientHandler().get() == (disconnected))
                        disconnectedPlayer = view.getPlayerNickname();
                }
            }

            //when one player disconnect, this will set every player to their status (online or offline)
            //it is redundant, but it should be ok
            for (int i = 0; i < virtualViews.length; i++)
                game.getPlayer(i).setOnline(virtualViews[i].isOnline());

            if (game.howManyPlayersOnline() < MINIMUM_ONLINE_PLAYER && game.areEnoughPlayersOnline())
                notEnoughOnline();

            if(game.areEnoughPlayersOnline()) {
                if (disconnectedPlayer.equals(game.getPlayer(game.getCurrentPlayer()).getNickname())) {
                    try {
                        restoreLastSavedGame();
                        for (int i = 0; i < virtualViews.length; i++)
                            game.getPlayer(i).setOnline(virtualViews[i].isOnline());
                    } catch (GameNotFoundException e) {
                        logger.log(Level.SEVERE, MessagesHelper.ERROR_SAVE_NOT_FOUND, e);
                    }
                }
                for(Player p : game.getPlayers()) {
                    if (p.getNickname().equals(disconnectedPlayer))
                        p.setCanPlayThisRound(false);
                }
                if(disconnectedPlayer.equals(game.getPlayer(game.getCurrentPlayer()).getNickname())) {
                    for (VirtualView view : virtualViews)
                        view.getClientHandler().ifPresent(e -> e.sendMessage(new GameMessage(view.getPlayerNickname(), MessageType.GAME_RESTORING, this.game)));
                    if (game.getGamePhase() == PLANNING)
                        playerHasEndedPlanning();
                    else {
                        game.setGamePhase(ACTION_END);
                        playerHasEndedAction();
                    }
                }
            }
        }
    }

    /**
     * Restore the last game
     * @throws GameNotFoundException if the game wasn't found
     */
    private void restoreLastSavedGame() throws GameNotFoundException {
        Game savedGame = DataDumper.getInstance().getGame(game.getGameId());
        this.game.copyStatusFrom(savedGame);
        this.tableController.setTable(game.getTable());
        //setting observers
        for (VirtualView virtualView : virtualViews) {
            for (Player player : game.getPlayers()) {
                player.addObserver(virtualView);
                player.getBoard().addObserver(virtualView);
            }
            tableController.table.addObserver(virtualView);
        }
    }

    /**
     * called only when, previously a disconnection, there are enough player online, and then there are not
     */
    private void notEnoughOnline() {
        game.setEnoughPlayersOnline(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (game) {
                    game.callGameOverFromDisconnection();
                }
            }
        }, ApplicationConstants.DISCONNECTION_TIMER_NOT_ENOUGH_PLAYER);
    }

    /**
     *
     * @param reconnectedPlayer the nickname of the reconnected player
     */
    public void onReconnect(String reconnectedPlayer) {
        synchronized (game) {
            //when one player reconnect, this will set every player to their status (online or offline)
            //it is redundant, but it should be ok
            for (int i = 0; i < virtualViews.length; i++)
                game.getPlayer(i).setOnline(virtualViews[i].isOnline());

            if(game.howManyPlayersOnline() >= MINIMUM_ONLINE_PLAYER) {
                boolean areEnoughPlayersOnline = game.areEnoughPlayersOnline();
                if (!reconnectedPlayer.equals(game.getPlayer(game.getCurrentPlayer()).getNickname())
                && !game.getPlayer(game.getCurrentPlayer()).isOnline()) {
                    try {
                        restoreLastSavedGame();
                    } catch (GameNotFoundException e) {
                        logger.log(Level.SEVERE, MessagesHelper.ERROR_SAVE_NOT_FOUND, e);
                    }
                }
                if(game.areEnoughPlayersOnline() != areEnoughPlayersOnline)
                    game.setEnoughPlayersOnline(areEnoughPlayersOnline);
                for (int i = 0; i < virtualViews.length; i++)
                    game.getPlayer(i).setOnline(virtualViews[i].isOnline());
                for(Player p : game.getPlayers()) {
                    if(!p.isOnline())
                        p.setCanPlayThisRound(false);
                }
                if(!reconnectedPlayer.equals(game.getPlayer(game.getCurrentPlayer()).getNickname())
                && !game.getPlayer(game.getCurrentPlayer()).isOnline()) {
                    for (VirtualView view : virtualViews)
                        view.getClientHandler().ifPresent(e -> e.sendMessage(new GameMessage(view.getPlayerNickname(), MessageType.GAME_RESTORING, this.game)));
                    if (game.getGamePhase() == PLANNING)
                        playerHasEndedPlanning();
                    else {
                        game.setGamePhase(ACTION_END);
                        playerHasEndedAction();
                    }
                }
            }

            if (game.howManyPlayersOnline() >= MINIMUM_ONLINE_PLAYER && !game.areEnoughPlayersOnline())
                enoughOnline();

            if (game.howManyPlayersOnline() < MINIMUM_ONLINE_PLAYER && !game.areEnoughPlayersOnline())
                notEnoughOnline();

        }
    }

    /**
     * called only when, previously a reconnection, there weren't enough player online, and then there are
     */
    private void enoughOnline() {
        game.setEnoughPlayersOnline(true);
        timer.cancel();
        timer = new Timer();
    }


}
