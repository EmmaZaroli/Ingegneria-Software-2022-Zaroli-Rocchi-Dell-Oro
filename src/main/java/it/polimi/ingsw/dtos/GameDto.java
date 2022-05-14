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
    private final List<PlayerDto> opponents;
    private final PlayerDto me;
    private final List<CloudTileDto> clouds;
    private int tableCoins;
    private SchoolBoardDto schoolBoard;

    public GameDto(Game origin, String nickname) {
        Player currentPlayer = Arrays.stream(origin.getPlayers()).filter(x -> x.getNickname().equals(nickname)).findFirst().get();
        this.opponents = Arrays.stream(origin.getPlayers())
                .filter(x -> !x.getNickname().equals(nickname))
                .map(x -> new PlayerDto(x))
                .toList();
        this.me = new PlayerDto(currentPlayer);
        this.clouds = origin.getTable().getCloudTiles().stream().map(x -> new CloudTileDto(x)).toList();
        if (origin instanceof ExpertGame expertGame) {
            this.tableCoins = expertGame.getTable().getCoins();
        }
        this.schoolBoard = new SchoolBoardDto(currentPlayer.getBoard());
    }

    public PlayerDto getMe() {
        return me;
    }

    public List<CloudTileDto> getClouds() {
        return clouds;
    }

    public int getTableCoins() {
        return tableCoins;
    }

    public List<PlayerDto> getOpponents() {
        return this.opponents;
    }

    public SchoolBoardDto getSchoolBoard() {
        return this.schoolBoard;
    }
}
