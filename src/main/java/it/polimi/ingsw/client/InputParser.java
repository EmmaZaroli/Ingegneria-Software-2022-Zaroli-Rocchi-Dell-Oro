package it.polimi.ingsw.client;

public class InputParser {
    //TODO maybe use exceptions
    public boolean checkUsername(String username) {
        if (username.isEmpty()) return false;
        return true;
    }
}