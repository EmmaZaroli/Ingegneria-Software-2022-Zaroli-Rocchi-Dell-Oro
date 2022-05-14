package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.ExpertPlayer;
import it.polimi.ingsw.model.Player;

import java.io.Serial;
import java.io.Serializable;

public class PlayerDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 105L;

    private final String nickname;
    private final int coins;

    public PlayerDto(Player origin) {
        this.nickname = origin.getNickname();
        this.coins = origin instanceof ExpertPlayer ? ((ExpertPlayer) origin).getCoins() : 0;
    }

    public String getNickname() {
        return nickname;
    }

    public int getCoins() {
        return coins;
    }
}
