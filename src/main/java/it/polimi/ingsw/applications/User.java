package it.polimi.ingsw.applications;

import it.polimi.ingsw.network.Endpoint;

public class User {
    private final String nickname;
    private final Endpoint endpoint;

    public User(String nickname, Endpoint endpoint) {
        this.nickname = nickname;
        this.endpoint = endpoint;
    }

    public String getNickname() {
        return nickname;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }
}
