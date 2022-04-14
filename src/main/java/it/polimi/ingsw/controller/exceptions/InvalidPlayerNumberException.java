package it.polimi.ingsw.controller.exceptions;

/**
 * Signals that in GameControllerBuilder the specified PlayerCount does not match the actual number of player passed to the builder
 */
public class InvalidPlayerNumberException extends Exception{
    //TODO find a better name for the enum
    public enum ExceptionType{
        TOO_MANY_PLAYERS,
        NOT_ENOUGH_PLAYERS
    }

    private ExceptionType exceptionType;

    public InvalidPlayerNumberException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
