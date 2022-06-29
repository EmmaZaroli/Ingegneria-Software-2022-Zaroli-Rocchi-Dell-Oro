package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.ExpertPlayer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Wizard;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Player Dto
 */
public class PlayerDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 105L;

    private final String nickname;
    private final Wizard wizard;
    private final SchoolBoardDto schoolBoard;
    private final AssistantCard discardPileHead;
    private final boolean isFromActualTurn;
    private final ArrayList<AssistantCard> deck;
    private final int coins;
    private final boolean isOnline;
    private final boolean canPlayThisRound;

    /**
     * Construct the playerDto from a Player
     * @param origin the Player
     */
    public PlayerDto(Player origin) {
        this.nickname = origin.getNickname();
        this.wizard = origin.getWizzard();
        this.schoolBoard = new SchoolBoardDto(origin.getBoard());
        this.discardPileHead = origin.getDiscardPileHead();
        this.isFromActualTurn = origin.isFromActualTurn();
        this.deck = new ArrayList<>(origin.getAssistantDeck());
        this.isOnline = origin.isOnline();
        this.canPlayThisRound = origin.canPlayThisRound();
        this.coins = origin instanceof ExpertPlayer ? ((ExpertPlayer) origin).getCoins() : 0;
    }

    /**
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return the Wizard type
     */
    public Wizard getWizard() {
        return wizard;
    }

    /**
     *
     * @return the schoolBoardDto of this player
     */
    public SchoolBoardDto getSchoolBoard() {
        return schoolBoard;
    }

    /**
     *
     * @return the last assistantCard played
     */
    public AssistantCard getDiscardPileHead() {
        return discardPileHead;
    }

    /**
     *
     * @return true if his its actual turn
     */
    public boolean isFromActualTurn() {
        return isFromActualTurn;
    }

    /**
     *
     * @return the list of assistantCard
     */
    public List<AssistantCard> getDeck() {
        return deck;
    }

    /**
     *
     * @return the number of the player's coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     *
     * @return true if the player is online
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
}
