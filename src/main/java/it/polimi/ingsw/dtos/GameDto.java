package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class GameDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 104L;

    private boolean isExpert;
    private int tableCoins = 0;
    private List<CharacterCardDto> characterCards = new ArrayList<>();
    private List<PlayerDto> opponents = new ArrayList<>();
    private PlayerDto me;
    private List<CloudTileDto> clouds;
    private List<IslandCardDto> islands;
    private GamePhase gamePhase;
    private String currentPlayer;

    public GameDto(Game origin, String nickname) {
        this.isExpert = origin instanceof ExpertGame;
        if(isExpert){
            this.tableCoins = ((ExpertGame)origin).getTable().getCoins();
            this.characterCards = Arrays.stream(((ExpertGame) origin).getCharacterCards()).map(x -> new CharacterCardDto(x)).toList();
        }
        Optional<Player> currentPlayerOptional = Arrays.stream(origin.getPlayers()).filter(x -> x.getNickname().equals(nickname)).findFirst();
        if (currentPlayerOptional.isPresent()) {
            Player currentPlayer = currentPlayerOptional.get();
            for (Player player : origin.getPlayers()) {
                if (!player.getNickname().equals(nickname)) {
                    opponents.add(new PlayerDto(player));
                }
            }
            this.me = new PlayerDto(currentPlayer);
            this.clouds = origin.getTable().getCloudTiles().stream().map(x -> new CloudTileDto(x)).toList();
            this.islands = origin.getTable().getIslands().stream().map(x -> new IslandCardDto(x)).toList();
            if (origin instanceof ExpertGame expertGame) {
                this.tableCoins = expertGame.getTable().getCoins();
            }
            this.gamePhase = origin.getGamePhase();
            this.currentPlayer = origin.getPlayer(origin.getCurrentPlayer()).getNickname();
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

    public List<PlayerDto> getOpponents() {
        return this.opponents;
    }

    public SchoolBoardDto getSchoolBoard() {
        return this.me.getSchoolBoard();
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public List<AssistantCard> getCurrentPlayerDeck() {
        return this.me.getDeck();
    }

    public boolean isExpert() {
        return isExpert;
    }

    public int getTableCoins() {
        return tableCoins;
    }

    public List<CharacterCardDto> getCharacterCards() {
        return characterCards;
    }
}
