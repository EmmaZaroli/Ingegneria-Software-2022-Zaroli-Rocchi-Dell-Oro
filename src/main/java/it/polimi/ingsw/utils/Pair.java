package it.polimi.ingsw.utils;

/**
 * Used for returning a generic pair of values
 * @param <T> the first generic value
 * @param <Q> the second generic value
 */
public record Pair<T, Q>(T first, Q second) {
}
