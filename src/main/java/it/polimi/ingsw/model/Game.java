package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class Game {
    private Player[] players;
    private int currentPlayer;
    private boolean isFirsRound;
    private GamePhase gamePhase;

    public Game(Player[] players) {
        this.players = players; //TODO eventualmente verranno passati solo i nickname e i wizard, e nel costruttore verranno costruiti i player
        this.currentPlayer = 0;
        this.isFirsRound = true;
        this.gamePhase = GamePhase.PLANNING_ADD_STUDENTS_TO_CLOUD;
    }

    private int pickNextPlayer() {
        switch (gamePhase) {
            case PLANNING_ADD_STUDENTS_TO_CLOUD:
            case PLANNING_PLAY_ASSISTANT:
                return (currentPlayer + 1) % players.length;
            case ACTION_MOVE_STUDENTS:
            case ACTION_MOVE_MOTHER_NATURE:
            case ACTION_CHOOSE_CLOUD:
                Optional<Player> nextPlayer = Arrays.stream(players).filter((Player p) -> {
                    return p.getDiscardPileHead().getValue() >= players[currentPlayer].getDiscardPileHead().getValue();
                }).sorted((p1, p2) -> ((Integer) (p1.getDiscardPileHead().getValue())).compareTo((Integer) (p2.getDiscardPileHead().getValue()))).findFirst();
                if (!nextPlayer.isPresent())
                    nextPlayer = Optional.of(players[0]);
                for (int i = 0; i < players.length; i++) {
                    if (players[i] == nextPlayer.get())
                        return i;
                }
        }
        return 0;
    }
}
