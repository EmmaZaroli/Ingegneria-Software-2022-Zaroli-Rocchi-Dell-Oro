package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.ExpertPlayer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Wizard;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

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

    public String getNickname() {
        return nickname;
    }

    public Wizard getWizzard() {
        return wizard;
    }

    public SchoolBoardDto getSchoolBoard() {
        return schoolBoard;
    }

    public AssistantCard getDiscardPileHead() {
        return discardPileHead;
    }

    public boolean isFromActualTurn() {
        return isFromActualTurn;
    }

    public ArrayList<AssistantCard> getDeck() {
        return deck;
    }

    public int getCoins() {
        return coins;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isCanPlayThisRound() {
        return canPlayThisRound;
    }
}
