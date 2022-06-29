package it.polimi.ingsw.client;

/**
 * Input Parser
 */
public class InputParser {

    /**
     * Checks if the username is valid
     * @param username the player's nickname
     * @return true if the username is not null
     */
    public boolean checkUsername(String username) {
        return !username.isEmpty();
    }
}
