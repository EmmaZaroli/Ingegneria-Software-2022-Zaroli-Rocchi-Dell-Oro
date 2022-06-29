package it.polimi.ingsw.dtos;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Game Dto
 */
public class GameDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 104L;

    private final boolean isExpert;
    private int tableCoins = 0;
    private List<CharacterCardDto> characterCards = new ArrayList<>();
    private final List<PlayerDto> opponents = new ArrayList<>();
    private PlayerDto me;
    private List<CloudTileDto> clouds;
    private List<IslandCardDto> islands;
    private GamePhase gamePhase;
    private String currentPlayer;
    private ExpertParametersDto expertParameters;
    private final boolean enoughPlayersOnline;

    /**
     * Construct a GameDto from a Game
     * @param origin the game
     * @param nickname the nickname of the player
     */
    public GameDto(Game origin, String nickname) {
        this.isExpert = origin instanceof ExpertGame;
        if(isExpert){
            this.tableCoins = ((ExpertGame)origin).getTable().getCoins();
            this.characterCards = Arrays.stream(((ExpertGame) origin).getCharacterCards()).map(CharacterCardDto::new).toList();
            this.expertParameters = new ExpertParametersDto(((ExpertGame)origin).getParameters());
        }
        Optional<Player> currentPlayerOptional = Arrays.stream(origin.getPlayers()).filter(x -> x.getNickname().equals(nickname)).findFirst();
        if (currentPlayerOptional.isPresent()) {
            Player currentPlayerFromGame = currentPlayerOptional.get();
            for (Player player : origin.getPlayers()) {
                if (!player.getNickname().equals(nickname)) {
                    opponents.add(new PlayerDto(player));
                }
            }
            this.me = new PlayerDto(currentPlayerFromGame);
            this.clouds = origin.getTable().getCloudTiles().stream().map(CloudTileDto::new).toList();
            this.islands = origin.getTable().getIslands().stream().map(IslandCardDto::new).toList();
            if (origin instanceof ExpertGame expertGame) {
                this.tableCoins = expertGame.getTable().getCoins();
            }
            this.gamePhase = origin.getGamePhase();
            this.currentPlayer = origin.getPlayer(origin.getCurrentPlayer()).getNickname();
        }
        this.enoughPlayersOnline = origin.areEnoughPlayersOnline();
    }

    /**
     *
     * @return the playerDto containing this player info
     */
    public PlayerDto getMe() {
        return me;
    }

    /**
     *
     * @return the list of cloudTileDto
     */
    public List<CloudTileDto> getClouds() {
        return clouds;
    }

    /**
     *
     * @return the list of islandCardDto
     */
    public List<IslandCardDto> getIslands() {
        return islands;
    }

    /**
     *
     * @return a list of playerDto containing the opponents' info
     */
    public List<PlayerDto> getOpponents() {
        return this.opponents;
    }

    /**
     *
     * @return the player's schoolBoardDto
     */
    public SchoolBoardDto getSchoolBoard() {
        return this.me.getSchoolBoard();
    }

    /**
     *
     * @return the gamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     *
     * @return the nickname of the current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     *
     * @return true if it's an expert game
     */
    public boolean isExpert() {
        return isExpert;
    }

    /**
     *
     * @return the expertParametersDto
     */
    public ExpertParametersDto getExpertParameters() {
        return expertParameters;
    }

    /**
     *
     * @return the number of coins on the table
     */
    public int getTableCoins() {
        return tableCoins;
    }

    /**
     *
     * @return a list of the characterCardsDto on the table
     */
    public List<CharacterCardDto> getCharacterCards() {
        return characterCards;
    }

    /**
     *
     * @return true if there are enough player online to play
     */
    public boolean areEnoughPlayersOnline() {
        return enoughPlayersOnline;
    }
}
