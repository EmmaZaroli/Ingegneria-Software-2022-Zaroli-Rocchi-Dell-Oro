package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.ModelObserver;
import it.polimi.ingsw.servercontroller.User;

import java.util.Optional;
import java.util.List;

/**
 * Virtual View
 */
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

    /**
     *
     * @return the Endpoint
     */
    public Optional<Endpoint> getClientHandler() {
        return user.getEndpoint();
    }

    /**
     *
     * @return the nickname of the player
     */
    public String getPlayerNickname() {
        return user.getNickname();
    }

    /**
     *
     * @return true if the player is online
     */
    public boolean isOnline() {
        return user.isOnline();
    }

    /**
     * Receives a message coming from the client.
     * Notify the GameController
     * @param message the message coming from the client
     */
    public void onMessageReceived(Message message) {
        notifyMessage(message);
    }

    /**
     *
     * @return the nickname of the current player
     */
    private String getCurrentPlayer() {
        return game.getPlayers()[game.getCurrentPlayer()].getNickname();
    }

    /**
     * Notifies the players a change on the given CharacterCard
     *
     * @param message The changed CharacterCard
     * @param parameters the characterCard's parameters
     */
    @Override
    public void updateCharacterCard(CharacterCard message, Object[] parameters) {
        user.sendMessage(new CharacterCardMessage(getCurrentPlayer(), MessageType.UPDATE_CHARACTER_CARD, message, parameters));
    }

    /**
     * Notifies the players a change on the expertParameters
     *
     * @param message The changed expertParameters
     */
    @Override
    public void updateExpertParameters(ExpertGameParameters message) {
        user.sendMessage(new ExpertParametersMessage(getCurrentPlayer(), message));
    }

    /**
     * Notifies the players a change on the IslandCard
     *
     * @param message The changed IslandCard
     */
    @Override
    public void updateIslandCard(IslandCard message) {
        user.sendMessage(new IslandMessage(MessageType.UPDATE_ISLAND, message));
    }

    /**
     * Notifies the players a change on the gamePhase
     *
     * @param message The new gamePhase
     */
    @Override
    public void updateGamePhase(GamePhase message) {
        user.sendMessage(new ChangedPhaseMessage(getCurrentPlayer(), message));
    }

    /**
     * Notifies the players the new current player
     *
     * @param message The new current player
     */
    @Override
    public void updatePlayer(Player message) {

        user.sendMessage(new ChangedPlayerMessage(getCurrentPlayer()));
        if (game.getGamePhase().equals(GamePhase.PLANNING) || game.getGamePhase().equals(GamePhase.ACTION_END))
            user.sendMessage(new GetDeckMessage(getCurrentPlayer(), MessageType.PLANNING, game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()));
    }

    /**
     * Notifies the players the connection state of a player
     *
     * @param message the player
     */
    @Override
    public void updatePlayerOnline(Player message) {
        if(message.isOnline())
            user.sendMessage(new ConnectionMessage(message.getNickname(), MessageType.IS_ONLINE));
        else
            user.sendMessage(new ConnectionMessage(message.getNickname(), MessageType.IS_OFFLINE));
    }

    /**
     * Notifies the players if a play can or can't play in that round
     * @param message the player
     */
    @Override
    public void updatePlayerCanPlay(Player message) {
        user.sendMessage(new PlayerCanPlayMessage(message.getNickname(), message.canPlayThisRound()));
    }

    /**
     * Notifies the players the number of their coins
     * @param message the player's coins
     */
    @Override
    public void updatePlayerCoin(int message) {
        user.sendMessage(new CoinMessage(getCurrentPlayer(),message,false));
    }

    /**
     * Update the number of coins on the table
     * @param message the number of coins on the table
     */
    @Override
    public void updateTableCoins(int message) {
        user.sendMessage(new CoinMessage(getCurrentPlayer(),message,true));
    }

    /**
     * Notifies the players the changed cloudTile
     * @param message the cloudTile
     */
    @Override
    public void updateCloudTile(CloudTile message) {
        user.sendMessage(new CloudMessage("server", MessageType.UPDATE_CLOUD, message));
    }

    /**
     * Notifies the players the played assistantCard
     * @param message the assistantCard
     */
    @Override
    public void updateAssistantCard(AssistantCard message) {
        user.sendMessage(new AssistantPlayedMessage(getCurrentPlayer(), MessageType.UPDATE_ASSISTANT_CARD, message));
    }

    /**
     * Notifies the players the updated schoolBoard
     * @param message the schoolBoard
     */
    @Override
    public void updateSchoolBoard(SchoolBoard message) {
        user.sendMessage(new SchoolBoardMessage(getCurrentPlayer(), MessageType.UPDATE_BOARD, message));
    }

    /**
     * Notifies the players that an error occurred on the server
     * @param message the Exception
     */
    @Override
    public void updateException(Exception message) {
        user.sendMessage(new ErrorMessage(getCurrentPlayer(), game.getLastError().getMessage()));
    }

    /**
     * Ask the current player to move a student
     */
    @Override
    public void updateAskStudent(){
        user.sendMessage(new MoveStudentMessage(getCurrentPlayer(),MessageType.ASK_STUDENTS_TO_MOVE, PawnColor.NONE));
    }

    /**
     * Notify the winners to the players
     * @param message the list of winners
     */
    @Override
    public void updateWinners(List<String> message){
        //notify winners
        user.sendMessage(new GameOverMessage(MessageType.GAME_OVER, message));
    }

    /**
     * Notifies the players a game over from disconnection
     */
    @Override
    public void updateGameOverFromDisconnection() {
        user.sendMessage(new ConnectionMessage(getCurrentPlayer(), MessageType.GAME_OVER_FROM_DISCONNECTION));
    }

    /**
     * Notifies the player that there aren't enough players to continue playing
     * @param message true if there aren't enough players to continue playing
     */
    @Override
    public void updateEnoughPlayerOnline(boolean message) {
        if(message)
            user.sendMessage(new ConnectionMessage(getCurrentPlayer(), MessageType.ENOUGH_PLAYERS));
        else
            user.sendMessage(new ConnectionMessage(getCurrentPlayer(), MessageType.NOT_ENOUGH_PLAYERS));
    }

}