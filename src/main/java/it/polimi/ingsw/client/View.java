package it.polimi.ingsw.client;

/**
 * ModelView class contains a small representation of the game model
 */
abstract class View {
    private String nickname;
    private int numberOfPlayer;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }
}
