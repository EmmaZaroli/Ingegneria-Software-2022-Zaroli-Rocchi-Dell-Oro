package it.polimi.ingsw.applications;

import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.gamecontroller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.persistency.DataDumper;
import it.polimi.ingsw.servercontroller.*;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main server class
 */
public class Server implements GameEndingListener{
    private final int port;

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private final GameHandlerBuilder normal2PlayersBuilder = new GameHandlerBuilder();
    private final GameHandlerBuilder normal3PlayersBuilder = new GameHandlerBuilder();
    private final GameHandlerBuilder expert2PlayersBuilder = new GameHandlerBuilder();
    private final GameHandlerBuilder expert3PlayersBuilder = new GameHandlerBuilder();

    private final List<UserHandler> userHandlers = new LinkedList<>();

    private final List<GameHandler> normal2PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> normal3PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> expert2PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> expert3PlayersRunningGames = new LinkedList<>();

    private final Map<String, User> allUsers = new HashMap<>();

    /**
     * Default constructor
     * Checks if the port passed as parameter is valid. If it is, it starts the server
     * @param args the port on which to run the server
     */
    public static void main(String[] args) {
        if (args.length < 1 || !args[0].equals("--port") || !isNumeric(args[1])) {
            logger.log(Level.SEVERE, MessagesHelper.NO_PORT);
        } else {
            int port = Integer.parseInt(args[1]);

            Server server = new Server(port);
            try {
                server.startServer();
            } catch (IOException e) {
                logger.log(Level.SEVERE, MessagesHelper.ERROR_STARTING_SERVER, e);
            }
        }
    }

    /**
     * Check if the string passed as a parameter is a number
     * @param str the string
     * @return true if the string is a number
     */
    private static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    /**
     * Server constructor
     * Loads the previously saved games
     * @param port the port on which to run the server
     */
    public Server(int port) {
        this.port = port;
        this.normal2PlayersBuilder.gameMode(GameMode.NORMAL_MODE);
        this.normal2PlayersBuilder.playersNumber(PlayersNumber.TWO);
        this.normal3PlayersBuilder.gameMode(GameMode.NORMAL_MODE);
        this.normal3PlayersBuilder.playersNumber(PlayersNumber.THREE);
        this.expert2PlayersBuilder.gameMode(GameMode.EXPERT_MODE);
        this.expert2PlayersBuilder.playersNumber(PlayersNumber.TWO);
        this.expert3PlayersBuilder.gameMode(GameMode.EXPERT_MODE);
        this.expert3PlayersBuilder.playersNumber(PlayersNumber.THREE);

        this.loadSavedGames();
    }

