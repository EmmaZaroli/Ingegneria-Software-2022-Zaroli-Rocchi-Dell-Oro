package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.Message;

public class User {
    private final String nickname;
    private Endpoint endpoint;

    public User(String nickname) {
        this.nickname = nickname;
        this.endpoint = null;
    }

    public User(String nickname, Endpoint endpoint) {
        this.nickname = nickname;
        this.endpoint = endpoint;
    }

    @Override
    public boolean equals(Object obj) {
        return nickname.equals(((User) obj).getNickname());
    }

    public String getNickname() {
        return nickname;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public boolean isOnline() {
        if (endpoint == null)
            return false;
        return endpoint.isOnline();
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void sendMessage(Message message) {
        endpoint.sendMessage(message);
    }

}
