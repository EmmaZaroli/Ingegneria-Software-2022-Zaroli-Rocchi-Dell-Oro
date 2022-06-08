package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.ModelObserver;
import it.polimi.ingsw.observer.ModelObservable;
import it.polimi.ingsw.servercontroller.User;

import java.util.Optional;
import java.util.List;


public class VirtualView extends MessageObservable implements ModelObserver, MessageListener {
    private final User user;
    private final Game game;

    public VirtualView(User user, Game game) {
        this.user = user;
        this.game = game;
        //case loadSavedGames method has the endpoint null
        if (user.getEndpoint().isPresent())
            user.getEndpoint().get().addMessageListener(this);

    }

    public Optional<Endpoint> getClientHandler() {
        return user.getEndpoint();
    }

    public String getPlayerNickname() {
        return user.getNickname();
    }

    public boolean isOnline() {
        return user.isOnline();
    }

    /**
     * Receives a message coming from the client.
     *
     * @param message a message coming from the client
     */
    public void onMessageReceived(Message message) {
        notifyMessage(message);
    }

    private String getCurrentPlayer() {
        return game.getPlayers()[game.getCurrentPlayer()].getNickname();
    }

    /**
     * Notifies the observers about a change on the given CharacterCard
     *
     * @param message The changed CharacterCard
     */
    @Override
    public void updateCharacterCard(CharacterCard message, Object[] parameters) {
        user.sendMessage(new CharacterCardMessage(getCurrentPlayer(), MessageType.UPDATE_CHARACTER_CARD, message, parameters));
    }

    @Override
    public void updateExpertParameters(ExpertGameParameters message) {
        user.sendMessage(new ExpertParametersMessage(getCurrentPlayer(), message));
    }

    @Override
    public void updateIslandCard(IslandCard message) {
        user.sendMessage(new IslandMessage(MessageType.UPDATE_ISLAND, message));
    }

    @Override
    public void updateGamePhase(GamePhase message) {
        switch (game.getGamePhase()) {
            case PLANNING ->  user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));
            case ACTION_MOVE_STUDENTS -> user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));
            case ACTION_MOVE_MOTHER_NATURE -> user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));
            case ACTION_CHOOSE_CLOUD -> user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));
        }
    }

    @Override
    public void updatePlayer(Player message) {

        user.sendMessage(new ChangedPlayerMessage(getCurrentPlayer()));
        if (game.getGamePhase().equals(GamePhase.PLANNING) || game.getGamePhase().equals(GamePhase.ACTION_END))
            user.sendMessage(new GetDeckMessage(getCurrentPlayer(), MessageType.PLANNING, game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()));
    }

    @Override
    public void updatePlayerOnline(Player message) {
        if(message.isOnline())
            user.sendMessage(new ConnectionMessage(message.getNickname(), MessageType.IS_ONLINE));
        else
            user.sendMessage(new ConnectionMessage(message.getNickname(), MessageType.IS_OFFLINE));
    }

    @Override
    public void updatePlayersCoin(int message) {
        user.sendMessage(new CoinMessage(getCurrentPlayer(),message,false));
    }

    @Override
    public void updateCloudTile(CloudTile message) {
        user.sendMessage(new CloudMessage("server", MessageType.UPDATE_CLOUD, message));
    }

    @Override
    public void updateAssistantCard(AssistantCard message) {
        user.sendMessage(new AssistantPlayedMessage(getCurrentPlayer(), MessageType.UPDATE_ASSISTANT_CARD, message));
    }

    @Override
    public void updateSchoolBoard(SchoolBoard message) {
        user.sendMessage(new SchoolBoardMessage(getCurrentPlayer(), MessageType.UPDATE_BOARD, message));
    }

    @Override
    public void updateException(Exception message) {
        user.sendMessage(new ErrorMessage(getCurrentPlayer(), game.getLastError().getMessage()));
    }

    @Override
    public void updateAskStudent(){
        user.sendMessage(new MoveStudentMessage(getCurrentPlayer(),MessageType.ASK_STUDENTS_TO_MOVE, PawnColor.NONE));
    }

    @Override
    public void updateWinners(List<String> message){
        //notify winners
        user.sendMessage(new GameOverMessage(MessageType.GAME_OVER, message));
    }

    @Override
    public void updateGameOverFromDisconnection() {
        user.sendMessage(new ConnectionMessage(getCurrentPlayer(), MessageType.GAME_OVER_FROM_DISCONNECTION));
    }

    @Override
    public void updateEnoughPlayerOnline(boolean message) {
        if(message)
            user.sendMessage(new ConnectionMessage(getCurrentPlayer(), MessageType.ENOUGH_PLAYERS));
        else
            user.sendMessage(new ConnectionMessage(getCurrentPlayer(), MessageType.NOT_ENOUGH_PLAYERS));
    }

}