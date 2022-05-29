package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.dtos.PlayerDto;
import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.Wizzard;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {
    private String nickname;
    private Wizzard wizzard;
    private SchoolBoardDto schoolBoard;
    private AssistantCard discardPileHead;
    private int coins;
    private List<AssistantCard> deck = new ArrayList<>();
    private boolean isOnline;

    public PlayerInfo() {
        this.nickname = "";
        this.coins = 0;
        this.isOnline = true;
    }

    public PlayerInfo(PlayerDto origin) {
        this.nickname = origin.getNickname();
        this.wizzard = origin.getWizzard();
        this.schoolBoard = origin.getSchoolBoard();
        this.discardPileHead = origin.getDiscardPileHead();
        this.deck = origin.getDeck();
        this.isOnline = origin.isOnline();
        this.coins = origin.getCoins();
    }

    private PlayerInfo(String nickname, Wizzard wizzard, SchoolBoardDto schoolBoard, AssistantCard discardPileHead, int coins, List<AssistantCard> deck, boolean isOnline) {
        this.nickname = nickname;
        this.wizzard = wizzard;
        this.schoolBoard = schoolBoard;
        this.discardPileHead = discardPileHead;
        this.deck = deck;
        this.coins = coins;
        this.isOnline = isOnline;
    }

    public PlayerInfo with(boolean online) {
        PlayerInfo retVal = this.deepClone();
        retVal.isOnline = isOnline;
        return retVal;
    }

    public PlayerInfo with(String nickname) {
        PlayerInfo retVal = this.deepClone();
        retVal.nickname = nickname;
        return retVal;
    }

    public PlayerInfo with(Wizzard wizzard) {
        PlayerInfo retVal = this.deepClone();
        retVal.wizzard = wizzard;
        return retVal;
    }

    public PlayerInfo with(AssistantCard assistantCard) {
        PlayerInfo retVal = this.deepClone();
        retVal.discardPileHead = assistantCard;
        return retVal;
    }

    public PlayerInfo with(List<AssistantCard> deck) {
        PlayerInfo retVal = this.deepClone();
        retVal.deck = deck;
        return retVal;
    }

    public PlayerInfo with(SchoolBoardDto board) {
        PlayerInfo retVal = this.deepClone();
        retVal.schoolBoard = board;
        return retVal;
    }

    public PlayerInfo with(int coins) {
        PlayerInfo retVal = this.deepClone();
        retVal.coins = coins;
        return retVal;
    }

    public String getNickname() {
        return nickname;
    }

    public Wizzard getWizzard() {
        return wizzard;
    }

    public SchoolBoardDto getBoard() {
        return schoolBoard;
    }

    public AssistantCard getDiscardPileHead() {
        return discardPileHead;
    }

    public int getCoins() {
        return coins;
    }

    public List<AssistantCard> getDeck() {
        return this.deck;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public PlayerInfo deepClone() {
        //TODO dtos if we have time
        return new PlayerInfo(this.nickname, this.wizzard, this.schoolBoard, this.discardPileHead, this.coins, this.deck, this.isOnline);
    }
}
