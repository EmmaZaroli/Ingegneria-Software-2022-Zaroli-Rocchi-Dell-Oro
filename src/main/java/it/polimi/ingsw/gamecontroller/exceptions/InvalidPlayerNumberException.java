package it.polimi.ingsw.gamecontroller.exceptions;

/**
 * Signals that in GameControllerBuilder the specified PlayerCount does not match the actual number of player passed to the builder
 */
public class InvalidPlayerNumberException extends Exception {
    public enum Reason {
        TOO_MANY_PLAYERS,
        NOT_ENOUGH_PLAYERS
    }

    private final Reason reason;

    public InvalidPlayerNumberException(Reason reason) {
        this.reason = reason;
    }

}
