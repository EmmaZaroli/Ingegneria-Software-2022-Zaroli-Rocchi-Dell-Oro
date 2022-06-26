package it.polimi.ingsw.gamecontroller.enums;

/**
 * The Players Number
 */
public enum PlayersNumber {
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int number;

    /**
     *
     * @return the number of players
     */
    public int getPlayersNumber() {
        return number;
    }

    /**
     *
     * @param playersNumber the number of players
     */
    PlayersNumber(int playersNumber) {
        this.number = playersNumber;
    }
}
