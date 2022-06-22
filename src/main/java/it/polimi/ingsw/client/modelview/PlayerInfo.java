package it.polimi.ingsw.client.modelview;

import it.polimi.ingsw.dtos.PlayerDto;
import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.enums.Wizzard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Player Info
 * Collection of object related to the player, used by the View
 */
public class PlayerInfo {
    private String nickname;
    private Wizzard wizzard;
    private SchoolBoardDto schoolBoard;
    private Optional<AssistantCard> discardPileHead;
    private boolean isFromActualTurn = false;
    private int coins;
    private List<AssistantCard> deck = new ArrayList<>();
    private boolean isOnline;
    private boolean canPlayThisRound;

    public PlayerInfo() {
        this.nickname = "";
        this.coins = 0;
        this.isOnline = true;
    }

    public PlayerInfo(PlayerDto origin) {
        this.nickname = origin.getNickname();
        this.wizzard = origin.getWizzard();
        this.schoolBoard = origin.getSchoolBoard();
        this.discardPileHead = Optional.ofNullable(origin.getDiscardPileHead());
        this.isFromActualTurn = origin.isFromActualTurn();
        this.deck = origin.getDeck();
        this.isOnline = origin.isOnline();
        this.canPlayThisRound = origin.isCanPlayThisRound();
        this.coins = origin.getCoins();
    }

    private PlayerInfo(String nickname, Wizzard wizzard, SchoolBoardDto schoolBoard, Optional<AssistantCard> discardPileHead, int coins, List<AssistantCard> deck, boolean isOnline, boolean canPlayThisRound, boolean isFromActualTurn) {
        this.nickname = nickname;
        this.wizzard = wizzard;
        this.schoolBoard = schoolBoard;
        this.discardPileHead = discardPileHead;
        this.deck = deck;
        this.coins = coins;
        this.isOnline = isOnline;
        this.canPlayThisRound = canPlayThisRound;
        this.isFromActualTurn = isFromActualTurn;
    }

    /**
     * @param online true if the player is online, false otherwise
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo with(boolean online) {
        PlayerInfo retVal = this.deepClone();
        retVal.isOnline = online;
        return retVal;
    }

    /**
     * @param nickname the nickname of the player
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo with(String nickname) {
        PlayerInfo retVal = this.deepClone();
        retVal.nickname = nickname;
        return retVal;
    }

    /**
     * @param wizzard the Wizzard of the player
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo with(Wizzard wizzard) {
        PlayerInfo retVal = this.deepClone();
        retVal.wizzard = wizzard;
        return retVal;
    }

    /**
     * @param assistantCard the assistantCard
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo with(Optional<AssistantCard> assistantCard) {
        PlayerInfo retVal = this.deepClone();
        retVal.discardPileHead = assistantCard;
        return retVal;
    }

    /**
     * @param deck the list of assistantCards
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo with(List<AssistantCard> deck) {
        PlayerInfo retVal = this.deepClone();
        retVal.deck = deck;
        return retVal;
    }

    /**
     * @param board the schoolBoard of the player
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo with(SchoolBoardDto board) {
        PlayerInfo retVal = this.deepClone();
        retVal.schoolBoard = board;
        return retVal;
    }

    /**
     * @param coins the number of coins of the player
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo with(int coins) {
        PlayerInfo retVal = this.deepClone();
        retVal.coins = coins;
        return retVal;
    }

    /**
     * @param isFromActualTurn  is true if it's the player's actual turn
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo withIsFromActualTurn(boolean isFromActualTurn) {
        PlayerInfo retVal = this.deepClone();
        retVal.isFromActualTurn = isFromActualTurn;
        return retVal;
    }

    /**
     * @param canPlayThisRound is true if the player can play this round
     * @return the updated clone of the playerInfo
     */
    public PlayerInfo withCanPlayThisRound(boolean canPlayThisRound) {
        PlayerInfo retVal = this.deepClone();
        retVal.canPlayThisRound = canPlayThisRound;
        return retVal;
    }

    /**
     *
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return the wizzard
     */
    public Wizzard getWizzard() {
        return wizzard;
    }

    /**
     *
     * @return the schoolBoard
     */
    public SchoolBoardDto getBoard() {
        return schoolBoard;
    }

    /**
     *
     * @return the last assistantCard played
     */
    public Optional<AssistantCard> getDiscardPileHead() {
        return discardPileHead;
    }

    /**
     *
     * @return the number of coins of the player
     */
    public int getCoins() {
        return coins;
    }

    /**
     *
     * @return the list of assistantCards
     */
    public List<AssistantCard> getDeck() {
        return this.deck;
    }

    /**
     *
     * @return true if the player is online, false otherwise
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     *
     * @return true if the player can play this round
     */
    public boolean isCanPlayThisRound() {
        return canPlayThisRound;
    }

    /**
     *
     * @return true if it's the player's actual turn
     */
    public boolean isFromActualTurn() {
        return isFromActualTurn;
    }

    /**
     *
     * @return a copy of the playerInfo instance
     */
    public PlayerInfo deepClone() {
        //TODO dtos if we have time
        return new PlayerInfo(this.nickname, this.wizzard, this.schoolBoard, this.discardPileHead, this.coins, this.deck, this.isOnline, this.canPlayThisRound, this.isFromActualTurn);
    }
}
