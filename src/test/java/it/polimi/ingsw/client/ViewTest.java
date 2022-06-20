package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.dtos.CloudTileDto;
import it.polimi.ingsw.dtos.SchoolBoardDto;
import it.polimi.ingsw.gamecontroller.enums.GameMode;
import it.polimi.ingsw.gamecontroller.enums.PlayersNumber;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.model.enums.Wizzard;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.servercontroller.enums.NicknameStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.Optional;



class ViewTest{

    View view = new TestView();
    String nickname = "Player1";

    @Test
    void nicknameResponseMessage(){
        NicknameResponseMessage message = new NicknameResponseMessage("Another Nickname", NicknameStatus.FROM_DISCONNECTED_PLAYER);
        view.onMessageReceived(message);
        Assertions.assertEquals("Another Nickname", view.getMe().getNickname());

        message = new NicknameResponseMessage(nickname, NicknameStatus.FREE);
        view.onMessageReceived(message);
        Assertions.assertEquals(nickname, view.getMe().getNickname());
    }

    @Test
    void gametypeResponseMessageNotOk(){
        nicknameResponseMessage();

        GametypeResponseMessage message2 = new GametypeResponseMessage(nickname, false);
        view.onMessageReceived(message2);
        Assertions.assertEquals(ErrorMessages.PARAMETERS_ERROR, view.getError());
    }

    @Test
    void gametypeResponseMessageOk(){
        nicknameResponseMessage();

        GametypeResponseMessage message = new GametypeResponseMessage(nickname, true);
        view.onMessageReceived(message);
    }

    @Test
    void gameStartingMessage(){
        gametypeResponseMessageOk();

        Player player1 = new Player(nickname, Wizzard.BLUE, Tower.BLACK, 2);
        Player player2 = new Player("Player2", Wizzard.GREEN, Tower.WHITE, 2);
        Player[] players = {player1, player2};
        Game game = new Game(players, new Table(PlayersNumber.TWO), new GameParameters(PlayersNumber.TWO, GameMode.NORMAL_MODE));
        GameMessage message = new GameMessage(nickname, MessageType.GAME_STARTING, game);
        view.onMessageReceived(message);
        Assertions.assertEquals(1, view.getOpponents().size());
        Assertions.assertEquals(0, view.getOpponentIndex("Player2").get());
        Assertions.assertTrue(comparePlayers(player2, view.getOpponents().get(0)));
        Assertions.assertTrue(comparePlayers(player1, view.getMe()));
        Assertions.assertEquals(nickname, view.getCurrentPlayer());
        Assertions.assertEquals(0, view.getCoins());
        Assertions.assertEquals(0, view.getCharacterCards().size());
        Assertions.assertTrue(compareIslands(game.getTable().getIslands(), view.getIslands()));
        Assertions.assertTrue(compareClouds(game.getTable().getCloudTiles(), view.getClouds()));
        Assertions.assertEquals(GamePhase.PLANNING, view.getCurrentPhase());
        Assertions.assertTrue(view.areEnoughPlayers());
    }

    private boolean comparePlayers(Player gamePlayer, PlayerInfo viewPlayer){
        if(!gamePlayer.getNickname().equals(viewPlayer.getNickname()))
            return false;

        if(!gamePlayer.getWizzard().equals(viewPlayer.getWizzard()))
            return false;

        if(gamePlayer.getDiscardPileHead() == null ^ viewPlayer.getDiscardPileHead().isEmpty())
            return false;
        if(!(gamePlayer.getDiscardPileHead() == null && viewPlayer.getDiscardPileHead().isEmpty())){
            if(!gamePlayer.getDiscardPileHead().equals(viewPlayer.getDiscardPileHead()))
                return false;
        }

        if(gamePlayer instanceof ExpertPlayer expertGamePlayer){
            if(expertGamePlayer.getCoins() != viewPlayer.getCoins())
                return false;
        }

        if(gamePlayer.getAssistantDeck().size() != viewPlayer.getDeck().size())
            return false;

        for(int i = 0; i < gamePlayer.getAssistantDeck().size(); i++){
            if(!(gamePlayer.getAssistantDeck().get(i).equals(viewPlayer.getDeck().get(i))))
                return false;
        }

        return compareSchoolBoard(gamePlayer.getBoard(), viewPlayer.getBoard());
    }

