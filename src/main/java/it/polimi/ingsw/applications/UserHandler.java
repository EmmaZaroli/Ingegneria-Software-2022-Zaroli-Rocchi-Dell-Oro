package it.polimi.ingsw.applications;

import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Endpoint;

import java.io.IOException;
import java.net.Socket;

public class UserHandler implements Runnable{
    private Endpoint endpoint;
    private Server server;

    public UserHandler(Socket socket, Server server) throws IOException {
        this.endpoint = new Endpoint(socket);
        this.server = server;
    }

    @Override
    public void run() {
        //TODO ask for nickname
        String nickname = new String("");
        //TODO check if nickname is from disconnected player
        //TODO check if nickname is from a player already connected
        User user = new User(nickname, endpoint);

        //TODO ask for game type (GameMode and PlayersNumber)
        GameMode selectedGameMode = GameMode.NORMAL_MODE;
        PlayersNumber selectedPlayersNumber = PlayersNumber.TWO;

        enqueue(user, selectedGameMode, selectedPlayersNumber);
    }

    private void enqueue(User User, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber){
        //server.enqueue(user, selectedGameMode, selectedPlayersNumber)
    }
}
