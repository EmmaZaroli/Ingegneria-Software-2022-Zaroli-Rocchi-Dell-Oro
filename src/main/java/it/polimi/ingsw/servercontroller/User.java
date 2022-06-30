package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.network.Endpoint;
import it.polimi.ingsw.network.Message;

import java.util.Optional;

/**
 * User
 * Represents the individual users
 */
public class User {
    private final String nickname;
    private Optional<Endpoint> endpoint;

    public User(String nickname) {
        this.nickname = nickname;
        this.endpoint = Optional.empty();
    }

    public User(String nickname, Endpoint endpoint) {
        this.nickname = nickname;
        this.endpoint = Optional.ofNullable(endpoint);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user) {
            return nickname.equals(user.getNickname());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getNickname() {
        return nickname;
    }

    public Optional<Endpoint> getEndpoint() {
        return endpoint;
    }

    public boolean isOnline() {
        if (endpoint.isEmpty())
            return false;
        return endpoint.get().isOnline();
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = Optional.ofNullable(endpoint);
    }

    public void sendMessage(Message message) {
        endpoint.ifPresent(value -> value.sendMessage(message));
    }

}