    private boolean compareSchoolBoard(SchoolBoard gameBoard, SchoolBoardDto viewBoard){
        if(!gameBoard.getTowerColor().equals(viewBoard.getTowerColor()))
            return false;

        if(gameBoard.getTowersCount() != viewBoard.getTowersCount())
            return false;

        for(PawnColor gameProfessor : gameBoard.getProfessors()){
            if(!viewBoard.getProfessorTable().contains(gameProfessor))
                return false;
        }
        for(PawnColor viewProfessor : viewBoard.getProfessorTable()){
            if(!gameBoard.getProfessors().contains(viewProfessor))
                return false;
        }

        Map<PawnColor, Integer> gameEntranceCardinality = gameBoard.getStudentsInEntranceCardinality();
        Map<PawnColor, Integer> viewEntranceCardinality = viewBoard.getStudentsInEntranceCardinality();
        for(PawnColor color : PawnColor.values()){
            if(gameEntranceCardinality.get(color) != viewEntranceCardinality.get(color))
                return false;
        }

        for(PawnColor color: PawnColor.getValidValues()){
            if(gameBoard.getStudentsInDiningRoom(color) != viewBoard.getDiningRoom().getStudentsInDiningRoom(color))
                return false;
        }

        return true;
    }

    private boolean compareIslands(List<IslandCard> gameIslands, List<LinkedIslands> viewIslands){
        if(gameIslands.size() != viewIslands.stream().filter(i -> i.isMainIsland()).count())
            return false;

        for(int i = 0; i < viewIslands.size(); i++){
            LinkedIslands viewIsland = viewIslands.get(i);
            if(viewIsland.getIsland().getSize() != 1 || viewIsland.getIsland().getIndices().size() != 1)
                return false;
            if(!viewIsland.isMainIsland()){
                if(viewIsland.getIsland().getStudents().size() != 0)
                    return false;
            }
            if(viewIsland.isConnectedWithNext()){
                if(viewIsland.getIsland().getTower() != viewIslands.get(Math.floorMod(i + 1, viewIslands.size())).getIsland().getTower())
                    return false;
            }
        }

        for(IslandCard gameIsland : gameIslands){
            Optional<LinkedIslands> viewIsland = viewIslands.stream().filter(l -> l.getIsland().getUuid().equals(gameIsland.getUuid())).findFirst();
            if(viewIsland.isEmpty())
                return false;
            else{
                if (!viewIsland.get().isMainIsland())
                    return false;
                if (!viewIsland.get().getIsland().getTower().equals(gameIsland.getTower()))
                    return false;
                Map<PawnColor, Integer> gameCardinality = gameIsland.getStudentsCardinality();
                for(PawnColor color: PawnColor.getValidValues()){
                    if(gameCardinality.get(color) != viewIsland.get().getIsland().getStudents().stream().filter(s -> s.equals(color)).count())
                        return false;
                }
            }
        }

        return true;
    }

    private boolean compareClouds(List<CloudTile> gameClouds, List<CloudTileDto> viewClouds){
        if(gameClouds.size() != viewClouds.size())
            return false;

        for(CloudTile gameCloud : gameClouds){
            Map<PawnColor, Integer> gameCloudCardinality = gameCloud.getStudentsCardinality();
            CloudTileDto viewCloud = viewClouds.stream().filter(c -> c.getUuid().equals(gameCloud.getUuid())).findFirst().get();
            for(PawnColor color : PawnColor.values()){
                if(gameCloudCardinality.get(color) != viewCloud.getStudents().stream().filter(s -> s.equals(color)).count())
                    return false;
            }
        }

        return true;
    }


/*
    @Test
    void reciveGameStartingMessage() {
        GameStartingMessage message = new GameStartingMessage();
        view.onMessageReceived(message);
    }*/
}

