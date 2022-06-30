package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.applications.Server;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.gamecontroller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.messages.GametypeRequestMessage;
import it.polimi.ingsw.network.messages.GametypeResponseMessage;
import it.polimi.ingsw.network.messages.NicknameProposalMessage;
import it.polimi.ingsw.network.messages.NicknameResponseMessage;
import it.polimi.ingsw.servercontroller.enums.LoginPhase;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User Handler
 * This class is used to manage the user from the moment he connects to the server until the game starts
 */
public class UserHandler implements /*Runnable,*/ DisconnectionListener, MessageListener, GameReadyListener {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Endpoint endpoint;
    private final Server server;
    private Optional<String> nickname;
    private LoginPhase loginPhase;
    private boolean gameReady;

    public UserHandler(Socket socket, Server server) throws IOException {
        this.endpoint = new Endpoint(socket, true);
        this.endpoint.addDisconnectionListener(this);
        this.endpoint.addMessageListener(this);
        this.server = server;
        this.nickname = Optional.empty();
        this.loginPhase = LoginPhase.WAITING_FOR_NICKNAME;
        this.gameReady = false;
    }

    public UserHandler(User user, Server server){
        this.endpoint = user.getEndpoint().get();
        this.endpoint.addDisconnectionListener(this);
        this.endpoint.addMessageListener(this);
        this.server = server;
        this.nickname = Optional.of(user.getNickname());
        this.loginPhase = LoginPhase.WAITING_FOR_GAMETYPE;
        this.gameReady = false;
    }

    private void setLoginPhase(LoginPhase loginPhase) {
        this.loginPhase = loginPhase;
    }

    /**
     * Starts accepting messages from the endpoint
     */
    public void start(){
        endpoint.startReceiving();
    }

    @Override
    public void onDisconnect(Object disconnected) {
        endpoint.removeDisconnectionListener(this);
        synchronized (server) {
            nickname.ifPresent(server::removeUser);
        }
        finish();
    }

    /**
     * Since this class is used only for the initial part, it expects to receive only the messages related to the login phase
     * @param message the incoming message
     */
    @Override
    public void onMessageReceived(Message message) {
        switch (loginPhase){
            case WAITING_FOR_NICKNAME -> {
                if (message instanceof NicknameProposalMessage nicknameProposalMessage){
                    waitingForNickname(nicknameProposalMessage);
                }
            }
            case WAITING_FOR_GAMETYPE -> {
                if (message instanceof GametypeRequestMessage gametypeRequestMessage){
                    waitingForGametype(gametypeRequestMessage);
                }
            }
            case WAITING_FOR_GAMEREADY, END -> {
            }
        }
    }

    /**
     * Checks if the nickname sent belongs already to another player or if it belongs to a disconnected player
     * The result is then sent to the endpoint
     * @param message the incoming message
     */
    private void waitingForNickname(NicknameProposalMessage message){
        String nickname = message.getNickname();
        synchronized (server) {
            NicknameStatus nicknameStatus = server.checkNicknameStatus(nickname);
            switch (nicknameStatus) {
                case FREE -> {
                    endpoint.sendMessage(new NicknameResponseMessage(nickname, NicknameStatus.FREE));
                    logUser(nickname);
                    setLoginPhase(LoginPhase.WAITING_FOR_GAMETYPE);
                }
                case FROM_DISCONNECTED_PLAYER -> {
                    endpoint.sendMessage(new NicknameResponseMessage(nickname, NicknameStatus.FROM_DISCONNECTED_PLAYER));
                    reconnectUser(nickname);
                    setLoginPhase(LoginPhase.END);
                }
                case FROM_CONNECTED_PLAYER -> {
                    endpoint.sendMessage(new NicknameResponseMessage(nickname, NicknameStatus.FROM_CONNECTED_PLAYER));
                    setLoginPhase(LoginPhase.WAITING_FOR_NICKNAME);
                }
            }
        }
    }

    /**
     * Enqueue the user in the correct lobby, depending on the gameMode and the number of players selected
     * @param message the incoming message
     */
    private void waitingForGametype(GametypeRequestMessage message){
        GameMode selectedGameMode = message.getGameMode();
        PlayersNumber selectedPlayersNumber = message.getPlayersNumber();
        synchronized (server) {
            try {
                endpoint.sendMessage(new GametypeResponseMessage(nickname.get(), true));
                enqueueUser(nickname.get(), selectedGameMode, selectedPlayersNumber);
            } catch (InvalidPlayerNumberException e) {
                logger.log(Level.SEVERE, MessagesHelper.ERROR_CREATING_GAME, e);
            }
        }
        if(!gameReady)
            setLoginPhase(LoginPhase.WAITING_FOR_GAMEREADY);
    }

    /**
     * Enqueues the user
     */
    private void enqueueUser(String nickname, GameMode selectedGameMode, PlayersNumber selectedPlayersNumber) throws InvalidPlayerNumberException {
        server.enqueueUser(nickname, selectedGameMode, selectedPlayersNumber, this);
    }

    /**
     * Reconnects the user to his previous game
     * @param nickname the user's nickname
     */
    private void reconnectUser(String nickname) {
        server.reconnectUser(nickname, endpoint);
        onGameReady();
    }

    /**
     * Creates a new user that then is added to the list of users on the server
     * @param nickname the user's nickname
     */
    private void logUser(String nickname) {
        this.nickname = Optional.of(nickname);
        User user = new User(nickname, endpoint);
        server.addUser(user);
    }

    /**
     * Called if the game is starting
     * Removes this instance from the list of userHandlers on the server
     * It's removed from the list of gameStartingListener, messageListener and disconnectionListener
     */
    private void finish() {
        endpoint.removeDisconnectionListener(this);
        endpoint.removeMessageListener(this);
        server.removeGameStartingListener(this);
        server.removeUserHandler(this);
    }

    @Override
    public void onGameReady() {
        this.gameReady = true;
        finish();
        setLoginPhase(LoginPhase.END);
    }
}
