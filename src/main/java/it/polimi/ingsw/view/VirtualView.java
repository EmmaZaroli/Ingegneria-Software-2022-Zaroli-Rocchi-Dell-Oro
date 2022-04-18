package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.messages.ChangedPhaseMessage;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public class VirtualView<T> extends Observable<T> implements Observer<T> {
    /**
     * The user owning the view.
     */
    private final String nickname;

    /**
     * The connection to the client.
     */
    private final Endpoint clientHandler;
    private final Game game;

    public VirtualView(Endpoint clientHandler, String nickname, Game game) {
        this.nickname = nickname;
        this.clientHandler = clientHandler;
        this.game = game;
    }

    public Endpoint getClientHandler() {
        return clientHandler;
    }

    public String getPlayerNickname() {
        return nickname;
    }

    /**
     * Receives a message coming from the client.
     *
     * @param message a message coming from the client
     */
    public void notifyGame(Message message) {
        //TODO before sending the message to the controller, it should add the nickname of the player ?
        notify((T) message);
    }

    /**
     * Receives a notification from the model
     * create a message and sends it to the Socket
     */

    @Override
    public void update(T message) {
        String currentPlayer = game.getPlayers()[game.getCurrentPlayer()].getNickname();
        //TODO do we need to send every message to every player?

        // sent to every player
        if (message.getClass().equals(GamePhase.class)) {
            switch (game.getGamePhase()) {
                case PLANNING -> clientHandler.sendMessage(new GetDeckMessage(currentPlayer, MessageType.PLANNING, game.getPlayers()[game.getCurrentPlayer()].getAssistantDeck()));
                case ACTION_MOVE_STUDENTS -> clientHandler.sendMessage(new ChangedPhaseMessage(currentPlayer, MessageType.ACTION_MOVE_STUDENTS, "move" + game.getParameters().getStudentsToMove() + "students"));
                case ACTION_MOVE_MOTHER_NATURE -> clientHandler.sendMessage(new ChangedPhaseMessage(currentPlayer, MessageType.ACTION_MOVE_MOTHER_NATURE, ""));
                case ACTION_CHOOSE_CLOUD -> clientHandler.sendMessage(new ChangedPhaseMessage(currentPlayer, MessageType.ACTION_CHOOSE_CLOUD, ""));
            }
        }

        if (message.getClass().equals(Player.class)) {
            clientHandler.sendMessage(new ChangedPhaseMessage(currentPlayer, MessageType.CHANGE_PLAYER, ""));
        }

        if (message.getClass().equals(CloudTile.class)) {
            clientHandler.sendMessage(new CloudMessage("server", MessageType.CLOUD, (CloudTile) message));
        }

        if (message.getClass().equals(IslandCard.class)) {
            clientHandler.sendMessage(new IslandMessage(MessageType.ISLAND, (IslandCard) message));
        }

        if (message.getClass().equals(AssistantCard.class)) {
            clientHandler.sendMessage(new AssistantPlayedMessage(currentPlayer, MessageType.ASSISTANT_CARD, (AssistantCard) message));
        }

        if (message.getClass().equals(CharacterCard.class)) {
            clientHandler.sendMessage(new CharacterCardMessage(currentPlayer, MessageType.CHARACTER_CARD, (CharacterCard) message));
        }


        if (message.getClass().equals(String.class)) {
            //game over, sent to every player
            if (game.isGameOver()) {
                if (((String) message).equals("draw")) {
                    clientHandler.sendMessage(new WinMessage(MessageType.DRAW, false));
                } else {
                    clientHandler.sendMessage(new WinMessage(MessageType.GAME_OVER, ((String) message).equals(this.nickname)));
                }
            }
            //error sent only to the current player
            else if (this.nickname.equals(currentPlayer)) {
                clientHandler.sendMessage(new ErrorMessage(currentPlayer, (String) message));
            }
        }

        //sends only to the current player
        if (message.getClass().equals(SchoolBoard.class) && currentPlayer.equals(this.nickname)) {
            clientHandler.sendMessage(new SchoolBoardMessage(currentPlayer, MessageType.BOARD, (SchoolBoard) message));
        }

        //player's coins varied
        if (message.getClass().equals(Integer.class) && currentPlayer.equals(this.nickname)) {
            clientHandler.sendMessage(new CoinMessage(currentPlayer, (int) message));
        }

    }
}
