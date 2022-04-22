package it.polimi.ingsw.applications;

import it.polimi.ingsw.applications.enums.NicknameStatus;
import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;
import it.polimi.ingsw.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.persistency.DataDumper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO Think about splitting gameController and controller
//TODO applications should contain only Client and Server
public class Server {
    private final int port;

    private static Logger logger = Logger.getLogger(Server.class.getName());

    private final GameHandlerBuilder normal2PlayersBuilder = new GameHandlerBuilder();
    private final GameHandlerBuilder normal3PlayersBuilder = new GameHandlerBuilder();
    private final GameHandlerBuilder expert2PlayersBuilder = new GameHandlerBuilder();
    private final GameHandlerBuilder expert3PlayersBuilder = new GameHandlerBuilder();

    private final List<GameHandler> normal2PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> normal3PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> expert2PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> expert3PlayersRunningGames = new LinkedList<>();

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
                executor.submit(new UserHandler(socket, this));
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
        //TODO create a controller for every game, start the thread
    }

    public void enqueueUser(User user, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        if (selectedGameMode == GameMode.NORMAL_MODE) {
            if (selectedPlayersNumber == PlayersNumber.TWO)
                enqueueNormal2Players(user);
            else
                enqueueNormal3Players(user);
        } else {
            if (selectedPlayersNumber == PlayersNumber.TWO)
                enqueueExpert2Players(user);
            else
                enqueueExpert3Players(user);
        }
    }

    private void enqueueNormal2Players(User user) throws InvalidPlayerNumberException {
        synchronized (normal2PlayersBuilder) {
            synchronized (normal2PlayersRunningGames) {
                normal2PlayersBuilder.player(user);

                if (normal2PlayersBuilder.isGameFull()) {
                    GameHandler gameHandler = normal2PlayersBuilder.build();
                    normal2PlayersRunningGames.add(gameHandler);
                    gameHandler.start();
                    normal2PlayersBuilder.reset();
                }
            }
        }
    }

    private void enqueueNormal3Players(User user) throws InvalidPlayerNumberException {
        synchronized (normal3PlayersBuilder) {
            synchronized (normal3PlayersRunningGames) {
                normal3PlayersBuilder.player(user);

                if (normal3PlayersBuilder.isGameFull()) {
                    GameHandler gameHandler = normal3PlayersBuilder.build();
                    normal3PlayersRunningGames.add(gameHandler);
                    gameHandler.start();
                    normal3PlayersBuilder.reset();
                }
            }
        }
    }

    private void enqueueExpert2Players(User user) throws InvalidPlayerNumberException {
        synchronized (expert2PlayersBuilder) {
            synchronized (expert2PlayersRunningGames) {
                expert2PlayersBuilder.player(user);

                if (expert2PlayersBuilder.isGameFull()) {
                    GameHandler gameHandler = expert2PlayersBuilder.build();
                    expert2PlayersRunningGames.add(gameHandler);
                    gameHandler.start();
                    expert2PlayersBuilder.reset();
                }
            }
        }
    }

    private void enqueueExpert3Players(User user) throws InvalidPlayerNumberException {
        synchronized (expert3PlayersBuilder) {
            synchronized (expert3PlayersRunningGames) {
                expert3PlayersBuilder.player(user);

                if (expert3PlayersBuilder.isGameFull()) {
                    GameHandler gameHandler = expert3PlayersBuilder.build();
                    expert3PlayersRunningGames.add(gameHandler);
                    gameHandler.start();
                    expert3PlayersBuilder.reset();
                }
            }
        }
    }

    public NicknameStatus checkNicknameStatus(String nickname) {
        NicknameStatus status = NicknameStatus.FREE;

        for (GameHandler gameHandler : normal2PlayersRunningGames) {
            status = gameHandler.checkNicknameStatus(nickname);
            if (status != NicknameStatus.FREE)
                break;
        }

        return status;
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

    //TODO two player may try to connect with the same username
    public void reconnectPlayer(String nickname, Endpoint endpoint) {
        Optional<GameHandler> gameHandler = getGameByPlayer(nickname);
        if (gameHandler.isPresent()) {
            GameHandler gh = gameHandler.get();
            gh.reconnectPlayer(nickname, endpoint);
        }
    }
}
