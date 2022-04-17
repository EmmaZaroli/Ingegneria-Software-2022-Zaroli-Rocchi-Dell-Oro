package it.polimi.ingsw.applications;

import it.polimi.ingsw.applications.enums.NicknameStatus;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerBuilder;
import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;
import it.polimi.ingsw.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.persistency.DataDumper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;

    private final GameControllerBuilder normal2PlayersBuilder = new GameControllerBuilder();
    private final GameControllerBuilder normal3PlayersBuilder = new GameControllerBuilder();
    private final GameControllerBuilder expert2PlayersBuilder = new GameControllerBuilder();
    private final GameControllerBuilder expert3PlayersBuilder = new GameControllerBuilder();

    private final List<GameHandler> normal2PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> normal3PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> expert2PlayersRunningGames = new LinkedList<>();
    private final List<GameHandler> expert3PlayersRunningGames = new LinkedList<>();

    public static void main(String[] args) {
        //TODO start thread
        Server server = new Server(10000);
        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server(int port){
        this.port = port;
    }

    public void startServer() throws IOException {
        //It creates threads when necessary, otherwise it re-uses existing one when possible
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("Server ready");
        while (true){
            try{
                Socket socket = serverSocket.accept();
                executor.submit(new UserHandler(socket, this));
            }catch(IOException e){
                break; //In case the serverSocket gets closed
            }
        }
        executor.shutdown();
        serverSocket.close();
    }

    //TODO we need a runnable wrapper around GameController
    private List<GameController> loadSavedGames() {
        DataDumper dd = DataDumper.getInstance();
        return dd.getAllGames();
    }

    public void enqueueUser(User user, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        if (selectedGameMode == GameMode.NORMAL_MODE){
            if(selectedPlayersNumber == PlayersNumber.TWO)
                enqueueNormal2Players(user);
            else
                enqueueNormal3Players(user);
        }
        else{
            if(selectedPlayersNumber == PlayersNumber.TWO)
                enqueueExpert2Players(user);
            else
                enqueueExpert3Players(user);
        }
    }

    private void enqueueNormal2Players(User user) throws InvalidPlayerNumberException {
        synchronized (normal2PlayersBuilder){
            synchronized (normal2PlayersRunningGames){
                normal2PlayersBuilder.player(user.getNickname());

                if(normal2PlayersBuilder.isGameFull()){
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

    private void enqueueNormal3Players(User user){
        //TODO copy and modify enqueueNormal2Players
    }

    private void enqueueExpert2Players(User user){
        //TODO copy and modify enqueueNormal2Players
    }

    private void enqueueExpert3Players(User user){
        //TODO copy and modify enqueueNormal2Players
    }

    public NicknameStatus checkNicknameStatus(String nickname){
        NicknameStatus status = NicknameStatus.FREE;

        for(GameHandler gameHandler: normal2PlayersRunningGames){
            status = gameHandler.checkNicknameStatus(nickname);
            if(status != NicknameStatus.FREE)
                break;
        }

        return status;
    }

}
