package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.messages.Message;

public class User {
    private final String nickname;
    private Endpoint endpoint;
    private boolean online;

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

    public boolean isOnline() {
        return online;
    }

    public void setOnline() {
        this.online = true;
    }

    public void setOffline() {
        this.online = false;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void sendMessage(Message message) {
        endpoint.sendMessage(message);
    }
}