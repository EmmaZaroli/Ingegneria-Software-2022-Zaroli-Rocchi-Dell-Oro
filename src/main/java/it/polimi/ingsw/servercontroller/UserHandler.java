package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.applications.Server;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.gamecontroller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.network.DisconnectionListener;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.MessageListener;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler implements Runnable, DisconnectionListener, MessageListener {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Endpoint endpoint;
    private final Server server;

    public UserHandler(Socket socket, Server server) throws IOException {
        this.endpoint = new Endpoint(socket);
        this.endpoint.addDisconnectionListener(this);
        this.endpoint.addMessageListener(this);
        this.endpoint.startReceiving();
        this.server = server;
    }

    //TODO  remove from listener
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

        nickname = ((NicknameProposalMessage) endpoint.synchronizedReceive(NicknameProposalMessage.class)).getNickname();
        nicknameStatus = server.checkNicknameStatus(nickname);
        while (nicknameStatus == NicknameStatus.FROM_CONNECTED_PLAYER) {
            endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FROM_CONNECTED_PLAYER));
            nickname = ((NicknameProposalMessage) endpoint.synchronizedReceive(NicknameProposalMessage.class)).getNickname();
            nicknameStatus = server.checkNicknameStatus(nickname);
        }

        if (nicknameStatus == NicknameStatus.FROM_DISCONNECTED_PLAYER) {
            endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FROM_DISCONNECTED_PLAYER));
            reconnectPlayer(nickname);
        } else {
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

    private void connectPlayer(String nickname) {
        User user = new User(nickname, endpoint);

        GametypeRequestMessage gametypeRequestMessage = (GametypeRequestMessage) endpoint.synchronizedReceive(GametypeRequestMessage.class);
        GameMode selectedGameMode = gametypeRequestMessage.getGameMode();
        PlayersNumber selectedPlayersNumber = gametypeRequestMessage.getPlayersNumber();

        try {
            enqueue(user, selectedGameMode, selectedPlayersNumber);
            endpoint.sendMessage(new GametypeResponseMessage(nickname, MessageType.GAME_TYPE_RESPONSE, true));
        } catch (InvalidPlayerNumberException e) {
            logger.log(Level.SEVERE, MessagesHelper.ERROR_CREATING_GAME, e);
        }
    }
}
