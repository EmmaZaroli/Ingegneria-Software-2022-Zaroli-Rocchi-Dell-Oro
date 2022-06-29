package it.polimi.ingsw.gamecontroller.exceptions;

/**
 * Threw when someone tries to fill a CloudTile that already has pawnColors on it
 * This is considered a severe case, because it implies that a player didn't pick the student's from the cloudTile,
 * in which case the game is closed
 */
public class FullCloudException extends Exception {
}
