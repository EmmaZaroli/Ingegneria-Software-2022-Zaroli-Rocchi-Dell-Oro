package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.List;

/**
 * generic interface, supports the update method
 */
public interface ModelObserver{
    void updateCharacterCard(CharacterCard message, Object[] parameters);

    void updateIslandCard(IslandCard message);

    void updateGamePhase(GamePhase message);

    void updatePlayer(Player message);

    void updatePlayerOnline(Player message);

    void updatePlayerCanPlay(Player message);

    void updateCloudTile(CloudTile message);

    void updatePlayerCoin(int message);

    void updateTableCoins(int message);

    void updateAssistantCard(AssistantCard message);

    void updateSchoolBoard(SchoolBoard message);

    void updateException(Exception message);

    void updateAskStudent();

    void updateWinners(List<String> message);

    void updateGameOverFromDisconnection();

    void updateEnoughPlayerOnline(boolean message);

    void updateExpertParameters(ExpertGameParameters parameters);
}
