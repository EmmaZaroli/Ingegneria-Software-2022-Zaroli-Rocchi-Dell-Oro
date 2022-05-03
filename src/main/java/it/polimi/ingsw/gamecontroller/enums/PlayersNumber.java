package it.polimi.ingsw.gamecontroller.enums;

//TODO 4 is useless
public enum PlayersNumber {
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int number;

    public int getPlayersNumber() {
        return number;
    }

    PlayersNumber(int playersNumber) {
        this.number = playersNumber;
    }
}
