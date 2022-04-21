package it.polimi.ingsw.applications;

import it.polimi.ingsw.applications.enums.NicknameStatus;
import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;
import it.polimi.ingsw.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.network.DisconnectionListener;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.Socket;

public class UserHandler implements Runnable, DisconnectionListener, MessageListener {
    private final Endpoint endpoint;
    private final Server server;

    public UserHandler(Socket socket, Server server) throws IOException {
        this.endpoint = new Endpoint(socket);
        this.endpoint.addDisconnectionListener(this);
        this.endpoint.addMessageListener(this);
        this.endpoint.startReceiving();
        this.server = server;
    }

    @Override
    public void onDisconnect() {
        //TODO dispatch to the controller
    }

    @Override
    public void onMessageReceived(Message message) {
        //TODO dispatch to the controller
    }

    @Override
    public void run() {
        //TODO ask for nickname
        String nickname = "";
        NicknameStatus nicknameStatus = server.checkNicknameStatus(nickname);

        if (nicknameStatus == NicknameStatus.FROM_CONNECTED_PLAYER) {
            //TODO re-ask for nickname
        } else {
            if (nicknameStatus == NicknameStatus.FROM_DISCONNECTED_PLAYER) {
                reconnectPlayer(nickname);
            }
        }

        User user = new User(nickname, endpoint);

        //TODO ask for game type (GameMode and PlayersNumber)
        GameMode selectedGameMode = GameMode.NORMAL_MODE;
        PlayersNumber selectedPlayersNumber = PlayersNumber.TWO;

        //TODO manage this exception
        try {
            enqueue(user, selectedGameMode, selectedPlayersNumber);
        } catch (InvalidPlayerNumberException e) {
            e.printStackTrace();
        }
    }

    private void enqueue(User user, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        server.enqueueUser(user, selectedGameMode, selectedPlayersNumber);
    }

    private void reconnectPlayer(String nickname /*or maybe User*/) {
        server.reconnectPlayer(nickname, endpoint);
    }
}
