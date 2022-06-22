package it.polimi.ingsw.utils;

public class ApplicationConstants {
    public static final int DISCONNECTION_TIMER_NOT_ENOUGH_PLAYER = 10000; //time after which to call game over from disconnection, in milliseconds
    public static final int DISCONNECTION_TIMER_PING = 2000; //time after which to call endpoint disconnection, if no message is recieved, in milliseconds
    public static final int PING_PERIOD = 500; //time between one ping and the next one, in milliseconds
    public static final int MINIMUM_ONLINE_PLAYER = 2; //minimum number of online player to play the game

}
