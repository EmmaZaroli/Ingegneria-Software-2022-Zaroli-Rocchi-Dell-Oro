package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 104L;

    private List<PlayerDto> opponents;
    private PlayerDto me;
    private List<CloudTileDto> clouds;
    private int tableCoins;
    private SchoolBoardDto schoolBoard;

    public GameDto(Game origin, String nickname) {
        Optional<Player> currentPlayerOptional = Arrays.stream(origin.getPlayers()).filter(x -> x.getNickname().equals(nickname)).findFirst();
        if (currentPlayerOptional.isPresent()) {
            Player currentPlayer = currentPlayerOptional.get();
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
