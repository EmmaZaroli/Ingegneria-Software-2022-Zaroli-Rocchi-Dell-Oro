package it.polimi.ingsw.applications;

import it.polimi.ingsw.applications.enums.NicknameStatus;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerBuilder;
import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;
import it.polimi.ingsw.controller.exceptions.InvalidPlayerNumberException;
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

    private final GameControllerBuilder normal2PlayersBuilder = new GameControllerBuilder();
    private final GameControllerBuilder normal3PlayersBuilder = new GameControllerBuilder();
    private final GameControllerBuilder expert2PlayersBuilder = new GameControllerBuilder();
    private final GameControllerBuilder expert3PlayersBuilder = new GameControllerBuilder();

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
                //TODO
            }
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public Server(int port) {
        this.port = port;
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

    private List<GameController> loadSavedGames() {
        DataDumper dd = DataDumper.getInstance();
        return null;
        //TODO create a controller for every game, start the thread
        //return dd.getAllGames();
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
                normal2PlayersBuilder.player(user.getNickname());

                if (normal2PlayersBuilder.isGameFull()) {
                    //TODO have to pass virtualview and socket
                    GameController gameController = normal2PlayersBuilder.build();
                    GameHandler gameHandler = new GameHandler(gameController);
                    normal2PlayersRunningGames.add(gameHandler);
                    gameHandler.start();
                    normal2PlayersBuilder.reset();
                }
            }
        }
    }

    private void enqueueNormal3Players(User user) {
        //TODO copy and modify enqueueNormal2Players
    }

    private void enqueueExpert2Players(User user) {
        //TODO copy and modify enqueueNormal2Players
    }

    private void enqueueExpert3Players(User user) {
        //TODO copy and modify enqueueNormal2Players
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

    public void reconnectPlayer(String nickname, Endpoint endpoint) {
        Optional<GameHandler> gameHandler = getGameByPlayer(nickname);
        if (gameHandler.isPresent()) {
            GameHandler gh = gameHandler.get();
            //TODO
        }
    }
}
