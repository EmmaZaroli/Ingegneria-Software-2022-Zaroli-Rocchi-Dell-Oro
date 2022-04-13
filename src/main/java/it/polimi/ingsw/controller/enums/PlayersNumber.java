package it.polimi.ingsw.controller.enums;

public enum PlayersNumber {
    TWO(2),
    THREE(3),
    FOUR(4);

    private int playersNumber;

    public int getPlayersNumber() {
        return playersNumber;
    }

    PlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }
}