    /**
     * Starts the server
     * Creates a server socket, bound to the specified port
     * For every connection accepted, it creates a new UserHandler to which it passes the accepted socket and a reference to this instance
     * @throws IOException signals that an I/O exception of some sort has occurred
     */
    public void startServer() throws IOException {
        //It creates threads when necessary, otherwise it re-uses existing one when possible
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.log(Level.SEVERE, MessagesHelper.PORT_NOT_AVAILABLE);
            return;
        }
        logger.log(Level.INFO, MessagesHelper.SERVER_STARTED);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                //executor.submit(new UserHandler(socket, this));
                UserHandler userHandler = new UserHandler(socket, this);
                userHandlers.add(userHandler);
                userHandler.start();

            } catch (IOException e) {
                break; //In case the serverSocket gets closed
            }
        }
        executor.shutdown();
        serverSocket.close();
    }

    /**
     * Loads previously saved games
     */
    private void loadSavedGames() {
        DataDumper dd = DataDumper.getInstance();
        List<Game> games = dd.getAllGames();
        for (Game game : games) {
            List<User> users = new LinkedList<>();
            for (int i = 0; i < game.getPlayers().length; i++) {
                game.getPlayer(i).setOnline(false);
                users.add(new User(game.getPlayer(i).getNickname()));
            }
            game.setEnoughPlayersOnline(false);
            GameParameters parameters = game.getParameters();
            try {
                if (parameters.getGameMode() == GameMode.NORMAL_MODE) {
                    if (parameters.getPlayersNumber() == PlayersNumber.TWO)
                        loadGame(game, users, normal2PlayersBuilder, normal2PlayersRunningGames);
                    else
                        loadGame(game, users, normal3PlayersBuilder, normal3PlayersRunningGames);
                } else {
                    if (parameters.getPlayersNumber() == PlayersNumber.TWO)
                        loadGame(game, users, expert2PlayersBuilder, expert2PlayersRunningGames);
                    else
                        loadGame(game, users, expert3PlayersBuilder, expert3PlayersRunningGames);
                }
            } catch (InvalidPlayerNumberException e) {
                logger.log(Level.SEVERE, MessagesHelper.ERROR_CREATING_GAME, e);
            }
        }
    }

    /**
     * Creates a gameHandler to which it passes the previous game
     */
    private void loadGame(Game game, List<User> users, GameHandlerBuilder builder, List<GameHandler> runningGames) throws InvalidPlayerNumberException {
        for (User user : users) {
            builder.player(user);
            addUser(user);
        }
        GameHandler gameHandler = builder.load(game);
        gameHandler.addGameEndingListener(this);
        runningGames.add(gameHandler);
        gameHandler.start();
        builder.reset();
    }

    public synchronized void addGameStartingListener(GameReadyListener l, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) {
        if (selectedGameMode == GameMode.NORMAL_MODE) {
            if (selectedPlayersNumber == PlayersNumber.TWO)
                normal2PlayersBuilder.addGameStartingListener(l);
            else
                normal3PlayersBuilder.addGameStartingListener(l);
        } else {
            if (selectedPlayersNumber == PlayersNumber.TWO)
                expert2PlayersBuilder.addGameStartingListener(l);
            else
                expert3PlayersBuilder.addGameStartingListener(l);
        }
    }

    public synchronized void removeGameStartingListener(GameReadyListener l) {
        normal2PlayersBuilder.removeGameStartingListener(l);
        normal3PlayersBuilder.removeGameStartingListener(l);
        expert2PlayersBuilder.removeGameStartingListener(l);
        expert3PlayersBuilder.removeGameStartingListener(l);
    }

    //User must be already present in allUser list

    /**
     * @param nickname the user's nickname
     * @param selectedGameMode the chosen gameMode
     * @param selectedPlayersNumber the selected number of players
     * @param l the gameReadyListener
     * @throws InvalidPlayerNumberException Signals that in GameControllerBuilder the specified PlayerCount does not match the actual number of player passed to the builder
     */
    public synchronized void enqueueUser(String nickname, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber, GameReadyListener l) throws InvalidPlayerNumberException {
        addGameStartingListener(l, selectedGameMode, selectedPlayersNumber);
        enqueueUser(nickname, selectedGameMode, selectedPlayersNumber);
    }

    /**
     * Enqueue the user to the lobby corresponding to the gameMode and number of players previously selected
     * The user must be already present in the allUser list
     * @param nickname the player's nickname
     * @param selectedGameMode the selected gameMode
     * @param selectedPlayersNumber the selected playersNumber
     * @throws InvalidPlayerNumberException Signals that in GameControllerBuilder the specified PlayerCount does not match the actual number of player passed to the builder
     */
    public synchronized void enqueueUser(String nickname, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        Optional<User> optionalUser = getUser(nickname);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (selectedGameMode == GameMode.NORMAL_MODE) {
                if (selectedPlayersNumber == PlayersNumber.TWO)
                    enqueueUser(user, normal2PlayersBuilder, normal2PlayersRunningGames);
                else
                    enqueueUser(user, normal3PlayersBuilder, normal3PlayersRunningGames);
            } else {
                if (selectedPlayersNumber == PlayersNumber.TWO)
                    enqueueUser(user, expert2PlayersBuilder, expert2PlayersRunningGames);
                else
                    enqueueUser(user, expert3PlayersBuilder, expert3PlayersRunningGames);
            }
        }
    }

    /**
     * Adds the player to the lobby corresponding to the gameMode and number of players previously selected
     * If the lobby is full, a new gameHandler is created and added to the list of running games. The lobby then is emptied
     * @param user the user
     * @param builder the gameHandlerBuilder corresponding to the gameMode and number of players previously selected
     * @param runningGames the list of running games
     * @throws InvalidPlayerNumberException Signals that in GameControllerBuilder the specified PlayerCount does not match the actual number of player passed to the builder
     */
    private void enqueueUser(User user, GameHandlerBuilder builder, List<GameHandler> runningGames) throws InvalidPlayerNumberException {
        synchronized (builder) {
            synchronized (runningGames) {
                builder.player(user);

                if (builder.isGameFull()) {
                    GameHandler gameHandler = builder.build();
                    gameHandler.addGameEndingListener(this);
                    runningGames.add(gameHandler);
                    gameHandler.start();
                    builder.reset();
                }
            }
        }
    }

    /**
     * Checks if the chosen nickname is already used or if belongs to a previously disconnected player
     * @param nickname the chosen nickname
     * @return the nicknameStatus
     */
    public NicknameStatus checkNicknameStatus(String nickname) {
        Optional<User> optionalUser = getUser(nickname);
        if (optionalUser.isEmpty())
            return NicknameStatus.FREE;
        else {
            return optionalUser.get().isOnline()
                    ? NicknameStatus.FROM_CONNECTED_PLAYER
                    : NicknameStatus.FROM_DISCONNECTED_PLAYER;
        }
    }

    /**
     * Returns the gameHandler in which appears the player with the wanted nickname
     * @param nickname the player's nickname
     * @return Optional<GameHandler>
     */
    private Optional<GameHandler> getGameByPlayer(String nickname) {
        for (GameHandler gameHandler : normal2PlayersRunningGames) {
            if (gameHandler.containsUser(nickname))
                return Optional.of(gameHandler);
        }
        for (GameHandler gameHandler : normal3PlayersRunningGames) {
            if (gameHandler.containsUser(nickname))
                return Optional.of(gameHandler);
        }
        for (GameHandler gameHandler : expert2PlayersRunningGames) {
            if (gameHandler.containsUser(nickname))
                return Optional.of(gameHandler);
        }
        for (GameHandler gameHandler : expert3PlayersRunningGames) {
            if (gameHandler.containsUser(nickname))
                return Optional.of(gameHandler);
        }
        return Optional.empty();
    }

    /**
     * Return the gameHandler associated with the game having the seek uuid
     * @param uuid the uuid
     * @return Optional<GameHandler>
     */
    private Optional<GameHandler> getGame(UUID uuid) {
        synchronized (normal2PlayersRunningGames){
            for (GameHandler gameHandler : normal2PlayersRunningGames) {
                if (gameHandler.getGameId().equals(uuid))
                    return Optional.of(gameHandler);
            }
        }
        synchronized (normal3PlayersRunningGames){
            for (GameHandler gameHandler : normal3PlayersRunningGames) {
                if (gameHandler.getGameId().equals(uuid))
                    return Optional.of(gameHandler);
            }
        }
        synchronized (expert2PlayersRunningGames){
            for (GameHandler gameHandler : expert2PlayersRunningGames) {
                if (gameHandler.getGameId().equals(uuid))
                    return Optional.of(gameHandler);
            }
        }
        synchronized (expert3PlayersRunningGames){
            for (GameHandler gameHandler : expert3PlayersRunningGames) {
                if (gameHandler.getGameId().equals(uuid))
                    return Optional.of(gameHandler);
            }
        }
        return Optional.empty();
    }

    /**
     * Remove the gameHandler associated with the game that has the uuid passed as parameter
     * If the uuid is not present, it does nothing
     * @param uuid the game uuid
     */
    private void removeGame(UUID uuid){
        synchronized (normal2PlayersRunningGames){
            normal2PlayersRunningGames.removeIf(gameHandler -> gameHandler.getGameId().equals(uuid));
        }
        synchronized (normal3PlayersRunningGames){
            normal3PlayersRunningGames.removeIf(gameHandler -> gameHandler.getGameId().equals(uuid));
        }
        synchronized (expert2PlayersRunningGames){
            expert2PlayersRunningGames.removeIf(gameHandler -> gameHandler.getGameId().equals(uuid));
        }
        synchronized (expert3PlayersRunningGames){
            expert3PlayersRunningGames.removeIf(gameHandler -> gameHandler.getGameId().equals(uuid));
        }
    }

    public synchronized void reconnectUser(String nickname, Endpoint endpoint) {
        Optional<GameHandler> gameHandler = getGameByPlayer(nickname);
        if (gameHandler.isPresent()) {
            GameHandler gh = gameHandler.get();
            gh.reconnectPlayer(nickname, endpoint);
        }
    }

    /**
     * Adds a user to the map containing the players
     * @param user the user to add
     */
    public synchronized void addUser(User user) {
        synchronized (allUsers) {
            allUsers.put(user.getNickname(), user);
        }
    }

    /**
     * Check if a chosen nickname belongs already to another user
     * @param nickname the nickname to verify
     * @return Optional<User> if the nickname chosen belongs already to another user
     */
    public Optional<User> getUser(String nickname) {
        synchronized (allUsers) {
            return Optional.ofNullable(allUsers.get(nickname));
        }
    }

    /**
     * Tries to remove a user
     * The user must not be in an active game
     * @param nickname the user's nickname
     * @return true if the removal is successful, false if not (user does not exist or is in an active game)
     */
    public synchronized boolean removeUser(String nickname) {
        Optional<User> user = getUser(nickname);
        if (user.isEmpty() || getGameByPlayer(nickname).isPresent())
            return false;
        synchronized (allUsers){
            allUsers.remove(user.get().getNickname());
        }
        synchronized (normal2PlayersBuilder){
            normal2PlayersBuilder.removePlayer(user.get());
        }
        synchronized (normal3PlayersBuilder){
            normal3PlayersBuilder.removePlayer(user.get());
        }
        synchronized (expert2PlayersBuilder){
            expert2PlayersBuilder.removePlayer(user.get());
        }
        synchronized (expert3PlayersBuilder){
            expert3PlayersBuilder.removePlayer(user.get());
        }
        return true;
    }

    /**
     * Removes from the userHandlers list the userHandler
     * @param userHandler the userHandler to remove
     */
    public void removeUserHandler(UserHandler userHandler) {
        synchronized (userHandlers){
            userHandlers.remove(userHandler);
        }
    }

    /**
     * If a game has ended, the corresponding gameHandler is remove from the list of running games
     * If the users are still online, they are added to a new userHandler.
     * If they are not, they are removed from the list of players.
     * Finally, the game is removed from the saved games
     * @param uuid the game uuid
     */
    @Override
    public void onGameEnding(UUID uuid) {
        Optional<GameHandler> optionalGameHandler = getGame(uuid);
        removeGame(uuid);
        if(optionalGameHandler.isPresent()){
            GameHandler gameHandler = optionalGameHandler.get();
            gameHandler.removeGameEndingListener(this);
            for(User user : gameHandler.getUsers()){
                if(user.isOnline()){
                    UserHandler userHandler = new UserHandler(user, this);
                    synchronized (userHandlers){
                        userHandlers.add(userHandler);
                    }
                }
                else{
                    removeUser(user.getNickname());
                }
            }

        }
        DataDumper.getInstance().removeGameFromMemory(uuid);
    }
}
