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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler implements Runnable, DisconnectionListener, MessageListener, GameReadyListener {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Endpoint endpoint;
    private final Server server;
    private Optional<String> nickname;

    public UserHandler(Socket socket, Server server) throws IOException {
        this.endpoint = new Endpoint(socket);
        this.endpoint.addDisconnectionListener(this);
        this.endpoint.addMessageListener(this);
        this.endpoint.startReceiving();
        this.server = server;
        this.nickname = Optional.empty();
    }

    @Override
    public void onDisconnect() {
        endpoint.removeDisconnectionListener(this);
        synchronized (server) {
            nickname.ifPresent(server::removeUser);
        }
        finish();
    }

    @Override
    public void onMessageReceived(Message message) {
        //TODO dispatch to the controller
    }

    @Override
    public void run() {
        String nickname = "";
        NicknameStatus nicknameStatus;

        do {
            nickname = ((NicknameProposalMessage) endpoint.synchronizedReceive(NicknameProposalMessage.class)).getNickname();
            synchronized (server) {
                nicknameStatus = server.checkNicknameStatus(nickname);
                switch (nicknameStatus) {
                    case FREE -> {
                        endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FREE));
                        logUser(nickname);
                    }
                    case FROM_DISCONNECTED_PLAYER -> {
                        endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FROM_DISCONNECTED_PLAYER));
                        reconnectUser(nickname);
                    }
                    case FROM_CONNECTED_PLAYER -> {
                        endpoint.sendMessage(new NicknameResponseMessage(nickname, MessageType.NICKNAME_RESPONSE, NicknameStatus.FROM_CONNECTED_PLAYER));
                    }
                }
            }
        } while (nicknameStatus == NicknameStatus.FROM_CONNECTED_PLAYER);

        if (nicknameStatus == NicknameStatus.FREE) {
            GametypeRequestMessage gametypeRequestMessage = (GametypeRequestMessage) endpoint.synchronizedReceive(GametypeRequestMessage.class);
            GameMode selectedGameMode = gametypeRequestMessage.getGameMode();
            PlayersNumber selectedPlayersNumber = gametypeRequestMessage.getPlayersNumber();
            synchronized (server) {
                try {
                    enqueueUser(nickname, selectedGameMode, selectedPlayersNumber);
                    endpoint.sendMessage(new GametypeResponseMessage(nickname, MessageType.GAME_TYPE_RESPONSE, true));
                } catch (InvalidPlayerNumberException e) {
                    logger.log(Level.SEVERE, MessagesHelper.ERROR_CREATING_GAME, e);
                }
            }
        }
    }

    private void enqueueUser(String nickname, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        server.enqueueUser(nickname, selectedGameMode, selectedPlayersNumber, this);
    }

    private void reconnectUser(String nickname /*or maybe User*/) {
        server.reconnectUser(nickname, endpoint);
    }

    private void logUser(String nickname) {
        this.nickname = Optional.of(nickname);
        User user = new User(nickname, endpoint);
        server.addUser(user);
    }

    private void finish() {
        endpoint.removeDisconnectionListener(this);
        server.removeGameStartingListener(this);
    }

    @Override
    public void onGameReady() {
        finish();
    }
}
