package it.polimi.ingsw.client;

public class InputParser {

    public boolean checkUsername(String username) {
        if (username.isEmpty()) return false;
        return true;
    }
}
