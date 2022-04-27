package it.polimi.ingsw.client;

/**
 * ModelView class contains a small representation of the game model
 */
abstract class View {
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
