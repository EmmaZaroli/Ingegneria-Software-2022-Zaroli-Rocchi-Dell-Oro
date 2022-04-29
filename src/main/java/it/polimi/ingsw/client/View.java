package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.IslandCard;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.ArrayList;
import java.util.List;

/**
 * View class contains a small representation of the game model
 */
public abstract class View {
    private String nickname;
    private SchoolBoard board;
    private AssistantCard cardThrown;
    public List<CloudTile> clouds = new ArrayList<>();

    public SchoolBoard getBoard() {
        return board;
    }

    
    public void setBoard(SchoolBoard board) {
        this.board = board;
    }

    public AssistantCard getCardThrown() {
        return cardThrown;
    }

    public void setCardThrown(AssistantCard cardThrown) {
        this.cardThrown = cardThrown;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public abstract void init();

    public abstract void askPlayerNickname();

    public abstract void showNicknameResult(boolean nicknameAccepted, boolean playerReconnected);

    public abstract void askGameSettings();

    public abstract void genericMessage(String Message);

    public abstract void changePhase(GamePhase phase);

    public abstract void askAssistantCard(ArrayList<AssistantCard> deck);

    public abstract void updateAssistantCardPlayed(AssistantCard card, String player);

    public abstract void askMotherNatureSteps();

    public abstract void updateCurrentPlayersTurn(String otherPlayer);

    public abstract void updateCloud(CloudTile cloud);

    public abstract void updateIslands(IslandCard island);

    public abstract void updateSchoolBoard(String player, SchoolBoard schoolBoard);

    public abstract void win();

    public abstract void lose();

    public abstract void draw();

    public abstract void errorAndExit(String error);

    public abstract void error(String error);

}
