package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.PlayerCountIcon;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.exceptions.IllegalAssistantException;
import it.polimi.ingsw.model.exceptions.NotAllowedMotherNatureMovementException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//TODO order methods by access modifier: public, protected, private
public class Game {
    private final Player[] players;
    private final Table table;

    private GamePhase gamePhase;
    private int playedCount;

    private int currentPlayer;
    private int firstPlayerInRound;
    private SchoolBoard currentPlayerBoard;
    private boolean isFirstRound;

    //TODO manage game over logics
    public Game(Player[] players) {
        this.players = players;
        this.table = players.length == 3 ? new Table(PlayerCountIcon.THREE) : new Table(PlayerCountIcon.TWO_FOUR);

        Bag bag = this.table.getBag();
        for (Player c : this.players) {
            c.getBoard().addStudentToEntrance(bag.drawStudents(7));   //TODO gameParams
        }

        this.currentPlayer = 0;
        this.isFirstRound = true;
        this.gamePhase = GamePhase.PLANNING;
    }

    private boolean isTurnComplete() {
        return this.playedCount == this.players.length;
    }

    private void planning() {
        this.table.fillClouds();

        this.playedCount = 0;
    }

    //TODO how to check this method is called exclusively by the currentPlayer?
    public void playAssistant(int assistant) throws IllegalActionException, IllegalAssistantException {
        if (this.gamePhase != GamePhase.PLANNING) {
            throw new IllegalActionException();
        }
        if (!this.canPlayAssistant(assistant)) {
            throw new IllegalAssistantException();
        }
        //TODO this.players[this.currentPlayer].playAssistant(assistant);

        this.playerHasEnded();
    }

    //TODO this doesn't work
    private void playerHasEnded() {
        this.playedCount++;

        if (!this.isTurnComplete()) {
            this.currentPlayer = pickNextPlayer();
            this.currentPlayerBoard = players[this.currentPlayer].getBoard();
        } else {
            this.gamePhase = this.pickNextPhase();
            if (this.gamePhase == GamePhase.PLANNING) {
                this.planning();
            }
        }
    }

    private GamePhase pickNextPhase() {
        return switch (this.gamePhase) {
            case PLANNING -> GamePhase.ACTION_MOVE_STUDENTS;
            case ACTION_MOVE_STUDENTS -> GamePhase.ACTION_MOVE_MOTHER_NATURE;
            case ACTION_MOVE_MOTHER_NATURE -> GamePhase.ACTION_CHOOSE_CLOUD;
            case ACTION_CHOOSE_CLOUD -> this.isTurnComplete() ? GamePhase.PLANNING : GamePhase.ACTION_MOVE_STUDENTS;
        };
    }

    //TODO check if total moved pawns == allowed pawns
    public void moveStudentToDiningRoom(PawnColor pawn) throws IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_MOVE_STUDENTS) {
            throw new IllegalActionException();
        }
        this.currentPlayerBoard.moveStudentFromEntranceToDiningRoom(pawn);
        this.checkProfessorsStatus();
    }

    private void checkProfessorsStatus() {
        //TODO
    }

    public void moveStudentToIsle(PawnColor pawn, int isle) {
        //TODO table receives an int instead of an Isle
        //this.table.movePawnOnIsland(pawn, isle);
    }

    public void moveMotherNature(int steps) throws NotAllowedMotherNatureMovementException, IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_MOVE_MOTHER_NATURE) {
            throw new IllegalActionException();
        }
        if (steps < 1 || steps > this.players[currentPlayer].getDiscardPileHead().getMotherNatureMovement()) {
            throw new NotAllowedMotherNatureMovementException();
        }
        this.table.moveMotherNature(steps);
        //TODO add logic to build towers
        //TODO add logic to return towers to schoolBoard
    }

    public void pickStudentsFromCloud(int cloudNumber) throws IllegalActionException {
        if (this.gamePhase != GamePhase.ACTION_CHOOSE_CLOUD) {
            throw new IllegalActionException();
        }
        //TODO int better than reference: already written some methods ago...
        //TODO check params validity
        List<PawnColor> studentsFromCloud = this.table.takeStudentsFromCloud(null);
        currentPlayerBoard.addStudentToEntrance(studentsFromCloud);
    }

    private boolean canPlayAssistant(int assistant) {
        //considero assisant il numero dell'assistente da giocare
        //suppongo che il giocatore abbia effettivamente tale assistente nel deck
        //suppongo che il giocatore per cui si sta controllando sia quello corrente

        //se assistant è diverso da tutte le altre carte giocate, lo posso giocare
        if(isAssistantDifferentFromOthers(assistant))
            return true;

        //se assistant è uguale ad un'altra carta giocata, controllo che nel mazzo del giocatore non esista almeno una carta diversa da tutte le altre giocate
        for(AssistantCard ac : players[currentPlayer].getAssistantDeck()){
            if(!isAssistantDifferentFromOthers(ac.getValue()))
                return false;
        }
        return true;
    }

    //ritorna true se l'assistente è diverso da tutti quelli giocati dagli altri giocatori
    private boolean isAssistantDifferentFromOthers(int assistant){
        for(int i = firstPlayerInRound; i != currentPlayer; i = (i + 1) % players.length){
            if(players[i].getDiscardPileHead().getValue() == assistant)
                return false;
        }
        return true;
    }

    //TODO hopefully it will become less complex
    private int pickNextPlayer() {
        switch (gamePhase) {
            case PLANNING:
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
