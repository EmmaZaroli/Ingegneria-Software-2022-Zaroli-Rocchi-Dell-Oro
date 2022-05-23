package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class GameDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 104L;

    private List<PlayerDto> opponents = new ArrayList<>();
    private List<SchoolBoardDto> opponentsBoard = new ArrayList<>();
    private PlayerDto me;
    private List<CloudTileDto> clouds;
    private List<IslandCardDto> islands;
    private int tableCoins;
    private SchoolBoardDto schoolBoard;

    public GameDto(Game origin, String nickname) {
        Optional<Player> currentPlayerOptional = Arrays.stream(origin.getPlayers()).filter(x -> x.getNickname().equals(nickname)).findFirst();
        if (currentPlayerOptional.isPresent()) {
            Player currentPlayer = currentPlayerOptional.get();
            for (Player player : origin.getPlayers()) {
                if (!player.getNickname().equals(nickname)) {
                    opponents.add(new PlayerDto(player));
                    opponentsBoard.add(new SchoolBoardDto(player.getBoard()));
                }
            }
            this.me = new PlayerDto(currentPlayer);
            this.clouds = origin.getTable().getCloudTiles().stream().map(x -> new CloudTileDto(x)).toList();
            this.islands = origin.getTable().getIslands().stream().map(x -> new IslandCardDto(x)).toList();
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

    public List<IslandCardDto> getIslands() {
        return islands;
    }

    public int getTableCoins() {
        return tableCoins;
    }

    public List<PlayerDto> getOpponents() {
        return this.opponents;
    }

    public List<SchoolBoardDto> getOpponentsBoard() {
        return this.opponentsBoard;
    }

    public SchoolBoardDto getSchoolBoard() {
        return this.schoolBoard;
    }
}
