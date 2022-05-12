package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GameDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 104L;

    //TODO player DTO
    private final List<Player> opponents;
    private final Player me;
    private final List<CloudTileDto> clouds;
    private int tableCoins;

    public GameDto(Game origin, String nickname) {
        this.opponents = Arrays.stream(origin.getPlayers()).filter(x -> !x.getNickname().equals(nickname)).toList();
        this.me = Arrays.stream(origin.getPlayers()).filter(x -> x.getNickname().equals(nickname)).findFirst().get();
        this.clouds = origin.getTable().getCloudTiles().stream().map(x -> new CloudTileDto(x)).toList();
        if (origin instanceof ExpertGame expertGame) {
            this.tableCoins = expertGame.getTable().getCoins();
        }
    }
}
