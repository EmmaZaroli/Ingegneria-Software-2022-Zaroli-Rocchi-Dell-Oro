package it.polimi.ingsw.applications;

import it.polimi.ingsw.applications.enums.NicknameStatus;
import it.polimi.ingsw.controller.enums.GameMode;
import it.polimi.ingsw.controller.enums.PlayersNumber;
import it.polimi.ingsw.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.network.DisconnectionListener;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.messages.GametypeResponseMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.NicknameResponseMessage;

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
        String nickname = "";
        NicknameStatus nicknameStatus;
        do{
            endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FROM_CONNECTED_PLAYER));
            //TODO listen for user to propose a nickname
            nicknameStatus = server.checkNicknameStatus(nickname);
        }while (nicknameStatus == NicknameStatus.FROM_CONNECTED_PLAYER);

        if (nicknameStatus == NicknameStatus.FROM_DISCONNECTED_PLAYER) {
            endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FROM_DISCONNECTED_PLAYER));
            reconnectPlayer(nickname);
        }
        else {
            endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FREE));
            connectPlayer(nickname);
        }
    }

    private void enqueue(User user, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        server.enqueueUser(user, selectedGameMode, selectedPlayersNumber);
    }

    private void reconnectPlayer(String nickname /*or maybe User*/) {
        server.reconnectPlayer(nickname, endpoint);
    }

    private void connectPlayer(String nickname){
        User user = new User(nickname, endpoint);

        //TODO listen for game type (GameMode and PlayersNumber)
        GameMode selectedGameMode = GameMode.NORMAL_MODE;
        PlayersNumber selectedPlayersNumber = PlayersNumber.TWO;

        //TODO is the way this exception is managed ok?
        try {
            enqueue(user, selectedGameMode, selectedPlayersNumber);
            endpoint.sendMessage(new GametypeResponseMessage(nickname, MessageType.GAMETYPE_RESPONSE, true));
        } catch (InvalidPlayerNumberException e) {
            e.printStackTrace();
        }
    }
}
