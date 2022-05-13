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

public class Server {
    private final int port;

    private static Logger logger = Logger.getLogger(Server.class.getName());

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

    private static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

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

        //this.loadSavedGames();
    }

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

    private void loadSavedGames() {
        DataDumper dd = DataDumper.getInstance();
        List<Game> games = dd.getAllGames();
        List<User> users = new LinkedList<>();
        for (Game game : games) {
            for (int i = 0; i < game.getPlayers().length; i++) {
                game.getPlayer(i).setOnline(false);
                users.add(new User(game.getPlayer(i).getNickname()));
            }
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

    private void loadGame(Game game, List<User> users, GameHandlerBuilder builder, List<GameHandler> runningGames) throws InvalidPlayerNumberException {
        for (int i = 0; i < users.size(); i++) {
            builder.player(users.get(i));
            addUser(users.get(i));
        }
        GameHandler gameHandler = builder.build();
        runningGames.add(gameHandler);
        gameHandler.start();
        builder.reset();
    }

    public synchronized void addGameReadyListener(GameReadyListener l, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) {
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
    public synchronized void enqueueUser(String nickname, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber, GameReadyListener l) throws InvalidPlayerNumberException {
        addGameReadyListener(l, selectedGameMode, selectedPlayersNumber);
        enqueueUser(nickname, selectedGameMode, selectedPlayersNumber);
    }

    //User must be already present in allUser list
    public synchronized void enqueueUser(String nickname, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        User user = getUser(nickname).get(); //TODO this could return null
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

    private void enqueueUser(User user, GameHandlerBuilder builder, List<GameHandler> runningGames) throws InvalidPlayerNumberException {
        synchronized (builder) {
            synchronized (runningGames) {
                builder.player(user);

                if (builder.isGameFull()) {
                    GameHandler gameHandler = builder.build();
                    runningGames.add(gameHandler);
                    gameHandler.start();
                    builder.reset();
                }
            }
        }
    }

    public NicknameStatus checkNicknameStatus(String nickname) {
        if (!containUser(nickname))
            return NicknameStatus.FREE;
        else {
            return getUser(nickname).get().isOnline() ? NicknameStatus.FROM_CONNECTED_PLAYER : NicknameStatus.FROM_DISCONNECTED_PLAYER;
        }
    }

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

    public synchronized void reconnectUser(String nickname, Endpoint endpoint) {
        Optional<GameHandler> gameHandler = getGameByPlayer(nickname);
        if (gameHandler.isPresent()) {
            GameHandler gh = gameHandler.get();
            gh.reconnectPlayer(nickname, endpoint);
            //TODO send whole game status
        }
    }

    public synchronized void addUser(User user) {
        synchronized (allUsers) {
            allUsers.put(user.getNickname(), user);
        }
    }

    public Optional<User> getUser(String nickname) {
        synchronized (allUsers) {
            return Optional.ofNullable(allUsers.get(nickname));
        }
    }

    public boolean containUser(String nickname) {
        synchronized (allUsers) {
            return allUsers.containsKey(nickname);
        }
    }

    //try to remove a user
    //user must not be in an active game
    //return true if the removal is succesfull, false if not (user does not exist or is in an active game)
    public synchronized boolean removeUser(String nickname) {
        Optional<User> user = getUser(nickname);
        if (user.isEmpty() || !getGameByPlayer(nickname).isEmpty())
            return false;
        allUsers.remove(user.get().getNickname());
        normal2PlayersBuilder.removePlayer(user.get());
        normal3PlayersBuilder.removePlayer(user.get());
        expert2PlayersBuilder.removePlayer(user.get());
        expert3PlayersBuilder.removePlayer(user.get());
        return true;
    }

    public void removeUserHandler(UserHandler userHandler){
        userHandlers.remove(userHandler);
    }
}
