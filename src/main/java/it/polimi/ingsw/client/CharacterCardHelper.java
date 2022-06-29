package it.polimi.ingsw.client;

import it.polimi.ingsw.client.modelview.LinkedIslands;
import it.polimi.ingsw.client.modelview.PlayerInfo;
import it.polimi.ingsw.client.modelview.ViewCharacterCard;
import it.polimi.ingsw.model.enums.PawnColor;
import it.polimi.ingsw.utils.ApplicationConstants;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Character Card Helper
 * Classed used client-side for checking the validity of the parameters of each characterCard played by the player
 */
public class CharacterCardHelper {

    //check client side

    /**
     * Checking if the parameters of character_1 are valid
     * @param card the viewCharacterCard
     * @param parameters  the parameters
     * @param islands the linkedIslands
     * @return true if the parameters are valid, false otherwise
     */
    protected static boolean areParametersOkCharacter1(ViewCharacterCard card, Object[] parameters, List<LinkedIslands> islands){
        if(parameters.length != 2)
            return false;
        if (!(parameters[0] instanceof PawnColor && parameters[1] instanceof UUID))
            return false;
        if(!(card.isWithSetUpAction()))
            return false;
        if(!(card.getStudents().contains(parameters[0])))
            return false;

        for (LinkedIslands island : islands) {
            if (island.getIsland().getUuid().equals(parameters[1]))
                return true;
        }
        return false;
    }

    //check client side

    /**
     * Checking if the parameters of character_7 are valid
     * @param card the viewCharacterCard
     * @param parameters the parameters
     * @param playerInfo the playerInfo
     * @return true if the parameters are valid, false otherwise
     */
    protected static boolean areParametersOkCharacter7(ViewCharacterCard card, Object[] parameters, PlayerInfo playerInfo){
        if(parameters.length != 2)
            return false;
        if (!(parameters[0] instanceof List<?> && parameters[1] instanceof List<?>))
            return false;
        if(!(card.isWithSetUpAction()))
            return false;
        List<PawnColor> colorsFromCard = (List<PawnColor>) parameters[0];
        List<PawnColor> colorsFromEntrance = (List<PawnColor>) parameters[1];
        Map<PawnColor, Integer> cardinalityCard = card.getStudentsCardinality();
        Map<PawnColor, Integer> cardinalityEntrance = playerInfo.getBoard().getStudentsInEntranceCardinality();
        for (PawnColor color : PawnColor.values()) {
            if (colorsFromCard.stream().filter(x -> x == color).count() > cardinalityCard.get(color))
                return false;
        }
        for (PawnColor color : PawnColor.values()) {
            if (colorsFromEntrance.stream().filter(x -> x == color).count() > cardinalityEntrance.get(color))
                return false;
        }
        return true;
    }

    //check client and server side

    /**
     * Checking if the parameters of character_9 are valid
     * @param parameters the parameters
     * @return true if the parameters are valid, false otherwise
     */
    protected static boolean areParametersOkCharacter9(Object[] parameters) {
        return (parameters.length == 1) && (parameters[0] instanceof PawnColor);
    }

    //check client side

    /**
     * Checking if the parameters of character_11 are valid
     * @param card the viewCharacterCard
     * @param parameters the parameters
     * @param player the playerInfo
     * @return true if the parameters are valid, false otherwise
     */
    protected static boolean areParametersOkCharacter11(ViewCharacterCard card, Object[] parameters, PlayerInfo player){
        if(parameters.length != 1)
            return false;
        if (!(parameters[0] instanceof PawnColor))
            return false;
        if(!(card.isWithSetUpAction()))
            return false;
        if(!card.getStudents().contains(parameters[0]))
            return false;
        return player.getBoard().getDiningRoom().getStudentsInDiningRoom((PawnColor) parameters[0]) < ApplicationConstants.STUDENTS_IN_DININGROOM;
    }
}

